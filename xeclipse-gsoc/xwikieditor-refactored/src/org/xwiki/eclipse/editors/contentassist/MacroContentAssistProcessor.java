package org.xwiki.eclipse.editors.contentassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.xwiki.eclipse.editors.PageEditor;
import org.xwiki.eclipse.editors.VelocityDirectiveType;
import org.xwiki.eclipse.editors.utils.Utils;

/**
 * @author fmancinelli, venkatesh, malaka
 */
public class MacroContentAssistProcessor implements IContentAssistProcessor
{
    private static Pattern MACRO_DECLARATION_PATTERN = Pattern.compile("#macro *\\( *([\\p{Alnum}_]+)");

    public MacroContentAssistProcessor(PageEditor pageEditor)
    {
        super();
    }

    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset)
    {
        List<CompletionProposal> result = new ArrayList<CompletionProposal>();

        IDocument document = viewer.getDocument();
        String macroPrefix = Utils.getPrefix(document, offset, '#', "$(\n");
        if (macroPrefix != null) {
            for (String directive : getMacros(document, offset)) {
                if (directive.startsWith(macroPrefix)) {
                    int cursorPos;
                    if (directive.equals(VelocityDirectiveType.ELSE) || directive.equals(VelocityDirectiveType.END)
                        || directive.equals(VelocityDirectiveType.STOP)) {
                        cursorPos = directive.length() - 1;
                    } else {
                        directive += "()";
                        cursorPos = directive.length() - 2;
                    }

                    result.add(new CompletionProposal(directive, offset - macroPrefix.length(), macroPrefix.length(),
                        cursorPos, null, "#" + directive, null, null));
                }
            }

            Collections.sort(result, new Comparator<CompletionProposal>()
            {
                public int compare(CompletionProposal proposal1, CompletionProposal proposal2)
                {
                    return proposal1.getDisplayString().compareTo(proposal2.getDisplayString());
                }

                public boolean equals(Object proposal)
                {
                    return false;
                }
            });

            return result.toArray(new ICompletionProposal[result.size()]);
        }

        if (result.size() > 0) {
            return result.toArray(new ICompletionProposal[result.size()]);
        }

        return null;
    }

    private List<String> getMacros(IDocument document, int offset)
    {
        List<String> list = new ArrayList<String>();
        for (VelocityDirectiveType d : VelocityDirectiveType.values()) {
            list.add(d.toString().toLowerCase());
        }

        try {
            String text = document.get(0, offset);

            Matcher m = MACRO_DECLARATION_PATTERN.matcher(text);
            while (m.find()) {
                list.add(m.group(1));
            }
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset)
    {
        return null;
    }

    public char[] getCompletionProposalAutoActivationCharacters()
    {
        return null;
    }

    public char[] getContextInformationAutoActivationCharacters()
    {
        return null;
    }

    public IContextInformationValidator getContextInformationValidator()
    {
        return null;
    }

    public String getErrorMessage()
    {
        return null;
    }

}
