/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Petr Křenek
 */
public abstract class XmlData {
    
    private static final Logger LOGGER = Logger.getLogger(XmlData.class.getName());
    /**
     * lastModified uses the W3C date format
     * (http://www.w3.org/TR/NOTE-datetime)
     */
    private static final ThreadLocal<DateFormat[]> dateFormats = new ThreadLocal<DateFormat[]>() {
	@Override
        protected DateFormat[] initialValue() {
            return new DateFormat[] { new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm+hh:00"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm-hh:00"),
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+hh:00"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-hh:00"), new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz") 
	    };
	}
    };

    /**
     * Convert the given date (given in an acceptable DateFormat), null if the
     * date is not in the correct format.
     *
     * @param date
     *            - the date to be parsed
     * @return the Date equivalent
     */
    public static Date convertToDate(String date) {
        if (date != null) {
            for (DateFormat df : dateFormats.get()) {
                try {
                    return df.parse(date);
                } catch (ParseException ex) {
		    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
        // Not successful parsing any dates
        return null;
    }

}
