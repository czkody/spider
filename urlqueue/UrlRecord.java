/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package urlqueue;

import java.net.URL;

/**
 * Record of URL with aditional informations for transmission within spider
 * @author Petr Křenek
 */
public class UrlRecord {

    private URL url;
    private int priority;
    private HostInfo hostInfo;

    public UrlRecord(URL url, int priority, HostInfo hostInfo) {
	this.url = url;
	this.priority = priority;
	this.hostInfo = hostInfo;
    }

    /**
     * @return the url
     */
    public URL getUrl() {
	return url;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
	return priority;
    }

    /**
     * @return the hostInfo
     */
    public HostInfo getHostInfo() {
	return hostInfo;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
	this.url = url;
    }
}
