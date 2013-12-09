/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package urlqueue;

import downloader.DownloaderUrlSource;
import java.util.List;

/**
 * Implementation of queue UrlRecord source for downloader
 *
 * @author Petr Křenek
 */
public class QueueUrlRecordDownloaderSource implements DownloaderUrlSource {

    private UrlQueue urlQueue;
    private List<UrlRecord> urlRecordList;

    public QueueUrlRecordDownloaderSource(UrlQueue queue) {
	if (queue == null) {
	    throw new IllegalArgumentException("queue");
	}
	this.urlQueue = queue;
    }

/*    public synchronized void addUrl(UrlRecord record) {
    }
*/
    @Override
    public synchronized UrlRecord getUrl() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized UrlRecord[] getUrlArray(int count) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized List<UrlRecord> getUrlList(int urlCount) {
	return urlQueue.getUrlRecordList(urlCount);
    }

    @Override
    public synchronized boolean isEmpty() {
	return urlQueue.isEmpty();
    }
}
