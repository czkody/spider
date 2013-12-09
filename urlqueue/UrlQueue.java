/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package urlqueue;

import abstractThread.QueueNotifyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Downloader URLQueue
 *
 * @author Petr Křenek
 */
public class UrlQueue {

    private static final int MAX_QUEUE_SIZE = 1500;
    private static final String QUEUE_SIZE_TOO_BIG = "Requested queue size is too big, size will be set to default maximal size";
    private static final Logger LOGGER = Logger.getLogger(UrlQueue.class.getName());
    private List<UrlRecord> urlRecordList;
    private List<QueueNotifyListener> listeners = new ArrayList<>();

    public UrlQueue() {
	this(MAX_QUEUE_SIZE);
    }
    
    public UrlQueue(int queueSize) {
	if (queueSize > MAX_QUEUE_SIZE) {
	    LOGGER.log(Level.INFO, QUEUE_SIZE_TOO_BIG);
	    queueSize = MAX_QUEUE_SIZE;
	}
	urlRecordList = new ArrayList<>(queueSize);
    }
    
    public void addNotifyListener(QueueNotifyListener listener) {
	listeners.add(listener);
    }
    
    public void removeNotifyListener(QueueNotifyListener listener) {
	listeners.remove(listener);
    }
    
    private void notifyListeners() {
	for(QueueNotifyListener listener : listeners) {
	    listener.notifyNonEmpty();
	}
    }
    
    public synchronized void addUrlRecord(UrlRecord record) {
	if (record == null) {
	    throw new IllegalArgumentException("urlRecord");
	}
	urlRecordList.add(record);
	if (urlRecordList.size() == 1) {
	    notifyListeners();
	}
    }

    public synchronized void addUrlRecordList(List<UrlRecord> records) {
	if (records == null) {
	    throw new IllegalArgumentException("urlRecordList");
	}
	urlRecordList.addAll(records);
	if (urlRecordList.size() == records.size()) {
	    notifyListeners();
	}
    }

    public synchronized List<UrlRecord> getUrlRecordList(int urlCount) {
	List<UrlRecord> result = new ArrayList<>();
	if (urlCount > urlRecordList.size()) {
	    result.addAll(urlRecordList.subList(0, urlRecordList.size()));
	    /*	    for (int i = urlRecordList.size(); i < urlCount; i++) {
	     result.add(new UrlRecord(null, 0, null));
	     }
	     */ urlRecordList.subList(0, urlRecordList.size()).clear();
	} else {
	    result.addAll(urlRecordList.subList(0, urlCount));
	    urlRecordList.subList(0, urlCount).clear();
	}
	return result;
    }
    
    public synchronized boolean isEmpty() {
	return urlRecordList.isEmpty();
    }
}
