/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author Petr Křenek
 */
public interface ParserOutputDataFactory<T extends ParserOutputData> {

    public T getOutputData(String connectionType);
}
