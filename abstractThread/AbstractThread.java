/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstractThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class for thread
 *
 * @author Petr Křenek
 */
public abstract class AbstractThread<T extends ThreadAgent> extends Thread {

    private static final Logger LOGGER = Logger.getLogger(AbstractThread.class.getName());
    private static final String THREAD_STARTED = " started";
    private static final String THREAD_FINISHED = " finished";
    private static final String THREAD_ERROR_MESSAGE = "Thread error";
    private List<ThreadStatus> threadStatusList;
    protected T threadAgent;
    private static final Map<String, Integer> threadCounts = new HashMap<>();
    private final String className = this.getClass().getSimpleName();

    protected AbstractThread(T threadAgent) {
	super();
	if (!threadCounts.containsKey(className)) {
	    threadCounts.put(className, 1);
	} else {
	    threadCounts.put(className, threadCounts.get(className)+1);
	}
	this.setName(className.concat("_").concat(threadCounts.get(className).toString()));
	threadStatusList = new ArrayList<>();
	this.threadAgent = threadAgent;
    }

    @Override
    public final void run() {
	System.out.println(this.getName().concat(THREAD_STARTED));
	threadRunning();
	try {
	    work();
	} catch (Exception ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	System.out.println(this.getName().concat(THREAD_FINISHED));
	threadFinished();
    }

    public abstract void work();

    private void threadRunning() {
	for (ThreadStatus status : threadStatusList) {
	    status.running(this.getName());
	}
    }

    private void threadFinished() {
	for (ThreadStatus status : threadStatusList) {
	    status.finished(this.getName());
	}
    }

    /**
     * Adds thread status listener
     *
     * @param threadStatus
     */
    public void addThreadListener(ThreadStatus threadStatus) {
	threadStatusList.add(threadStatus);
    }
}
