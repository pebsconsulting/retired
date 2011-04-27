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

package org.xwiki.plugins.eclipse;

import java.io.File;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.xwiki.eclipse.XWikiEclipsePageIndexer;
import org.xwiki.plugins.eclipse.model.IXWikiConnection;
import org.xwiki.plugins.eclipse.model.impl.XWikiConnectionManager;

/**
 * The activator class controls the plug-in life cycle, this is a mandatory class and is used by the
 * eclipse plugin framework.
 */
public class XWikiEclipsePlugin extends AbstractUIPlugin
{
    /**
     * The plugin ID.
     */
    public static final String PLUGIN_ID = "org.xwiki.eclipse";

    public static final String CONNECTIONS_FILE_NAME = "connections.data";

    public static final String WORKINGSETS_FILE_NAME = "workingsets.data";

    /**
     * The shared instance.
     */
    private static XWikiEclipsePlugin plugin;

    /**
     * The constructor.
     */
    public XWikiEclipsePlugin()
    {
        plugin = this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        File connectionsFile = new File(getStateLocation().toFile(), CONNECTIONS_FILE_NAME);
        if (connectionsFile.exists()) {
            org.xwiki.eclipse.XWikiConnectionManager.getDefault().restoreConnections(
                connectionsFile);
        }

        File workingSetsFile = new File(getStateLocation().toFile(), WORKINGSETS_FILE_NAME);
        if (workingSetsFile.exists()) {
            org.xwiki.eclipse.WorkingSetManager.getDefault().restoreWorkingSets(workingSetsFile);
        }

        XWikiEclipsePageIndexer.getDefault().start();

        /*
         * This is the Quick&Dirty solution as explained at http://ws.apache.org/xmlrpc/ssl.html to
         * make XMLRPC work even on SSL.
         */
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType)
            {
                // Trust always
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType)
            {
                // Trust always
            }
        }};

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        // Create empty HostnameVerifier
        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String arg0, SSLSession arg1)
            {
                return true;
            }
        };

        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        XWikiEclipsePageIndexer.getDefault().stop();

        for (IXWikiConnection con : XWikiConnectionManager.getInstance().getAllConnections()) {
            try {
                con.disconnect();
            } catch (Exception e) {
                // Nothing to do.
            }
        }

        File connections = new File(getStateLocation().toFile(), CONNECTIONS_FILE_NAME);
        org.xwiki.eclipse.XWikiConnectionManager.getDefault().saveConnections(connections);
        org.xwiki.eclipse.XWikiConnectionManager.getDefault().dispose();

        File workingSetsFile = new File(getStateLocation().toFile(), WORKINGSETS_FILE_NAME);
        org.xwiki.eclipse.WorkingSetManager.getDefault().saveWorkingSets(workingSetsFile);

        plugin = null;
        super.stop(context);
    }

    /**
     * @return The shared instance of this plugin.
     */
    public static XWikiEclipsePlugin getDefault()
    {
        return plugin;
    }

    /**
     * @param path The path to image file.
     * @return An image descriptor for the image file at the given plug-in relative path.
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
