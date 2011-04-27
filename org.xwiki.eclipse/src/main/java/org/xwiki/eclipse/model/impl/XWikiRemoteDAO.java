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
package org.xwiki.eclipse.model.impl;

import java.util.List;

import org.codehaus.swizzle.confluence.IdentityObjectConvertor;
import org.codehaus.swizzle.confluence.Page;
import org.codehaus.swizzle.confluence.PageSummary;
import org.codehaus.swizzle.confluence.Space;
import org.codehaus.swizzle.confluence.SpaceSummary;
import org.codehaus.swizzle.confluence.SwizzleConfluenceException;
import org.codehaus.swizzle.confluence.SwizzleXWiki;

/**
 * A Data Access Object for accessing a remote XWiki instance.
 */
public class XWikiRemoteDAO implements IXWikiDAO
{
    private SwizzleXWiki swizzleXWiki;

    /**
     * Constructor.
     * 
     * @param serverUrl The remote URL for the XWiki XML-RPC service.
     * @param username The user name to be used when connecting to the remote server.
     * @param password The password to be used in order to access the remote account.
     * @throws XWikiDAOException
     */
    public XWikiRemoteDAO(String serverUrl, String username, String password)
        throws XWikiDAOException
    {
        try {
            swizzleXWiki = new SwizzleXWiki(serverUrl);
            swizzleXWiki.login(username, password);

            /*
             * This is a workaround for handling XWiki 1.1 and XWiki 1.2. This must be solved in
             * more elegant way (also on the server side). Basically we try to get the XWiki.WebHome
             * page. If some exception is raised then there is a problem with conversion, so we
             * proceed with executing the old initialization code (in the catch body). If the page
             * is correctly retrieved then everything is fine and we can proceed without doing
             * anything else.
             */
            try {
                Page page = swizzleXWiki.getPage("XWiki.WebHome");
            } catch (ClassCastException e) {
                try {
                    // Workaround for finding out xwiki version (server)
                    // (compatibility with <= XWiki 1.1.m4)
                    swizzleXWiki.setNoConversion();
                } catch (SwizzleConfluenceException ex) {
                    // Assume older version of xwiki and turn-off conversion on client.
                    swizzleXWiki.setConvertor(new IdentityObjectConvertor());
                }
            }
                        
        } catch (Exception e) {
            throw new XWikiDAOException(e);
        }

    }

    /**
     * Close the connection to the remote XWiki instance.
     * 
     * @throws XWikiDAOException
     */
    public synchronized void close() throws XWikiDAOException
    {
        try {
            swizzleXWiki.logout();
        } catch (Exception e) {
            throw new XWikiDAOException(e);
        }
    }

    /**
     * @return A list of the remotely available spaces.
     * @throws XWikiDAOException
     */
    @SuppressWarnings("unchecked")
    public synchronized List<SpaceSummary> getSpaces() throws XWikiDAOException
    {
        try {        	
        	List<SpaceSummary> spaces = swizzleXWiki.getSpaces();        	
            return spaces;
        } catch (Exception e) {
            throw new XWikiDAOException(e);
        }
    }

    /**
     * @param key The space key.
     * @return The information about the remote space.
     * @throws XWikiDAOException
     */
    public synchronized Space getSpace(String key) throws XWikiDAOException
    {
        try {
            return swizzleXWiki.getSpace(key);
        } catch (Exception e) {
            throw new XWikiDAOException(e);
        }
    }

    /**
     * @param spaceKey The space key.
     * @return The page summaries for all the pages available in the given space.
     * @throws XWikiDAOException
     */
    @SuppressWarnings("unchecked")
    public synchronized List<PageSummary> getPages(String spaceKey) throws XWikiDAOException
    {
        try {
            return swizzleXWiki.getPages(spaceKey);
        } catch (Exception e) {
            throw new XWikiDAOException(e);
        }
    }

    /**
     * @param id The page id.
     * @return All the page information for the page identified by the given id.
     * @throws XWikiDAOException
     */
    public synchronized Page getPage(String id) throws XWikiDAOException
    {
        try {
            return swizzleXWiki.getPage(id);
        } catch (Exception e) {
            throw new XWikiDAOException(e);
        }
    }

    /**
     * Stores a page on the remote server.
     * 
     * @param page The page information to be stored.
     * @throws XWikiDAOException
     */
    public synchronized void storePage(Page page) throws XWikiDAOException
    {
        try {
            swizzleXWiki.storePage(page);
        } catch (Exception e) {
            throw new XWikiDAOException(e);
        }
    }

    public synchronized Space createSpace(String key, String name, String description)
        throws XWikiDAOException
    {
        Space space = new Space();
        space.setKey(key);
        space.setName(name);
        space.setDescription(description);

        try {
            space = swizzleXWiki.addSpace(space);
        } catch (SwizzleConfluenceException e) {
            e.printStackTrace();
            throw new XWikiDAOException(e);
        }

        return space;
    }

    public synchronized Page createPage(String spaceKey, String title, String content)
        throws XWikiDAOException
    {
        Page page = new Page();
        page.setSpace(spaceKey);
        page.setTitle(title);
        page.setContent(content);

        try {
            page = swizzleXWiki.storePage(page);
        } catch (SwizzleConfluenceException e) {
            e.printStackTrace();
            throw new XWikiDAOException(e);
        }

        return page;
    }

    public synchronized void removePage(String id) throws XWikiDAOException
    {
        try {
            swizzleXWiki.removePage(id);
        } catch (SwizzleConfluenceException e) {
            e.printStackTrace();
            throw new XWikiDAOException(e);
        }

    }

    public synchronized void removeSpace(String key) throws XWikiDAOException
    {
        try {
            swizzleXWiki.removeSpace(key);
        } catch (SwizzleConfluenceException e) {
            e.printStackTrace();
            throw new XWikiDAOException(e);
        }
    }

}
