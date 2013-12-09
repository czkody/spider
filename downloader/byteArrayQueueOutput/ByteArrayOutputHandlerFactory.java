/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.byteArrayQueueOutput;

import downloader.DownloaderOutputHandler;
import downloader.DownloaderOutputHandlerFactory;

/**
 *
 * @author Petr Křenek
 */
public class ByteArrayOutputHandlerFactory implements DownloaderOutputHandlerFactory {

    private ByteArrayQueueInput output;

    public ByteArrayOutputHandlerFactory(ByteArrayQueueInput output) {
	this.output = output;
    }

    @Override
    public DownloaderOutputHandler getHandler() {
	return new ByteArrayOutputHandler(output);
    }
}
