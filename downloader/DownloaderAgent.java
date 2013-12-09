/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import abstractThread.AbstractThreadFactory;
import abstractThread.QueueNotifyListener;
import abstractThread.ThreadAgent;

/**
 * Downloader threads agent
 *
 * @author Petr Křenek
 */
public class DownloaderAgent extends ThreadAgent implements QueueNotifyListener {

    public DownloaderAgent(AbstractThreadFactory threadFactory) {
	super(threadFactory);
    }

    @Override
    public synchronized void notifyNonEmpty() {
	this.notifyAll();
    }
}
