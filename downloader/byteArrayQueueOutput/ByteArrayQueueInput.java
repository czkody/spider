/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.byteArrayQueueOutput;

import downloader.DownloaderByteArrayOutput;
import downloader.DownloaderOutputData;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for byte array output from downloader to queue
 *
 * @author Petr Křenek
 */
public class ByteArrayQueueInput implements DownloaderByteArrayOutput {

    private static final Logger LOGGER = Logger.getLogger(ByteArrayQueueInput.class.getName());
    private static final String ERROR_NULL_QUEUE = "StringOutputDataQueue cannot be null";
    private ByteArrayOutputDataQueue byteArrayOutputDataQueue;

    public ByteArrayQueueInput(ByteArrayOutputDataQueue byteArrayOutputDataQueue) {
	if (byteArrayOutputDataQueue == null) {
	    LOGGER.log(Level.SEVERE, ERROR_NULL_QUEUE);
	}
	this.byteArrayOutputDataQueue = byteArrayOutputDataQueue;
    }

    @Override
    public void putByteArrayOutputData(DownloaderOutputData data) {
	byteArrayOutputDataQueue.putByteArrayOutputData((ByteArrayOutputData) data);
    }
}
