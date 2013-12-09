/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 * Abstract class for data output from parser
 *
 * @author Petr Křenek
 */
public abstract class ParserOutputData {
    
    private String contentType;
    protected byte[] content;

    protected ParserOutputData(String contentType) {
	this.contentType = contentType;
    }

    protected void setContent(byte[] content) {
	this.content = content;
    }
    
    public byte[] getContent() {
	return content;
    }
    
    public String getContentType() {
	return contentType;
    }
}
