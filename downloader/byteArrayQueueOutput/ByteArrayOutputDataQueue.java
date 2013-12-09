/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.byteArrayQueueOutput;

import abstractThread.QueueNotifyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Petr Křenek
 */
public class ByteArrayOutputDataQueue {

    private static final int DEFAULT_QUEUE_SIZE = 1000;
    private static final Logger LOGGER = Logger.getLogger(ByteArrayOutputDataQueue.class.getName());
    private static ByteArrayOutputDataQueue instance = null;
    private final BlockingQueue<ByteArrayOutputData> dataQueue;
    private List<QueueNotifyListener> listeners = new ArrayList<>();

    private ByteArrayOutputDataQueue(int queueSize) {
	if (queueSize < 1) {
	    queueSize = DEFAULT_QUEUE_SIZE;
	}
	dataQueue = new ArrayBlockingQueue(queueSize);
    }

    public static ByteArrayOutputDataQueue getInstance(int queueSize) {
	if (instance == null) {
	    return new ByteArrayOutputDataQueue(queueSize);
	}
	return instance;
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

    public synchronized void putByteArrayOutputData(ByteArrayOutputData data) {
	try {
	    dataQueue.put(data);
	    if (dataQueue.size() == 1) {
		notifyListeners();
	    }
	} catch (InterruptedException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
    }

    public void putByteArrayOutputDataList(List<ByteArrayOutputData> data) {
	try {
	    for (ByteArrayOutputData sod : data) {
		synchronized (dataQueue) {
		    dataQueue.put(sod);
		}
	    }
	} catch (InterruptedException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
    }

    public synchronized ByteArrayOutputData takeByteArrayOutputData() {
	ByteArrayOutputData result = null;
	try {
	    result = dataQueue.take();
	} catch (InterruptedException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	return result;
    }

    public List<ByteArrayOutputData> takeByteArrayOutputDataList(int count) {
	List<ByteArrayOutputData> result = new ArrayList<>();
	try {
	    for (int i = 0; i < count; i++) {
		synchronized (dataQueue) {
		    result.add(dataQueue.take());
		}
	    }
	} catch (InterruptedException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	return result;
    }
    
    public synchronized Boolean isEmpty() {
	return dataQueue.isEmpty();
    }

    int size() {
	return dataQueue.size();
    }
}
