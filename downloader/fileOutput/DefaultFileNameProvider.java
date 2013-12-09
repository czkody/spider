/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.fileOutput;

import java.io.File;
import java.net.URL;

/**
 * Implementation of file name provider
 *
 * @author Petr Křenek
 */
public class DefaultFileNameProvider implements FileNameProvider {

    private static final String ERROR_ROOT_NULL = "Root can not be null";
    private static final String ERROR_ROOT_NOT_EXIST = "Root doesn't exist";
    private static final String FILE_EXTENSION_HTML = ".html";
    private static final String FILE_EXTENSION_XML = ".xml";
    private static final String FILE_EXTENSION_TXT = ".txt";
    private File root;

    public DefaultFileNameProvider(File root) {
	if (root == null) {
	    throw new IllegalArgumentException(ERROR_ROOT_NULL);
	}
	if (!root.isDirectory() || !root.exists()) {
	    throw new IllegalArgumentException(ERROR_ROOT_NOT_EXIST);
	}
	this.root = root;
    }

    @Override
    public String getFileName(URL url) {
	if (url.getFile().contains(FILE_EXTENSION_XML)) {
	    return String.format("%s/%s", root.getPath(), (url.getHost().concat(url.getFile())).replace("/", "--").replace(".", "_").concat(FILE_EXTENSION_XML));
	} else if (url.getFile().contains(FILE_EXTENSION_TXT)) {
	    return String.format("%s/%s", root.getPath(), (url.getHost().concat(url.getFile())).replace("/", "--").replace(".", "_").concat(FILE_EXTENSION_TXT));
	} else {
	    return String.format("%s/%s", root.getPath(), (url.getHost().concat(url.getFile())).replace("/", "--").replace(".", "_").concat(FILE_EXTENSION_HTML));
	}
    }
}
