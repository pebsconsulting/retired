package org.xwiki.eclipse.plugin;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author venkatesh
 *
 */

public class XwikiEditorMessages {

	private static final String RESOURCE_BUNDLE= "org.xwiki.eclipse.plugin.XwikiEditorMessages";//$NON-NLS-1$

	private static ResourceBundle fgResourceBundle= ResourceBundle.getBundle(RESOURCE_BUNDLE);

	private XwikiEditorMessages() {
	}
	public static String getString(String key) {
		try {
			return fgResourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		}
	}
	public static ResourceBundle getResourceBundle() {
		return fgResourceBundle;
	}
}