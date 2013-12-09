/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import abstractThread.AbstractThread;
import downloader.robotsTxt.RobotsTxt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import urlqueue.HostInfo;
import urlqueue.UrlRecord;
/**
 * Class for downloader thread
 *
 * @author Petr Křenek
 */
public class DownloaderThread extends AbstractThread<DownloaderAgent> {

    private static final long SOURCE_READ_TIMEOUT = 5000;
    private static final String FILENAME_ROBOTS_TXT = "/robots.txt";
    private static final String URL_PROTOCOL_DELIMITER = "://";
    private static final int DEFAULT_QUEUE_LENGTH = 2;
    private static final int DEFAULT_MIN_QUEUE_LENGTH = (int) (0.5 * DEFAULT_QUEUE_LENGTH);
    private static final int DEFAULT_BUFFER_SIZE = 16384 * 16;
    private static final String GZIP_HEADER = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HEADER_GZIP_ENCODING_VALUE = "compress, gzip";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String DEFAULT_USER_AGENT_NAME = "MyWebCrawler";
    public static final String MSG_PAGE_DISALLOWED = ": page disallowed by robots.txt";
    private LinkedList<UrlRecord> queue = new LinkedList<>();
    private DownloaderOutputHandlerFactory outputHandlerFactory;
    private DownloaderOutputHandler outputHandler;
    private DownloaderUrlSource urlSource;
    private DownloaderOutputData outputData;
    private DownloaderOutputDataFactory outputDataFactory;
//    private HostInfo hi;
    private int queueLength = DEFAULT_QUEUE_LENGTH;
    private int minQueueLength = DEFAULT_MIN_QUEUE_LENGTH;
    private static final Logger LOGGER = Logger.getLogger(DownloaderThread.class.getName());
    
    public DownloaderThread(DownloaderAgent downloaderAgent, DownloaderOutputHandlerFactory outputHandlerFactory, DownloaderOutputDataFactory outputDataFactory, DownloaderUrlSource urlSource) {
//    public DownloaderThread(DownloaderOutputHandlerFactory outputHandlerFactory, DownloaderUrlSource urlSource) {
	super(downloaderAgent);
	if (outputHandlerFactory == null) {
	    throw new IllegalArgumentException("outputHandlerFactory");
	}
		if (outputDataFactory == null) {
	throw new IllegalArgumentException("outputDataFactory");
	}
	if (urlSource == null) {
	    throw new IllegalArgumentException("urlSource");
	}
	this.outputHandlerFactory = outputHandlerFactory;
	this.outputDataFactory = outputDataFactory;
	this.outputHandler = this.outputHandlerFactory.getHandler();
	this.urlSource = urlSource;
    }

    private synchronized void fillQueue() {
	queue.addAll(urlSource.getUrlList(queueLength - queue.size()));
	Collections.sort(queue, new Comparator<UrlRecord>() {
	    @Override
	    public int compare(UrlRecord o1, UrlRecord o2) {
		return o2.getPriority() - o1.getPriority();
	    }
	});
    }

    /**
     * @return the next UrlRecord
     */
    @SuppressWarnings("empty-statement")
    private UrlRecord getNextUrlRecord() {
	synchronized (queue){
	    while (true) {
		for (int i = 0; i < queue.size(); i++) {
		    if (queue.get(i).getHostInfo().canAccess()) {
			return queue.remove(i);
		    }
		}
	    }
	}
    }

    /**
     * @return the minQueueLength
     */
    public int getMinQueueLength() {
	return minQueueLength;
    }

    /**
     * @param minQueueLength the minQueueLength to set
     */
    public void setMinQueueLength(int minQueueLength) {
	if (minQueueLength > queueLength || minQueueLength < 0) {
	    throw new IllegalArgumentException("minQueueLength");
	}
	this.minQueueLength = minQueueLength;
    }

    /**
     * @return the queueLength
     */
    public int getQueueLength() {
	return queueLength;
    }

    /**
     * @param queueLength the queueLength to set
     */
    public void setQueueLength(int queueLength) {
	if (minQueueLength > queueLength || queueLength < 1) {
	    throw new IllegalArgumentException("queueLength");
	}
	this.queueLength = queueLength;
    }

    @Override
    public void work() {
	UrlRecord record = null;
	long lastRead = new Date().getTime();
//	while (!urlSource.isEmpty()) {
	while (((new Date().getTime() - lastRead) < SOURCE_READ_TIMEOUT)) {
	    fillQueue();
	    // vypadni kdyz je fronta prazdna, nebo kdyz je pod minimem a neni prazdny zdroj
	    while (!queue.isEmpty() && (queue.size() >= minQueueLength || urlSource.isEmpty())) {
		record = getNextUrlRecord();
		record.getHostInfo().setLastAccess();
		HostInfo hi;
		synchronized (hi = HostInfo.getInstance(record.getUrl())) {
		    if (hi.isFirstVisit()) {
			hi.setFirstVisit(false);
//			try {
//			    /**
//			     * Process file robots.txt (if exists)
//			     */
//			    URL robotsTxtUrl = new URL(record.getUrl().getProtocol().concat(URL_PROTOCOL_DELIMITER).concat(record.getUrl().getHost()).concat(FILENAME_ROBOTS_TXT));
			    URL mainUrl = record.getUrl();
//			    record.setUrl(robotsTxtUrl);
////			    if (downloadPage(record)) {
//			    if (PageFetcher.downloadPage(record, outputDataFactory, outputHandler)) {
				/**
				 * Download file sitemap.xml (if exists)
				 */
			    URL sitemapUrl = record.getHostInfo().getRobotsDirectives().getSitemapUrl();
			    if (sitemapUrl != null) {
				record.setUrl(record.getHostInfo().getRobotsDirectives().getSitemapUrl());
//				downloadPage(record); 
				PageFetcher.downloadPage(record, outputDataFactory, outputHandler);
			    } 
			    record.setUrl(mainUrl);
//			} catch (MalformedURLException ex) {
//			    LOGGER.log(Level.SEVERE, null, ex);
//			}
		    }
		}
		// download page
		if (record.getHostInfo().getRobotsDirectives().allows(record.getUrl().getPath())) {
//		    if (downloadPage(record)) {
		    if (PageFetcher.downloadPage(record, outputDataFactory, outputHandler)) {
			record.getHostInfo().addVisitedUrl(record.getUrl());
			System.out.println(this.getName() + ": " + "Page " + record.getUrl().toString() + " downloaded");
		    }
		} else {
		    LOGGER.log(Level.INFO, record.getUrl().toString().concat(MSG_PAGE_DISALLOWED));
		}
		lastRead = new Date().getTime();
	    }
	    if (urlSource.isEmpty()) {
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

//    private boolean downloadPage(UrlRecord record) {
//	boolean success = false;
//	HttpURLConnection connection = null;
////	byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
//	InputStream is = null;
//	URL url = null;
//	try {
//	    url = record.getUrl();
//	    connection = (HttpURLConnection) url.openConnection();
//	    connection.addRequestProperty(HEADER_USER_AGENT, DEFAULT_USER_AGENT_NAME);
//	    connection.connect();
//	    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
//		is = connection.getInputStream();
//		outputData = outputDataFactory.getOutputData(connection.getContentType(), url);
//		if (!url.getFile().equals(FILENAME_ROBOTS_TXT)) {
//		    outputHandler.init(url);
//		    outputData.setContent(IOUtils.toByteArray(is));
//		    outputHandler.processData(outputData);
//		    outputHandler.finish();
//		} else {
//		    try (InputStreamReader isr = new InputStreamReader(is)) {
//			RobotsTxt robotstxt = new RobotsTxt(new BufferedReader(isr));
//			record.getHostInfo().setRobotsDirectives(robotstxt.getDirectivesFor(DEFAULT_USER_AGENT_NAME));
//		    }
//		}
//		success = true;
//	    }
//	} catch (Exception ex) {
//	    LOGGER.log(Level.SEVERE, null, ex);
//	} finally {
//	    
//	    if (is != null) {
//		try {
//		    is.close();
//		} catch (IOException ex) {
//		    LOGGER.log(Level.SEVERE, null, ex);
//		}
//	    }
//	    if (connection != null) {
//		connection.disconnect();
//	    }
//	    return success;
//	}
//    }
}
