/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.fileOutput;

import java.net.URL;

/**
 * Interface for file name providers
 * @author Petr Křenek
 */
public interface FileNameProvider {

    public String getFileName(URL url);
}
