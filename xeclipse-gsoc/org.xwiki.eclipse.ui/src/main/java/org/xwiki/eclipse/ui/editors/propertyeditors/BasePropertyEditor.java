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
package org.xwiki.eclipse.ui.editors.propertyeditors;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.xwiki.eclipse.core.model.XWikiEclipseObjectProperty;

public abstract class BasePropertyEditor {
	private Set<IPropertyModifyListener> listeners;

	protected FormToolkit toolkit;

	protected Control control;

	protected XWikiEclipseObjectProperty property;

	public BasePropertyEditor(FormToolkit toolkit, Composite parent,
			XWikiEclipseObjectProperty property) {
		this.toolkit = toolkit;
		this.property = property;
		control = createControl(parent);
		listeners = new HashSet<IPropertyModifyListener>();
		setValue(property.getValue());
	}

	protected abstract Composite createControl(Composite control);

	public abstract void setValue(Object value);

	public Control getControl() {
		return control;
	}

	public void addPropertyModifyListener(IPropertyModifyListener listener) {
		listeners.add(listener);
	}

	public void removePropertyModifyListener(IPropertyModifyListener listener) {
		listeners.remove(listener);
	}

	protected void firePropertyModifyListener() {
		for (IPropertyModifyListener listener : listeners)
			listener.modifyProperty(property);
	}
}
