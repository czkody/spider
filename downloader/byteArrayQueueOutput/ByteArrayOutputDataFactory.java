/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.byteArrayQueueOutput;

import downloader.DownloaderOutputDataFactory;
import java.net.URL;

/**
 * Factory class for production of ByteArrayOutputData
 *
 * @author Petr Křenek
 */
public class ByteArrayOutputDataFactory implements DownloaderOutputDataFactory<ByteArrayOutputData> {
    
    public ByteArrayOutputDataFactory() {
    }

    @Override
    public ByteArrayOutputData getOutputData(String contentType, URL url) {
	return new ByteArrayOutputData(contentType, url);
    }
    
}
