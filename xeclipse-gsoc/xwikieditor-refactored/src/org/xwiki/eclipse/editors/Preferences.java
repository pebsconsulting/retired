package org.xwiki.eclipse.editors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * @author fmancinelli, venkatesh, malaka
 */
public class Preferences
{
    public enum Style
    {
        HEADING1, HEADING2, HEADING3, HEADING4, HEADING5, HEADING6, BOLD, ITALIC, UNDERLINE, STRIKEOUT, LINK, LIST_BULLET, TT, MACRO, IMAGE, SUPERSCRIPT, SUBSCRIPT, IDENTIFIER, CODE
    }

    private static Preferences sharedInstance;

    private Map<Style, TextAttribute> stylesToTextAttributeMap;

    private TextAttribute defaultTextAttribute;

    private Preferences()
    {
        stylesToTextAttributeMap = new HashMap<Style, TextAttribute>();

        Font headingFont =
            new Font(Display.getDefault(), JFaceResources.getDefaultFont().getFontData()[0].getName(), 18, SWT.BOLD);
        stylesToTextAttributeMap.put(Style.HEADING1, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_YELLOW), null, SWT.BOLD, headingFont));
        headingFont =
            new Font(Display.getDefault(), JFaceResources.getDefaultFont().getFontData()[0].getName(), 16, SWT.BOLD);
        stylesToTextAttributeMap.put(Style.HEADING2, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_YELLOW), null, SWT.BOLD, headingFont));
        headingFont =
            new Font(Display.getDefault(), JFaceResources.getDefaultFont().getFontData()[0].getName(), 14, SWT.BOLD);
        stylesToTextAttributeMap.put(Style.HEADING3, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_YELLOW), null, SWT.BOLD, headingFont));
        stylesToTextAttributeMap.put(Style.HEADING4, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_YELLOW), null, SWT.BOLD, headingFont));
        stylesToTextAttributeMap.put(Style.HEADING5, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_YELLOW), null, SWT.BOLD, headingFont));
        stylesToTextAttributeMap.put(Style.HEADING6, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_YELLOW), null, SWT.BOLD, headingFont));

        stylesToTextAttributeMap.put(Style.BOLD, new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_BLUE),
            null, SWT.BOLD));

        stylesToTextAttributeMap.put(Style.ITALIC, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_GREEN), null, SWT.ITALIC));

        TextAttribute attribute =
            new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN), null, SWT.NONE);
        stylesToTextAttributeMap.put(Style.UNDERLINE, attribute);
        stylesToTextAttributeMap.put(Style.STRIKEOUT, attribute);
        stylesToTextAttributeMap.put(Style.SUPERSCRIPT, attribute);
        stylesToTextAttributeMap.put(Style.SUBSCRIPT, attribute);

        stylesToTextAttributeMap.put(Style.LINK, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_RED), null, SWT.BOLD));

        stylesToTextAttributeMap.put(Style.IDENTIFIER, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_CYAN), null, SWT.BOLD));

        stylesToTextAttributeMap.put(Style.LIST_BULLET, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_CYAN), null, SWT.BOLD));

        attribute = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA), null, SWT.BOLD);
        stylesToTextAttributeMap.put(Style.MACRO, attribute);
        stylesToTextAttributeMap.put(Style.IMAGE, attribute);

        Font ttFont =
            new Font(Display.getDefault(), "Courier", JFaceResources.getDefaultFont().getFontData()[0].getHeight(),
                SWT.BOLD);
        stylesToTextAttributeMap.put(Style.TT, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_YELLOW), null, SWT.BOLD, ttFont));

        stylesToTextAttributeMap.put(Style.CODE, new TextAttribute(Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_GRAY), null, SWT.BOLD, ttFont));

        defaultTextAttribute = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_BLUE), null, SWT.NONE);
    }

    public static Preferences getDefault()
    {
        if (sharedInstance == null) {
            sharedInstance = new Preferences();
        }

        return sharedInstance;
    }

    public TextAttribute getTextAttribute(Style style)
    {
        TextAttribute result = stylesToTextAttributeMap.get(style);
        if (result == null) {
            result = defaultTextAttribute;
        }

        return result;
    }
}
