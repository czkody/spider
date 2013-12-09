/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spider;

import downloader.DownloaderAgent;
import downloader.DownloaderThreadFactory;
import downloader.DownloaderUrlSource;
import downloader.byteArrayQueueOutput.ByteArrayQueueInput;
import downloader.byteArrayQueueOutput.ByteArrayOutputDataFactory;
import downloader.byteArrayQueueOutput.ByteArrayOutputDataQueue;
import downloader.byteArrayQueueOutput.ByteArrayOutputHandlerFactory;
import downloader.byteArrayQueueOutput.ByteArrayQueueOutput;
import downloader.fileOutput.DefaultFileNameProvider;
import downloader.fileOutput.FileNameProvider;
import downloader.fileOutput.FileOutputHandlerFactory;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.ParserAgent;
import parser.ParserDataInput;
import parser.ParserOutputHandlerFactory;
import parser.ParserThreadFactory;
import parser.ParserUrlRecordOutput;
//import parser.xmlFileOutput.FileOutputHandlerFactory;
import urlqueue.HostInfo;
import urlqueue.QueueUrlRecordDownloaderSource;
import urlqueue.QueueUrlRecordParserOutput;
import urlqueue.UrlQueue;
import urlqueue.UrlRecord;
//import parser.xmlFileOutput.DefaultFileNameProvider;
//import parser.xmlFileOutput.FileNameProvider;

/**
 *
 * @author Petr Křenek
 */
public class Spider {

    private static final String DEFAULT_SPIDER_NAME = "Spider";
    private static final String DEFAULT_ROOTDIR = "spider";
    private static final String ERROR_ROOT_NOT_CREATED = "Root doesn't exist and cannot be created";
    private static final Logger LOGGER = Logger.getLogger(Spider.class.getName());
    private UrlQueue urlQueue;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	Spider spider = new Spider(args);
    }

    private Spider(String[] args) {
	File root = new File(DEFAULT_ROOTDIR);
	//DefaultFileNameProvider init
	if (!root.isDirectory()) {
	    if (!createRoot(root)) {
		LOGGER.log(Level.SEVERE, ERROR_ROOT_NOT_CREATED);
	    }
	}
	FileNameProvider downloaderFileNameProvider = new DefaultFileNameProvider(root);
	this.urlQueue = new UrlQueue();
	DownloaderUrlSource urlSource = new QueueUrlRecordDownloaderSource(urlQueue);
//	FileOutputHandlerFactory downloaderOutputHandlerFactory = new FileOutputHandlerFactory(downloaderFileNameProvider);
	ByteArrayOutputDataQueue downloaderOutputDataQueue = ByteArrayOutputDataQueue.getInstance(0);
	ByteArrayQueueInput byteArrayQueueInput = new ByteArrayQueueInput(downloaderOutputDataQueue);
	ByteArrayQueueOutput byteArrayQueueOutput = new ByteArrayQueueOutput(downloaderOutputDataQueue);
	ByteArrayOutputHandlerFactory downloaderOutputHandlerFactory = new ByteArrayOutputHandlerFactory(byteArrayQueueInput);
	ByteArrayOutputDataFactory downloaderOutputDataFactory = new ByteArrayOutputDataFactory();
	DownloaderThreadFactory downloaderThreadFactory = new DownloaderThreadFactory(downloaderOutputHandlerFactory, downloaderOutputDataFactory, urlSource);

	parser.xmlFileOutput.FileNameProvider parserFileNameProvider = new parser.xmlFileOutput.DefaultFileNameProvider(root);
	ParserDataInput dataInput = new ByteArrayQueueOutput(downloaderOutputDataQueue);
	ParserOutputHandlerFactory parserOutputHandlerFactory = new parser.xmlFileOutput.FileOutputHandlerFactory(parserFileNameProvider);
	ParserUrlRecordOutput parserUrlOutput = new QueueUrlRecordParserOutput(urlQueue);
	ParserThreadFactory parserThreadFactory = new ParserThreadFactory(parserOutputHandlerFactory, null, dataInput, parserUrlOutput);
	//Agents init
	DownloaderAgent downloaderAgent = new DownloaderAgent(downloaderThreadFactory);
	urlQueue.addNotifyListener(downloaderAgent);
	ParserAgent parserAgent = new ParserAgent(parserThreadFactory);
	downloaderOutputDataQueue.addNotifyListener(parserAgent);
	urlQueueInit(args);
	downloaderAgent.startThreads();
	parserAgent.startThreads();
/*	try {
	    synchronized (this) {
		wait(1000);
	    }
	} catch (InterruptedException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	try {
	    downloaderAgent.joinAll();
	} catch (InterruptedException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
*/	
	while (downloaderAgent.getRunningThreadCount() > 0) {
	    try {
		synchronized (this) {
		    wait(1000);
		}
	    } catch (InterruptedException ex) {
		LOGGER.log(Level.SEVERE, null, ex);
	    }
	}
	
	urlQueue.removeNotifyListener(downloaderAgent);
	downloaderOutputDataQueue.removeNotifyListener(parserAgent);
	HostInfo.stopAutoCleanup();
/*	while (parserAgent.getRunningThreadCount() > 0) {
	    try {
		synchronized (this) {
		    wait(1000);
		}
	    } catch (InterruptedException ex) {
		LOGGER.log(Level.SEVERE, null, ex);
	    }
	}
*/	
    }

    private void urlQueueInit(String[] args) {
	try {
	    if (args.length > 0) {
		URL url = new URL(args[0]);
		urlQueue.addUrlRecord(new UrlRecord(url, 1, HostInfo.addInstance(url.getHost(), 500)));
	    }
	    else{
		URL url = null;
//		url = new URL("http://www.ldplus.cz/");
//		urlQueue.addUrlRecord(new UrlRecord(url, 1, HostInfo.addInstance(url.getHost(), 500)));
//		url = new URL("http://www.ldplus.cz/conf/config.php");
//		urlQueue.addUrlRecord(new UrlRecord(url, 1, HostInfo.addInstance(url.getHost(), 500)));
		url = new URL("http://www.idnes.cz/");
		urlQueue.addUrlRecord(new UrlRecord(url, 50, HostInfo.getInstance(url)));
//		url = new URL("http://tomasek.krena.eu/index.php");
//		urlQueue.addUrlRecord(new UrlRecord(url, 100, HostInfo.addInstance(url.getHost(), 500)));
//		url = new URL("http://www.heureka.cz/");
//		urlQueue.addUrlRecord(new UrlRecord(url, 100, HostInfo.addInstance(url.getHost(), 50)));
//		url = new URL("http://www.ldplus.cz/kontakt/");
//		urlQueue.addUrlRecord(new UrlRecord(url, 50, HostInfo.addInstance(url.getHost(), 1000)));
//		url = new URL("http://www.jidelna-gastro.cz/kontakt/");
//		urlQueue.addUrlRecord(new UrlRecord(url, 50, HostInfo.addInstance(url.getHost(), 1000)));
	    }
	} catch (MalformedURLException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}

    }

    private static boolean createRoot(File root) {
	return root.mkdir();
    }
}
