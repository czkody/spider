/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import java.net.URL;

/**
 *
 * @author Petr Křenek
 */
public interface DownloaderOutputDataFactory<T extends DownloaderOutputData> {

    public T getOutputData(String connectionType, URL url);
}
