/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import abstractThread.AbstractThreadFactory;
import javax.xml.parsers.SAXParserFactory;

/**
 * Class for factory for parser threads
 *
 * @author Petr Křenek
 */
public class ParserThreadFactory implements AbstractThreadFactory<ParserAgent, ParserThread> {

    private ParserOutputHandlerFactory outputHandlerFactory;
    private ParserOutputDataFactory outputDataFactory;
    private ParserDataInput dataInput;
    private SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    private ParserUrlRecordOutput parserUrlRecordOutput;

    public ParserThreadFactory(ParserOutputHandlerFactory outputHandlerFactory, ParserOutputDataFactory outputDataFactory, ParserDataInput dataInput, ParserUrlRecordOutput parserUrlRecordOutput) {
//    public DownloaderThreadFactory(DownloaderOutputHandlerFactory outputHandlerFactory, DownloaderUrlSource urlSource) {
	this.outputHandlerFactory = outputHandlerFactory;
	this.outputDataFactory = outputDataFactory;
	this.dataInput = dataInput;
	this.parserUrlRecordOutput = parserUrlRecordOutput;
    }

    @Override
    public ParserThread getThread(ParserAgent threadAgent) {
	return new ParserThread(threadAgent, outputHandlerFactory, outputDataFactory, dataInput, saxParserFactory, parserUrlRecordOutput);
    }
}
