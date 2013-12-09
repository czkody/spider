/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.html;

import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import parser.AbstractSaxParser;

/**
 *
 * @author Petr Křenek
 */
public class HtmlSaxParser extends AbstractSaxParser{
    
    private static final String XML_SITEMAPINDEX_ROOT_NAME = "SITEMAPINDEX";
    private static final String HTML_A_HREF_INDEX_ITEM = "A HREF";
    private static final String XML_SITEMAP_ROOT_NAME = "URLSET";
    private static final String XML_SITEMAP_ITEM = "URL";
    private static final String XML_ROOT_ATTR = "XMLNS";
    private static final String XML_SITEMAP_LOC = "LOC";
    /**
     * Map of tags and attributes
     */
    private String temp;
    private HtmlXmlData xmlData;
    
    /**
     *
     * @param saxParserFactory
     * @param is 
     */
    public HtmlSaxParser(SAXParserFactory saxParserFactory) {
	super(saxParserFactory);
//	SitemapIndexSaxParser.registerParser(this, SitemapIndexXmlData.class);
   }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	if (qName.equalsIgnoreCase(HTML_A_HREF_INDEX_ITEM)) {
	    xmlData = new HtmlXmlData();
	}
	if (qName.equalsIgnoreCase(XML_SITEMAP_ITEM)) {
	    xmlData = new HtmlXmlData();
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
	    System.out.println("HTML");
	    return true;
	} else {
	    return false;
	}
    }

}    
