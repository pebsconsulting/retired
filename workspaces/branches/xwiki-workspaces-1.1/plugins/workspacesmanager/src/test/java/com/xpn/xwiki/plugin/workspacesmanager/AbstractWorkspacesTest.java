package com.xpn.xwiki.plugin.workspacesmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jmock.Mock;
import org.jmock.core.Invocation;
import org.jmock.core.stub.CustomStub;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiConfig;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.plugin.XWikiPluginManager;
import com.xpn.xwiki.plugin.spacemanager.impl.SpaceImpl;
import com.xpn.xwiki.plugin.spacemanager.impl.SpaceManagerImpl;
import com.xpn.xwiki.plugin.workspacesmanager.apps.activities.WorkspacesActivityStreamPlugin;
import com.xpn.xwiki.plugin.workspacesmanager.apps.activities.WorkspacesActivityStreamPluginApi;
import com.xpn.xwiki.store.XWikiHibernateStore;
import com.xpn.xwiki.store.XWikiHibernateVersioningStore;
import com.xpn.xwiki.store.XWikiStoreInterface;
import com.xpn.xwiki.store.XWikiVersioningStoreInterface;

/**
 * Setup copied from SpaceManagerTestImp until I find how to extend tests cross projects
 * 
 * @see com.xpn.xwiki.plugin.spacemanager.impl.SpaceManagerImplTest
 */
public abstract class AbstractWorkspacesTest extends org.jmock.cglib.MockObjectTestCase
{
    protected XWikiContext context;

    protected SpaceManagerImpl spaceManager;

    protected XWiki xwiki;

    protected Mock mockXWikiStore;

    protected Mock mockXWikiVersioningStore;

    protected Mock mockXWSActivityStreamPlugin;

    protected Mock mockXWSActivityStreamApi;

    protected Mock mockPluginManager;

    private Map docs = new HashMap();

    private List spaces = new ArrayList();

    /**
     * Set up unit testing
     */
    protected void setUp() throws XWikiException
    {
        this.context = new XWikiContext();
        this.context.setUser("XWiki.TestUser");
        this.xwiki = new XWiki(new XWikiConfig(), this.context);

        this.mockXWSActivityStreamPlugin =
            mock(WorkspacesActivityStreamPlugin.class, new Class[] {String.class, String.class,
            XWikiContext.class}, new Object[] {"", "", this.context});

        this.mockXWSActivityStreamApi =
            mock(WorkspacesActivityStreamPluginApi.class, new Class[] {
            WorkspacesActivityStreamPlugin.class, XWikiContext.class}, new Object[] {
            new WorkspacesActivityStreamPlugin("", "", this.context), this.context});
        
        this.mockXWSActivityStreamApi.expects(once()).method("addDocumentActivityEvent");
        
        WorkspacesActivityStreamPluginApi wasApi =
            (WorkspacesActivityStreamPluginApi) mockXWSActivityStreamApi.proxy();

        this.mockXWSActivityStreamPlugin.expects(once()).method("getPluginApi").will(
            returnValue(mockXWSActivityStreamApi.proxy()));

        Vector pluginNames = new Vector();
        pluginNames.add("xwsactivitystream");

        this.mockPluginManager = mock(XWikiPluginManager.class);
        this.mockPluginManager.stubs().method("getPlugin").will(
            returnValue(mockXWSActivityStreamPlugin.proxy()));
        this.mockPluginManager.stubs().method("getPlugins").will(returnValue(pluginNames));

        this.xwiki.setPluginManager((XWikiPluginManager) mockPluginManager.proxy());

        this.mockXWikiStore =
            mock(XWikiHibernateStore.class, new Class[] {XWiki.class, XWikiContext.class},
                new Object[] {this.xwiki, this.context});
        this.mockXWikiStore.expects(atLeastOnce()).method("executeWrite");
        this.mockXWikiStore.stubs().method("loadXWikiDoc").will(
            new CustomStub("Implements XWikiStoreInterface.loadXWikiDoc")
            {
                public Object invoke(Invocation invocation) throws Throwable
                {
                    XWikiDocument shallowDoc = (XWikiDocument) invocation.parameterValues.get(0);

                    if (docs.containsKey(shallowDoc.getFullName())) {
                        return (XWikiDocument) docs.get(shallowDoc.getFullName());
                    } else {
                        return shallowDoc;
                    }
                }
            });
        this.mockXWikiStore.stubs().method("saveXWikiDoc").will(
            new CustomStub("Implements XWikiStoreInterface.saveXWikiDoc")
            {
                public Object invoke(Invocation invocation) throws Throwable
                {
                    XWikiDocument document = (XWikiDocument) invocation.parameterValues.get(0);
                    document.setNew(false);
                    document.setStore((XWikiStoreInterface) mockXWikiStore.proxy());
                    docs.put(document.getFullName(), document);
                    if ((document.getName().equals("WebPreferences"))) {
                        String type = null;
                        try {
                            type =
                                document.getStringValue(spaceManager.getSpaceClassName(),
                                    SpaceImpl.SPACE_TYPE);
                        } catch (Exception e) {
                        }
                        if (spaceManager.getSpaceTypeName().equals(type)) {
                            if (!spaces.contains("" + document.getSpace()))
                                spaces.add("" + document.getSpace());
                        }
                        if ("deleted".equals(type))
                            spaces.remove("" + document.getSpace());
                    }
                    return null;
                }
            });

        this.mockXWikiStore.stubs().method("saveXWikiObject");

        this.mockXWikiStore.stubs().method("getTranslationList").will(
            returnValue(Collections.EMPTY_LIST));
        this.mockXWikiStore.stubs().method("search").will(returnValue(spaces));

        this.mockXWikiStore.stubs().method("searchDocumentsNames").will(
            returnValue(new ArrayList()));

        this.mockXWikiVersioningStore =
            mock(XWikiHibernateVersioningStore.class, new Class[] {XWiki.class,
            XWikiContext.class}, new Object[] {this.xwiki, this.context});
        this.mockXWikiVersioningStore.stubs().method("getXWikiDocumentArchive").will(
            returnValue(null));

        this.xwiki.setStore((XWikiStoreInterface) mockXWikiStore.proxy());
        this.xwiki.setVersioningStore((XWikiVersioningStoreInterface) mockXWikiVersioningStore
            .proxy());
        this.xwiki.setDatabase("xwiki");

        this.context.setWiki(xwiki);
        this.context.setUser("XWiki.NotAdmin");
        this.context.setDatabase("xwiki");
        this.context.setMainXWiki("xwiki");

        this.spaceManager =
            new SpaceManagerImpl("spacemanager", SpaceManagerImpl.class.toString(), context);
        this.spaceManager.setMailNotification(false);
        this.spaceManager.init(context);

        XWikiDocument prefdoc = new XWikiDocument("XWiki", "XWikiPreferences");
        BaseObject obj = new BaseObject();
        obj.setName("XWiki.XWikiPreferences");
        obj.setClassName("XWiki.XWikiGlobalRights");
        obj.setStringValue("users", "XWiki.Admin");
        obj.setStringValue("groups", "");
        obj.setStringValue("levels", "admin,programming");
        obj.setIntValue("allow", 1);
        prefdoc.addObject("XWiki.XWikiGlobalRights", obj);
        this.xwiki.saveDocument(prefdoc, context);

        XWikiDocument doc = new XWikiDocument("Main", "WebHome");
        doc.setAuthor("xwiki:XWiki.Admin");
        context.setDoc(doc);
    }

}
