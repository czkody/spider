/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.sitemap;

import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import parser.AbstractSaxParser;

/**
 *
 * @author Petr Křenek
 */
public class SitemapSaxParser extends AbstractSaxParser {
    
    private static final String XML_SITEMAPINDEX_ROOT_NAME = "SITEMAPINDEX";
    private static final String XML_SITEMAPINDEX_ITEM = "SITEMAP";
    private static final String XML_SITEMAP_ROOT_NAME = "URLSET";
    private static final String XML_SITEMAP_ITEM = "URL";
    private static final String XML_ROOT_ATTR = "XMLNS";
    private static final String XML_SITEMAP_LOC = "LOC";
    /**
     * Map of tags and attributes
     */
    private String temp;
    private SitemapXmlData xmlData;
    
    /**
     *
     * @param saxParserFactory
     * @param is 
     */
    public SitemapSaxParser(SAXParserFactory saxParserFactory) {
	super(saxParserFactory);
//	SitemapIndexSaxParser.registerParser(this, SitemapIndexXmlData.class);
   }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	if (qName.equalsIgnoreCase(XML_SITEMAPINDEX_ITEM)) {
	    xmlData = new SitemapXmlData();
	} else {
	    if (qName.equalsIgnoreCase(XML_SITEMAP_ITEM)) {
		xmlData = new SitemapXmlData();
	    }
	}
    }

    @Override
    public void characters(char[] buffer, int start, int length) {
	temp = new String(buffer, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
	if (qName.equalsIgnoreCase(XML_SITEMAP_LOC)) {
	    xmlData.setLoc(temp);
	    parsedDataList.add(xmlData);
	}
    }

    @Override
    public boolean canParse(String data) {
	if (data.contains(XML_SITEMAPINDEX_ROOT_NAME) || data.contains(XML_SITEMAP_ROOT_NAME)) {
	    System.out.println("SITEMAP");
	    return true;
	} else {
	    return false;
	}
    }

}