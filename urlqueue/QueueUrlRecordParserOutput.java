/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package urlqueue;

import java.util.List;
import parser.ParserUrlRecordOutput;

/**
 * Implementation of queue UrlRecord output from parser to UrlQueue
 *
 * @author Petr Křenek
 */
public class QueueUrlRecordParserOutput implements ParserUrlRecordOutput {

    private UrlQueue urlQueue;

    public QueueUrlRecordParserOutput(UrlQueue queue) {
	this.urlQueue = queue;
    }

    @Override
    public synchronized void putUrlRecord(UrlRecord record) {
	urlQueue.addUrlRecord(record);
    }

    @Override
    public synchronized void putUrlRecordList(List<UrlRecord> recordList) {
	urlQueue.addUrlRecordList(recordList);
    }
}
