/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.fileOutput;

import downloader.DownloaderOutputHandlerFactory;

/**
 * Factory for output handlers from downloader to file
 *
 * @author Petr Křenek
 */
public class FileOutputHandlerFactory implements DownloaderOutputHandlerFactory {

    private FileNameProvider fileNameProvider;

    public FileOutputHandlerFactory(FileNameProvider fileNameProvider) {
	this.fileNameProvider = fileNameProvider;
    }

    @Override
    public FileOutputHandler getHandler() {
	return new FileOutputHandler(fileNameProvider);
    }
}
