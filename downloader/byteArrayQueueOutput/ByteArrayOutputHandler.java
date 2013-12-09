/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.byteArrayQueueOutput;

import downloader.DownloaderOutputData;
import downloader.DownloaderOutputHandler;
import java.net.URL;

/**
 * Class for String output handler 
 *
 * @author Petr Křenek
 */
public class ByteArrayOutputHandler extends DownloaderOutputHandler<DownloaderOutputData> {
    
    private ByteArrayQueueInput output;
    
    public ByteArrayOutputHandler(ByteArrayQueueInput output) {
	this.output = output;
    }

    @Override
    public void init(URL url) {
	//nothing;
    }

/*    @Override
    public void write(byte[] buffer, int offset, int size) {
	//nothing;
    }
*/
    @Override
    public void finish() {
	//nothing;
    }

    @Override
    public void processData(DownloaderOutputData data) {
	output.putByteArrayOutputData(data);
    }
    
}
