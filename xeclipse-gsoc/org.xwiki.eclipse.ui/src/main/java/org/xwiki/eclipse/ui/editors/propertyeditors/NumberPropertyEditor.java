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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.xwiki.eclipse.core.model.XWikiEclipseObjectProperty;

public class NumberPropertyEditor extends BasePropertyEditor {
	Text text;

	public NumberPropertyEditor(FormToolkit toolkit, Composite parent,
			XWikiEclipseObjectProperty property) {
		super(toolkit, parent, property);
	}

	@Override
	public Composite createControl(Composite parent) {
		Section section = toolkit.createSection(parent,
				ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
		section.setText(property.getPrettyName());

		Composite composite = toolkit.createComposite(section, SWT.NONE);
		GridLayoutFactory.fillDefaults().extendedMargins(0, 0, 0, 10).applyTo(
				composite);

		text = toolkit.createText(composite, "", SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true,
				false).applyTo(text);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				property.setValue(text.getText());
				firePropertyModifyListener();
			}
		});

		section.setClient(composite);

		return section;
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof String)
			text.setText((String) value);
	}
}
