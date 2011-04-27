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
package org.xwiki.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xwiki.eclipse.dialogs.OpenPageDialog;
import org.xwiki.eclipse.editors.XWikiPageEditor;
import org.xwiki.eclipse.editors.XWikiPageEditorInput;
import org.xwiki.eclipse.model.IXWikiPage;

public class OpenPageHandler extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        OpenPageDialog dialog = new OpenPageDialog(HandlerUtil.getActiveShell(event));
        IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

        int result = dialog.open();
        if (result == OpenPageDialog.OK) {

            Object selectedObject = dialog.getResult()[0];
            if (selectedObject instanceof IXWikiPage) {
                IXWikiPage xwikiPage = (IXWikiPage) selectedObject;

                XWikiPageEditorInput editorInput = new XWikiPageEditorInput(xwikiPage);
                try {
                    activePage.openEditor(editorInput, XWikiPageEditor.ID);

                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }
}
