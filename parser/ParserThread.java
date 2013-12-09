/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import abstractThread.AbstractThread;
import abstractThread.ContentType;
import downloader.DownloaderOutputData;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import javax.xml.parsers.SAXParserFactory;
import parser.sitemap.SitemapXmlData;
import urlqueue.HostInfo;
import urlqueue.UrlRecord;

/**
 * Class for parser thread
 *
 * @author Petr Křenek
 */
public class ParserThread extends AbstractThread<ParserAgent> {

    private static final String PROTOCOL_HOST_DELIMITER = "://";
    private static final String HOST_FILE_DELIMITER = "/";
    private static final long SOURCE_READ_TIMEOUT = 10000;
    private static final Logger LOGGER = Logger.getLogger(ParserThread.class.getName());
    private static final int DEFAULT_QUEUE_LENGTH = 2;
    private static final int DEFAULT_MIN_QUEUE_LENGTH = (int) (0.5 * DEFAULT_QUEUE_LENGTH);
    private static final int BUFFER_SIZE = 4096;
    private int queueLength = DEFAULT_QUEUE_LENGTH;
    private int minQueueLength = DEFAULT_MIN_QUEUE_LENGTH;
    private ParserOutputHandlerFactory outputHandlerFactory;
    private ParserOutputDataFactory outputDataFactory;
    private ParserOutputHandler outputHandler;
    private ParserDataInput dataInput;
    private LinkedList<DownloaderOutputData> queue = new LinkedList<>();
    private SAXParserFactory saxParserFactory;
    private ParserUrlRecordOutput parserUrlRecordOutput;
    private DownloaderOutputData record;

    public ParserThread(ParserAgent parserAgent, ParserOutputHandlerFactory outputHandlerFactory, 
	    ParserOutputDataFactory outputDataFactory, ParserDataInput dataInput, 
	    SAXParserFactory saxParserFactory, ParserUrlRecordOutput parserUrlRecordOutput) {
	super(parserAgent);
	if (outputHandlerFactory == null) {
	    throw new IllegalArgumentException("outputHandlerFactory");
	}
//	if (outputDataFactory == null) {
//	    throw new IllegalArgumentException("outputDataFactory");
//	}
	if (dataInput == null) {
	    throw new IllegalArgumentException("dataInput");
	}
	if (saxParserFactory == null) {
	    throw new IllegalArgumentException("saxParserFactory");
	}
	if (parserUrlRecordOutput == null) {
	    throw new IllegalArgumentException("parserUrlRecordOutput");
	}
	this.outputHandlerFactory = outputHandlerFactory;
	this.outputDataFactory = outputDataFactory;
	this.outputHandler = this.outputHandlerFactory.getHandler();
	this.dataInput = dataInput;
	this.saxParserFactory = saxParserFactory;
	this.parserUrlRecordOutput = parserUrlRecordOutput;
    }

    private synchronized void fillQueue() {
	if (queueLength <= dataInput.size()) {
	    queue.addAll(dataInput.takeDataList(queueLength - queue.size()));
	} else {
	    queue.addAll(dataInput.takeDataList(dataInput.size()));
	}
    }

    private synchronized DownloaderOutputData getNextRecord() {
	while (true) {
	    for (int i = 0; i < queue.size(); i++) {
		return queue.remove(i);
	    }
	}
    }

    @Override
    public void work() {
	SaxParserFactory mySaxParserFactory;
	AbstractSaxParser saxParser;
	List<XmlData> parsedData;
	long lastRead = new Date().getTime();
	while (((new Date().getTime() - lastRead) < SOURCE_READ_TIMEOUT)) {
	    fillQueue();
	    // vypadni kdyz je fronta prazdna, nebo kdyz je pod minimem a neni prazdny zdroj
	    while (!queue.isEmpty() && (queue.size() >= minQueueLength || dataInput.isEmpty())) {
		record = getNextRecord();
		mySaxParserFactory = new SaxParserFactory(saxParserFactory);
		saxParser = mySaxParserFactory.getSaxParser(record);
		if (saxParser!=null) {
		    /**
		     * Parse input
		     */
		    System.out.println(this.getName().concat(": ").concat(record.getUrl().toString()));
		    ByteArrayInputStream inputStream = new ByteArrayInputStream(record.getContent());
		    if (record.getContentType().contains(ContentType.GZIP)) {
			GZIPInputStream gzipInputStream = null;
			ByteArrayOutputStream byteArrayOutputStream = null;
			try {
			    gzipInputStream = new GZIPInputStream(inputStream);
			    byteArrayOutputStream = new ByteArrayOutputStream();
			    byte[] buffer = new byte[4096];
			    int c = 0;
			    while (( c = gzipInputStream.read(buffer, 0, 4096)) > 0) {
			       byteArrayOutputStream.write(buffer, 0, c);
			    }			
			} catch (IOException ex) {
			    LOGGER.log(Level.SEVERE, null, ex);
			} finally {
			    try {
				if (byteArrayOutputStream != null) {
				    byteArrayOutputStream.close();
				}
				if (gzipInputStream != null) {
				    gzipInputStream.close();
				}
			    } catch (IOException ex) {
				LOGGER.log(Level.SEVERE, null, ex);
			    }
			}
			inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		    }
		    parsedData = saxParser.parseDocument(inputStream);
		    if (record.getContentType().contains(ContentType.HTML)) {
			//if xml file exists => test match (% of change)
		    } else if (record.getContentType().contains(ContentType.APP_XML) || 
			    record.getContentType().contains(ContentType.TEXT_XML)) {
			List<UrlRecord> urlRecordList = new ArrayList<>();
			URL url = null;
			for (XmlData data : parsedData) {
			    System.out.println("URL from sitemap: ".concat(((SitemapXmlData)data).getLoc()));
			    try {
				url = new URL(((SitemapXmlData)data).getLoc());
			    } catch (MalformedURLException ex) {
				LOGGER.log(Level.SEVERE, null, ex);
			    }
			    //MAX_PRIORITY
			    urlRecordList.add(new UrlRecord(url, 10, HostInfo.getInstance(url)));
			}
			parserUrlRecordOutput.putUrlRecordList(urlRecordList);
		    }
		    lastRead = new Date().getTime();
		} else {
		    System.out.println("KO");
//		    throw new IllegalArgumentException("Unknown format of content");
		}
	    }
	    if (dataInput.isEmpty()) {
		synchronized (threadAgent) {
		    try {
			threadAgent.wait(SOURCE_READ_TIMEOUT);
		    } catch (InterruptedException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		    }
		}
	    }
	}
    }
}
