/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader;

import java.util.List;
import urlqueue.UrlRecord;

/**
 * Interface for URL source for downloader
 *
 * @author Petr Křenek
 */
public interface DownloaderUrlSource {

    public UrlRecord getUrl();

    public UrlRecord[] getUrlArray(int count);

    public List<UrlRecord> getUrlList(int count);

    public boolean isEmpty();
}
