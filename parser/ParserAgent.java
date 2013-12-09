/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import abstractThread.AbstractThreadFactory;
import abstractThread.QueueNotifyListener;
import abstractThread.ThreadAgent;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParserFactory;

/**
 * Parser threads agent
 *
 * @author Petr Křenek
 */
public class ParserAgent extends ThreadAgent implements QueueNotifyListener {

    private static final Logger LOGGER = Logger.getLogger(ParserAgent.class.getName());

    public ParserAgent(AbstractThreadFactory threadFactory) {
	super(threadFactory);
	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    }

    @Override
    public synchronized void notifyNonEmpty() {
	this.notifyAll();
    }
}
