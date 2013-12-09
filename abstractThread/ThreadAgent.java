/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstractThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Agent to control the threads
 *
 * @author Petr Křenek
 */
public class ThreadAgent {

    private static final int DEFAULT_THREAD_COUNT = 2;
    private AbstractThreadFactory threadFactory;
    private List<AbstractThread> threadList;
    private int runningThreadCount;
    private int threadCount = DEFAULT_THREAD_COUNT;
    private ThreadStatus statusListener = new ThreadStatus() {
	@Override
	public synchronized void running(String name) {
	    ++runningThreadCount;
	}

	@Override
	public synchronized void finished(String name) {
	    --runningThreadCount;
	}
    };

    public ThreadAgent(AbstractThreadFactory threadFactory) {
	if (threadFactory == null) {
	    throw new IllegalArgumentException("threadFactory");
	}
	this.threadFactory = threadFactory;
	this.threadList = new ArrayList<>(DEFAULT_THREAD_COUNT);
    }

    public synchronized void startThreads() {
	AbstractThread thread;
	for (int i = 1; i <= getThreadCount(); i++) {
	    thread = threadFactory.getThread(this);
	    threadList.add(thread);
	    thread.addThreadListener(statusListener);
	    thread.start();
	}
    }
    
    public synchronized void joinAll() throws InterruptedException {
	for (AbstractThread thread : threadList) {
	    thread.join();
	}
    }

    /**
     * @return the runningThreadCount
     */
    public synchronized int getRunningThreadCount() {
	return runningThreadCount;
    }

    /**
     * @param runningThreadCount the runningThreadCount to set
     */
    public synchronized void setRunningThreadCount(int runningThreadCount) {
	this.runningThreadCount = runningThreadCount;
    }

    /**
     * @return the threadCount
     */
    public int getThreadCount() {
	return threadCount;
    }

    /**
     * @param threadCount the threadCount to set
     */
    public void setThreadCount(int threadCount) {
	this.threadCount = threadCount;
    }
}
