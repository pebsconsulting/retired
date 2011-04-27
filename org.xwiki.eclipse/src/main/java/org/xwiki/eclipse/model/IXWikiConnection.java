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
package org.xwiki.eclipse.model;

import java.util.Collection;

/**
 * The interface for accessing data provided by an XWiki instance. IXWikiConnection instances can be
 * obtained through {@link XWikiConnectionFactory}
 */
public interface IXWikiConnection
{
    /**
     * @return An unique identifier for the connection.
     */
    public String getId();

    /**
     * Connects to the remote XWiki server.
     * 
     * @param password The password to be used in order to access the remote account.
     * @throws XWikiConnectionException
     */
    public void connect(String password) throws XWikiConnectionException;

    /**
     * Disconnects from the remote XWiki server.
     * 
     * @throws XWikiConnectionException
     */
    public void disconnect() throws XWikiConnectionException;

    /**
     * Dispose the connection manager by releasing all the resources associated with it. This method
     * must be called whenever the connection is not used anymore.
     * 
     * @throws XWikiConnectionException
     */
    public void dispose() throws XWikiConnectionException;

    /**
     * @return true is the connection is connected to the remote XWiki server.
     */
    public boolean isConnected();

    /**
     * @return A collection of space descriptors for the available spaces.
     * @throws XWikiConnectionException
     */
    public Collection<IXWikiSpace> getSpaces() throws XWikiConnectionException;

    /**
     * @param spaceKey The key for the space to be queried.
     * @return A collection of page descriptors for the pages available in the space with the given
     *         key.
     * @throws XWikiConnectionException
     */
    public Collection<IXWikiPage> getPages(IXWikiSpace space) throws XWikiConnectionException;

    /**
     * @return The pages that have already been fetched and for which we have a local knowledge.
     */
    public Collection<IXWikiPage> getKnownPages();

    /**
     * @param pageId The page id for the page to be retrieved.
     * @return The page with the given id
     * @throws XWikiConnectionException
     */
    public IXWikiPage getPage(String pageId) throws XWikiConnectionException;

    /**
     * @return The username associated to this connection.
     */
    public String getUserName();

    /**
     * @return The URL of the XWiki associated to this connection.
     */
    public String getServerUrl();

    /**
     * Create a space on the remove XWiki
     * 
     * @param key 
     * @param name
     * @param description
     * @throws XWikiConnectionException
     */
    public IXWikiSpace createSpace(String key, String name, String description)
        throws XWikiConnectionException;

    /**
     * Create a page on the remote XWiki
     * 
     * @param space The space where the page will be created.
     * @param name The page name.
     * @param content The page content.
     * @return The newly created page.
     * @throws XWikiConnectionException
     */
    public IXWikiPage createPage(IXWikiSpace space, String name, String content)
        throws XWikiConnectionException;

    /**
     * Remove a space from the remote XWiki.
     * 
     * @param space The space to be removed.
     * @throws XWikiConnectionException
     */
    public void removeSpace(IXWikiSpace space) throws XWikiConnectionException;

    /**
     * Remove a page from the remote XWiki.
     * 
     * @param page The page to be removed.
     * @throws XWikiConnectionException
     */
    public void removePage(IXWikiPage page) throws XWikiConnectionException;
}
