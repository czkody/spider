/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package urlqueue;

import java.net.URL;
import java.util.Date;

/**
 *
 * @author Petr Křenek
 */
public class VisitedPage {

    public enum ChangeFrequency {

	NONE(10), NEVER(0), ALWAYS(6), HOURLY(1), DAILY(2), WEEKLY(3), MONTHLY(4), YEARLY(5);
	private int changeFrequency;

	private ChangeFrequency(int changeFreq) {
	    this.changeFrequency = changeFreq;
	}

    }
    
    private int changeFrequency;
    private URL url;
    private Date lastAccess;

    public VisitedPage(URL url, String changeFraquency) {
	this.url = url;
	this.changeFrequency = ChangeFrequency.valueOf(changeFraquency.toUpperCase()).changeFrequency;
	this.lastAccess = new Date();
    }

    public synchronized void setLastAccess() {
	lastAccess = new Date();
    }
}
