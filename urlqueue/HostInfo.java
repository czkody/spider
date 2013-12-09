/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package urlqueue;

import downloader.PageFetcher;
import downloader.robotsTxt.RobotsDirectives;
import downloader.robotsTxt.RobotsTxt;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for storing information about hosts visited by downloader
 *
 * @author Petr Křenek
 */
public class HostInfo {

    private static final String FILENAME_ROBOTS_TXT = "/robots.txt";
    private static final String URL_PROTOCOL_DELIMITER = "://";
    private static final String DEFAULT_FREQUENCY_CHANGE = "Always";
    private static final ConcurrentHashMap<String, HostInfo> instances = new ConcurrentHashMap<>();
    /**
     * Cleanup interval in milliseconds
     */
    private static final int CLEANUP_INTERVAL = 300000;
    private static final int CLEANUP_TIMEOUT = 3600000;
    private static Thread autoCleanUpThread;
    private String root;
    private float delay;
    private List<VisitedPage> visitedPageList;
    private Date lastAccess;
    private RobotsDirectives robotsDirectives;
    private boolean firstVisit = true;

    /**
     * @return the robotsDirectives
     */
    public RobotsDirectives getRobotsDirectives() {
	return robotsDirectives;
    }

    /**
     * @param robotsDirectives the robotsDirectives to set
     */
    public void setRobotsDirectives(RobotsDirectives robotsDirectives) {
	this.robotsDirectives = robotsDirectives;
    }

    private static class AutoCleanUpThread extends Thread {

	public static final Object lock = new Object();
	private static boolean stop = false;

	@Override
	public void run() {
	    while (!getStop()) {
		for (HostInfo hi : instances.values()) {
		    if ((new Date()).getTime() - hi.lastAccess.getTime() > CLEANUP_TIMEOUT) {
			HostInfo remove = instances.remove(hi);
		    }
		}
		try {
		    synchronized (lock) {
			lock.wait(CLEANUP_INTERVAL);
		    }
		} catch (InterruptedException ex) {
		    // ignore
		}
	    }
	}

	private synchronized boolean getStop() {
	    return stop;
	}
    }

    private HostInfo(String root, int delay) {
	this.root = root;
	this.delay = delay;
	this.visitedPageList = new ArrayList<>();
	this.robotsDirectives = null;
    }

    static {
	autoCleanup();
    }

    public static HostInfo getInstance(URL url) {
	String root = url.getHost();
	if (instances.containsKey(root)) {
	    return instances.get(root);
	} else {
	    HostInfo hi = addInstance(root, 0);
	    try {
		URL robotsTxtUrl = new URL(url.getProtocol().concat(URL_PROTOCOL_DELIMITER).concat(url.getHost()).concat(FILENAME_ROBOTS_TXT));
		//MAX_PRIORITY
		UrlRecord record = new UrlRecord(robotsTxtUrl, 10, hi);
		if (!PageFetcher.downloadPage(record, null, null)) {
		    hi.setRobotsDirectives(RobotsTxt.NO_DIRECTIVES);
		}
	    } catch (MalformedURLException ex) {
		Logger.getLogger(HostInfo.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    return hi;
	}
    }

    public static HostInfo addInstance(String root, int delay) {
	if (!instances.containsKey(root)) {
	    HostInfo hi = new HostInfo(root, delay);
	    hi.setLastAccess();
	    instances.put(root, hi);
	}
	return instances.get(root);
    }
    
    public synchronized void setRootDelay (float delay) {
	this.delay = delay;
    }

    public synchronized boolean canAccess() {
	return (new Date()).getTime() - lastAccess.getTime() >= delay;
    }

    public synchronized void setLastAccess() {
	lastAccess = new Date();
    }

    public synchronized void addVisitedUrl(URL url) {
	visitedPageList.add(new VisitedPage(url, DEFAULT_FREQUENCY_CHANGE));
    }

    public synchronized boolean isFirstVisit() {
	return firstVisit;
    }
    
    public synchronized void setFirstVisit(boolean firtsVisit) {
	this.firstVisit = firtsVisit;
    }

    private static void autoCleanup() {
	autoCleanUpThread = new AutoCleanUpThread();
	autoCleanUpThread.start();
    }

    public static void stopAutoCleanup() {
	AutoCleanUpThread.stop = true;
	if (autoCleanUpThread.getState() == Thread.State.TIMED_WAITING) {
	    synchronized (AutoCleanUpThread.lock) {
		AutoCleanUpThread.lock.notify();
	    }
	}
	try {
	    autoCleanUpThread.join();
	} catch (InterruptedException ex) {
	    Logger.getLogger(HostInfo.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
