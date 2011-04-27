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
package org.xwiki.eclipse.adapters;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.xwiki.eclipse.XWikiEclipseConstants;
import org.xwiki.eclipse.model.IXWikiPage;
import org.xwiki.eclipse.model.impl.ConflictData;
import org.xwiki.plugins.eclipse.XWikiEclipsePlugin;

/**
 * The adapter for XWiki pages
 */
public class XWikiPageAdapter implements IWorkbenchAdapter
{
    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(Object)
     */
    public Object[] getChildren(Object o)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(Object)
     */
    public ImageDescriptor getImageDescriptor(Object object)
    {
        if (object instanceof IXWikiPage) {
            IXWikiPage page = (IXWikiPage) object;

            ConflictData conflictData = page.isConflict();
            
            if (conflictData != null) {
                return XWikiEclipsePlugin
                    .getImageDescriptor(XWikiEclipseConstants.XWIKI_PAGE_CONFLICT_ICON);
            }

            if (page.isCached()) {
                return XWikiEclipsePlugin
                    .getImageDescriptor(XWikiEclipseConstants.XWIKI_PAGE_CACHED_ICON);
            }

            return XWikiEclipsePlugin.getImageDescriptor(XWikiEclipseConstants.XWIKI_PAGE_ICON);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(Object)
     */
    public String getLabel(Object object)
    {
        if (object instanceof IXWikiPage) {
            IXWikiPage page = (IXWikiPage) object;

            return page.getTitle();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(Object)
     */
    public Object getParent(Object o)
    {
        return null;
    }

}
