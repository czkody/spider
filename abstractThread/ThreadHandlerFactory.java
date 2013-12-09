/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstractThread;

/**
 *Interface for thread handlers factories
 * @author Petr Křenek
 */
public interface ThreadHandlerFactory <T extends ThreadHandler> {
    
    public T getHandler();
	
}
