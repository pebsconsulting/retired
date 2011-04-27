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

package org.xwiki.plugins.eclipse.editors;

import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * This is the text editor widget used inside {@link XWikiEditor}. This class extends
 * {@link AbstractTextEditor} for the purpose of customizing it into XWiki's needs.
 */
public class XWikiMarkupEditorPart extends AbstractTextEditor
{
    public XWikiMarkupEditorPart()
    {
        super();
        setDocumentProvider(new XWikiDocumentProvider());
        setSourceViewerConfiguration(new SourceViewerConfiguration());
    }
}
