/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import abstractThread.ThreadHandlerFactory;

/**
 * Interface for output handlers factory
 *
 * @author Petr Křenek
 */
public interface ParserOutputHandlerFactory extends ThreadHandlerFactory<ParserOutputHandler> {
    
    @Override
    public abstract ParserOutputHandler getHandler();

}
