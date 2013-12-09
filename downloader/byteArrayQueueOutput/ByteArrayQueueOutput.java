/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.byteArrayQueueOutput;

import downloader.DownloaderOutputData;
import java.util.List;
import parser.ParserDataInput;

/**
 *
 * @author Petr Křenek
 */
public class ByteArrayQueueOutput implements ParserDataInput<DownloaderOutputData> {
    private ByteArrayOutputDataQueue queue;

    public ByteArrayQueueOutput(ByteArrayOutputDataQueue queue) {
	this.queue = queue;
    }
	    
    @Override
    public DownloaderOutputData takeData() {
	return (DownloaderOutputData)queue.takeByteArrayOutputData();
    }

    @Override
    public List<DownloaderOutputData> takeDataList(int count) {
	return (List<DownloaderOutputData>)(List<?>)queue.takeByteArrayOutputDataList(count);
    }

    @Override
    public Boolean isEmpty() {
	return queue.isEmpty();
    }

    @Override
    public int size() {
	return queue.size();
    }

}
