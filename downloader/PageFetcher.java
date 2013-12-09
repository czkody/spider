/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import downloader.robotsTxt.RobotsTxt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import urlqueue.UrlRecord;

/**
 *
 * @author Petr Křenek
 */
public class PageFetcher {
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
    private static DownloaderOutputData outputData;
    private static final Logger LOGGER = Logger.getLogger(PageFetcher.class.getName());
    
    public static Boolean downloadPage(UrlRecord record, DownloaderOutputDataFactory outputDataFactory, DownloaderOutputHandler outputHandler) {
	boolean success = false;
	HttpURLConnection connection = null;
//	byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
	InputStream is = null;
	URL url = null;
	try {
	    url = record.getUrl();
	    connection = (HttpURLConnection) url.openConnection();
	    connection.addRequestProperty(HEADER_USER_AGENT, DEFAULT_USER_AGENT_NAME);
	    connection.connect();
	    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
		is = connection.getInputStream();
		if (!url.getFile().equals(FILENAME_ROBOTS_TXT)) {
		    outputData = outputDataFactory.getOutputData(connection.getContentType(), url);
		    outputHandler.init(url);
		    outputData.setContent(IOUtils.toByteArray(is));
		    outputHandler.processData(outputData);
		    outputHandler.finish();
		} else {
		    try (InputStreamReader isr = new InputStreamReader(is)) {
			RobotsTxt robotstxt = new RobotsTxt(new BufferedReader(isr));
			record.getHostInfo().setRobotsDirectives(robotstxt.getDirectivesFor(DEFAULT_USER_AGENT_NAME));
			record.getHostInfo().setRootDelay(record.getHostInfo().getRobotsDirectives().getCrawlDelay());
			
		    }
		}
		success = true;
	    }
	} catch (Exception ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	} finally {
	    
	    if (is != null) {
		try {
		    is.close();
		} catch (IOException ex) {
		    LOGGER.log(Level.SEVERE, null, ex);
		}
	    }
	    if (connection != null) {
		connection.disconnect();
	    }
	    return success;
	}
    }
    
    public static DownloaderOutputData getDownloadedPage() {
	return outputData;
    }
    
}
