/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import abstractThread.ThreadHandler;
import java.net.URL;

/**
 * Abstract class for output handler of downloader
 * @author Petr Křenek
 */
public abstract class DownloaderOutputHandler<T extends DownloaderOutputData> extends ThreadHandler{
    public abstract void init(URL url);
//    public abstract void write(byte[] buffer, int offset, int size);
    public abstract void finish();
    public abstract void processData(T data);
}
