/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import downloader.DownloaderOutputData;
import java.util.List;

/**
 *
 * @author Petr Křenek
 */
public interface ParserDataInput<T extends DownloaderOutputData> {

    public T takeData();

    public List<T> takeDataList(int count);

    public Boolean isEmpty();

    public int size();
}
