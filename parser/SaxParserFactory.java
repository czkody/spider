/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import abstractThread.ContentType;
import downloader.DownloaderOutputData;
import javax.xml.parsers.SAXParserFactory;
import parser.sitemap.SitemapSaxParser;

/**
 *
 * @author Petr Křenek
 */
public class SaxParserFactory {
    
    private SAXParserFactory factory;
//    private List<AbstractSaxParser> xmlSaxParsersList;
//    private List<AbstractSaxParser> htmlSaxParsersList;
    private SitemapSaxParser sitemapParser;
    
    public SaxParserFactory(SAXParserFactory factory) {
	this.factory = factory;
	this.sitemapParser = new SitemapSaxParser(factory);
    }

    public AbstractSaxParser getSaxParser(DownloaderOutputData record) {
//	String data = new String(record.getContent(), 0, 100).toUpperCase();
	if (record.getContentType().contains(ContentType.APP_XML) || record.getContentType().contains(ContentType.TEXT_XML)) {
	    /**
	     * Parse XML input data
	     */
	    System.out.println("XML");
//	    if (sitemapParser.canParse(data)) {
//		System.out.println(data);
		return sitemapParser;
//	    }
//	    if (record.getUrl().getFile().contains(SITEMAP_XML_NAME)) {
//		AbstractSaxParser saxParser = new SitemapIndexSaxParser(saxParserFactory, );
//	    }
	} else if (record.getContentType().contains(ContentType.HTML)) {
	    /**
	     * Parse HTML input data
	     */
	    System.out.println("HTML");
//	    AbstractSaxParser saxParser = new SitemapSaxParser(saxParserFactory, byteArrayInputStream, htmlTags);
	}
	return null;
    }
    
}
