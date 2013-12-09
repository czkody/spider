/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.List;
import urlqueue.UrlRecord;

/**
 *
 * @author Petr Křenek
 */
public interface ParserUrlRecordOutput {
    
    public void putUrlRecord(UrlRecord record);
    
    public void putUrlRecordList(List<UrlRecord> recordList);
    
}
