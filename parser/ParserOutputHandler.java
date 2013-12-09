/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import abstractThread.ThreadHandler;

/**
 *
 * @author Petr Křenek
 */
public abstract class ParserOutputHandler<T extends ParserOutputData> extends ThreadHandler {

    public abstract void processData(T data);
}
