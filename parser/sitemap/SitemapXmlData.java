/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.sitemap;

import parser.XmlData;

/**
 *
 * @author Petr Křenek
 */
public class SitemapXmlData extends XmlData{
    
    private String loc;
    
    public SitemapXmlData() {
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
