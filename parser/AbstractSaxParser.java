/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import downloader.DownloaderOutputData;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import parser.sitemap.SitemapSaxParser;

/**
 *
 * @author Petr Křenek
 */
public abstract class AbstractSaxParser extends DefaultHandler{

    protected static final Logger LOGGER = Logger.getLogger(SitemapSaxParser.class.getName());
//    private static Map<AbstractSaxParser, Class> registeredParsers = new HashMap<>();
    private SAXParser saxParser;
    protected List<XmlData> parsedDataList;

    public AbstractSaxParser(SAXParserFactory saxParserFactory) {
	if (saxParserFactory == null) {
	    throw new IllegalArgumentException("saxParserFactory");
	}
	try {
	    this.saxParser = saxParserFactory.newSAXParser();
	} catch (ParserConfigurationException | SAXException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	this.parsedDataList = new ArrayList<>();
    }
    
/*    public static synchronized void registerParser(AbstractSaxParser saxParser, Class outputData) {
	if (!registeredParsers.containsKey(saxParser)) {
	    registeredParsers.put(saxParser, outputData);
	}
    }
*/
    
    public List<XmlData> parseDocument(ByteArrayInputStream inputStream) {
	//parsing test
	try {
	    saxParser.parse(inputStream, this);
	} catch (SAXException | IOException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	return parsedDataList;
    }
    
    protected abstract boolean canParse(String data);
    
}