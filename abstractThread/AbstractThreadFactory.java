/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstractThread;

/**
 *
 * @author Petr Křenek
 */
public interface AbstractThreadFactory<A extends ThreadAgent, T extends AbstractThread> {

    public T getThread(A threadAgent);
}
