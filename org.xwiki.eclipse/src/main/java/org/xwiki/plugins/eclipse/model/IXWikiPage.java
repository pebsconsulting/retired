/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 */

package org.xwiki.plugins.eclipse.model;

import java.util.Date;

import org.codehaus.swizzle.confluence.PageSummary;
import org.codehaus.swizzle.confluence.SwizzleConfluenceException;
import org.eclipse.core.runtime.IPath;
import org.xwiki.plugins.eclipse.util.ICacheable;

/**
 * Represents an XWiki document.
 */
public interface IXWikiPage extends ICacheable
{
    /**
     * Initializes this page if it has not been initialized. This method simply retrieves all the
     * data required from the server.
     * 
     * @throws CommunicationException - If XMLRPC call fails.
     */
    public void init() throws SwizzleConfluenceException;

    /**
     * @return pageid As String.
     */
    public String getId();

    /**
     * @return key of the space to which this page belongs to.
     */
    public String getParentSpaceKey();

    /**
     * @return pageid of the parent page of this page.
     */
    public String getParentId();

    /**
     * Changes the parent of this page. Page must be saved (using update()) in order for changes to
     * take effect.
     * 
     * @param pageid Page ID of the new parent.
     */
    public void setParentId(String newParentId);

    /**
     * @return Title of this page.
     */
    public String getTitle();

    /**
     * Changes the title of this page. Page must be saved (using update()) in order for changes to
     * take effect.
     * 
     * @param newTitle new title to be set.
     */
    public void setTitle(String newTitle);

    /**
     * @return Url of this page (if it is to be viewed on browser).
     */
    public String getUrl();

    /**
     * @return Number of locks currently held onto this page.
     */
    public int getLocks();

    /**
     * @return Version of this page.
     */
    public int getVersion();

    /**
     * @return Content of this page (Markup).
     */
    public String getContent();

    /**
     * Changes the content of this page. Page must be saved (using update()) in order for changes to
     * take effect.
     * 
     * @param newContent (Markup) to be set as content.
     */
    public void setContent(String newContent);

    /**
     * @return Creation timestamp of this Page.
     */
    public Date getCreated();

    /**
     * @return Author of this page.
     */
    public String getCreator();

    /**
     * @return Last modified date of this page.
     */
    public Date getModified();

    /**
     * @return User who modified this page for the last time.
     */
    public String getLastModifier();

    /**
     * @return Whether this is the home page of parent space.
     */
    public boolean isHomePage();

    /**
     * @return True if operating off-line.
     */
    public boolean isOffline();

    /**
     * Synchronizes this page with the back-end.
     * 
     * @return TODO
     */
    public boolean synchronize(PageSummary newSummary) throws SwizzleConfluenceException;

    /**
     * Forgets all changes made to this page and obtains a fresh copy from the server
     * 
     * @throws SwizzleConfluenceException
     */
    public void revert() throws SwizzleConfluenceException;

    /**
     * @return Status of content (Markup) of this page.
     */
    public String getContentStatus();

    /**
     * @return The cache path for this page.
     */
    public IPath getCachePath();

    /**
     * @return Whether this page is current and not deleted.
     */
    public boolean isCurrent();

    /**
     * @return Whether the summary of this page has been retrieved or not.
     */
    public boolean isSummaryReady();

    /**
     * @return Whether the data (core) of this page has been retrieved or not.
     */
    public boolean isDataReady();

    /**
     * Whether this page has local changes that have not yet been commited.
     * 
     * @return True if the page is out of sync with the server
     */
    public boolean hasUncommitedChanges();

    /**
     * @return Parent space of this page.
     */
    public IXWikiSpace getParentSpace();

    /**
     * Saves this page in the server. This method should be invoked in order to make local changes
     * to the page permenent.
     * 
     * @return Updated page as returned from the server.
     * @throws CommunicationException - If XMLRPC call fails.
     */
    public IXWikiPage save() throws SwizzleConfluenceException;
}
