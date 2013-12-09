/*
 *  This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 *  Licensed to the Internet Archive (IA) by one or more individual 
 *  contributors. 
 *
 *  The IA licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package downloader.robotsTxt;

import java.io.Serializable;
import java.net.URL;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Represents the directives that apply to a user-agent (or set of user-agents)
 */
public class RobotsDirectives implements Serializable {

    private static final long serialVersionUID = 5386542759286155383L;
    private URL sitemapUrl = null;
    ConcurrentSkipListSet<String> disallows = new ConcurrentSkipListSet<>();
    ConcurrentSkipListSet<String> allows = new ConcurrentSkipListSet<>();
    float crawlDelay = -1;

    public synchronized boolean allows(String path) {
	return !(longestPrefixLength(disallows, path) > longestPrefixLength(allows, path));
    }
    
    /**
     * @param prefixSet
     * @param str
     * @return length of longest entry in {@code prefixSet} that prefixes
     * {@code str}, or zero if no entry prefixes {@code str}
     */
    protected int longestPrefixLength(ConcurrentSkipListSet<String> prefixSet,
	    String str) {
	String possiblePrefix = prefixSet.floor(str);
	if (possiblePrefix != null && str.startsWith(possiblePrefix)) {
	    return possiblePrefix.length();
	} else {
	    return 0;
	}
    }

    public void addDisallow(String path) {
	if (path.length() == 0) {
	    // ignore empty-string disallows 
	    // (they really mean allow, when alone)
	    return;
	}
	disallows.add(path);
    }

    public void addAllow(String path) {
	allows.add(path);
    }

    public void setCrawlDelay(float i) {
	crawlDelay = i;
    }

    public float getCrawlDelay() {
	return crawlDelay;
    }

    /**
     * @return the sitemapUrl
     */
    public URL getSitemapUrl() {
	return sitemapUrl;
    }

    /**
     * @param sitemapUrl the sitemapUrl to set
     */
    public void setSitemapUrl(URL sitemapUrl) {
	this.sitemapUrl = sitemapUrl;
    }
}