/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import abstractThread.ThreadHandlerFactory;

/**
 * Interface for output handlers factory
 *
 * @author Petr Křenek
 */
public interface DownloaderOutputHandlerFactory extends ThreadHandlerFactory<DownloaderOutputHandler> {

    @Override
    public abstract DownloaderOutputHandler getHandler();
}
