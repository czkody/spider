/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import abstractThread.AbstractThreadFactory;

/**
 * Class for factory for downloader threads
 *
 * @author Petr Křenek
 */
public class DownloaderThreadFactory implements AbstractThreadFactory<DownloaderAgent, DownloaderThread> {

    private DownloaderOutputHandlerFactory outputHandlerFactory;
    private DownloaderOutputDataFactory outputDataFactory;
    private DownloaderUrlSource urlSource;

    public DownloaderThreadFactory(DownloaderOutputHandlerFactory outputHandlerFactory, DownloaderOutputDataFactory outputDataFactory, DownloaderUrlSource urlSource) {
//    public DownloaderThreadFactory(DownloaderOutputHandlerFactory outputHandlerFactory, DownloaderUrlSource urlSource) {
	this.outputHandlerFactory = (DownloaderOutputHandlerFactory) outputHandlerFactory;
	this.outputDataFactory = outputDataFactory;
	this.urlSource = urlSource;
    }

    @Override
    public DownloaderThread getThread(DownloaderAgent threadAgent) {
	return new DownloaderThread(threadAgent, outputHandlerFactory, outputDataFactory, urlSource);
//	return new DownloaderThread(outputHandlerFactory, urlSource);
    }
}
