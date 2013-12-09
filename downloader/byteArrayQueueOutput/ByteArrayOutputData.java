/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.byteArrayQueueOutput;

import downloader.DownloaderOutputData;
import java.net.URL;

/**
 *
 * @author Petr Křenek
 */
public class ByteArrayOutputData extends DownloaderOutputData {

    public ByteArrayOutputData(String contentType, URL url) {
	super(contentType, url);
    }
}
