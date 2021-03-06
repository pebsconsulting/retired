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
package org.xwiki.eclipse.core.storage;

import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.swizzle.confluence.ServerInfo;
import org.codehaus.swizzle.confluence.SpaceSummary;
import org.eclipse.core.runtime.Assert;
import org.xwiki.eclipse.core.XWikiEclipseException;
import org.xwiki.xmlrpc.XWikiXmlRpcClient;
import org.xwiki.xmlrpc.model.XWikiClass;
import org.xwiki.xmlrpc.model.XWikiClassSummary;
import org.xwiki.xmlrpc.model.XWikiObject;
import org.xwiki.xmlrpc.model.XWikiObjectSummary;
import org.xwiki.xmlrpc.model.XWikiPage;
import org.xwiki.xmlrpc.model.XWikiPageSummary;

/**
 * This class implements a remote XWiki data storage. Basically it wraps the
 * XWiki XMLRPC interface.
 * 
 * @author fmancinelli
 */
public class RemoteXWikiDataStorage implements IDataStorage {
	private XWikiXmlRpcClient rpc;

	private boolean disposed;

	public RemoteXWikiDataStorage(String endpoint, String userName,
			String password) throws XWikiEclipseException {
		try {
			rpc = new XWikiXmlRpcClient(endpoint);
			rpc.login(userName, password);
			disposed = false;
		} catch (Exception e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized void dispose() {
		Assert.isTrue(!disposed);

		try {
			rpc.logout();
		} catch (XmlRpcException e) {
			// Ignore
		}

		disposed = true;
	}

	public synchronized XWikiPage getPage(String pageId)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.getPage(pageId);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized List<XWikiPageSummary> getPages(String spaceKey)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.getPages(spaceKey);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized List<SpaceSummary> getSpaces()
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.getSpaces();
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized XWikiPage storePage(XWikiPage page)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.storePage(page);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized ServerInfo getServerInfo() throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.getServerInfo();
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized boolean removePage(String pageId)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.removePage(pageId);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}

	}

	public synchronized List<XWikiObjectSummary> getObjects(String pageId)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.getObjects(pageId);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized XWikiObject getObject(String pageId, String className,
			int objectId) throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.getObject(pageId, className, objectId);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized XWikiClass getClass(String classId)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.getClass(classId);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public synchronized XWikiObject storeObject(XWikiObject object)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.storeObject(object);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}

	}

	public synchronized void storeClass(XWikiClass xwikiClass)
			throws XWikiEclipseException {
		// Do nothing
	}

	public synchronized boolean exists(String pageId) {
		// TODO implement
		return true;
	}

	public synchronized boolean exists(String pageId, String className,
			int objectId) {
		// TODO implement
		return true;
	}

	public List<XWikiClassSummary> getClasses() throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.getClasses();
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public boolean removeObject(String pageId, String className, int objectId)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		try {
			return rpc.removeObject(pageId, className, objectId);
		} catch (XmlRpcException e) {
			throw new XWikiEclipseException(e);
		}
	}

	public XWikiPageSummary getPageSummary(String pageId)
			throws XWikiEclipseException {
		Assert.isTrue(!disposed);

		String[] pageIdComponents = pageId.split("\\."); //$NON-NLS-1$
		Assert.isTrue(pageIdComponents.length == 2);

		List<XWikiPageSummary> pageSummaries = getPages(pageIdComponents[0]);

		for (XWikiPageSummary pageSummary : pageSummaries)
			if (pageSummary.getId().equals(pageId))
				return pageSummary;

		return null;
	}
}
