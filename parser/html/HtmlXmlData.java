/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.html;

import parser.XmlData;

/**
 *
 * @author krena
 */
public class HtmlXmlData extends XmlData{
    
    private String loc;
    
    public HtmlXmlData() {
    }

    /**
     * @return the loc
     */
    public String getLoc() {
	return loc;
    }

    /**
     * @param loc the loc to set
     */
    public void setLoc(String loc) {
	this.loc = loc;
    }
}
