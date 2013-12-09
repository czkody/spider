/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstractThread;

/**
 * Interface for status of threads
 *
 * @author Petr Křenek
 */
public interface ThreadStatus {

    public void running(String name);

    public void finished(String name);
}
