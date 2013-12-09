/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import java.net.URL;

/**
 * Abstract class for output data from downloader
 *
 * @author Petr Křenek
 */
public abstract class DownloaderOutputData {

    private URL url;
    private String contentType;
    private byte[] content;

    protected DownloaderOutputData(String contentType, URL url) {
	this.contentType = contentType;
	this.url = url;
    }

    public void setContent(byte[] content) {
	this.content = content;
    }
    
    public byte[] getContent() {
	return content;
    }
    
    public String getContentType() {
	return contentType;
    }

    public URL getUrl() {
	return url;
    }
}
