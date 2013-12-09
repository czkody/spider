/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

/**
 *
 * @author Petr Křenek
 */
public interface DownloaderByteArrayOutput<T extends DownloaderOutputData> {

    public void putByteArrayOutputData(T data);
}
