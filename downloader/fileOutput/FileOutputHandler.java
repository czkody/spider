/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.fileOutput;

import downloader.DownloaderOutputData;
import downloader.DownloaderOutputHandler;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handler for output from downloader to file
 *
 * @author Petr Křenek
 */
public class FileOutputHandler extends DownloaderOutputHandler<DownloaderOutputData> {

    private static final Logger LOGGER = Logger.getLogger(DownloaderOutputHandler.class.getName());
    private static final String ERROR_NO_URL = "URL can not be null";
    private FileOutputStream output;
    private FileNameProvider fileNameProvider;

    protected FileOutputHandler(FileNameProvider fileNameProvider) {
	this.fileNameProvider = fileNameProvider;
    }

    @Override
    public void init(URL url) {
	if (url == null) {
	    throw new IllegalArgumentException(ERROR_NO_URL);
	}
	try {
	    output = new FileOutputStream(fileNameProvider.getFileName(url));
	} catch (FileNotFoundException ex) {
	    throw new RuntimeException(ex);
	}
    }

/*    @Override
    public void write(byte[] buffer, int offset, int size) {
	try {
	    output.write(buffer, offset, size);
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }
*/
    @Override
    public void finish() {
	try {
	    output.close();
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @Override
    public void processData(DownloaderOutputData data) {
	try {
	    output.write(data.getContent());
	} catch (IOException ex) {
	    LOGGER.log(Level.SEVERE, "chyba");
	}
    }
}
