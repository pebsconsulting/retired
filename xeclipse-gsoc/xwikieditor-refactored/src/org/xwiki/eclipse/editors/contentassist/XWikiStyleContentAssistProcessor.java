package org.xwiki.eclipse.editors.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.xwiki.eclipse.editors.utils.Utils;

/**
 * @author fmancinelli, venkatesh, malaka
 */
public class XWikiStyleContentAssistProcessor implements IContentAssistProcessor
{
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset)
    {
        List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();

        IDocument document = viewer.getDocument();

        /* Check if we are in the middle of something where markup is not allowed */
        String linkPrefix = Utils.getPrefix(document, offset, '[', "]");
        String variablePrefix = Utils.getPrefix(document, offset, '$', " (\n");
        String macroPrefix = Utils.getPrefix(document, offset, '#', "$(\n");

        if (linkPrefix == null && variablePrefix == null && macroPrefix == null) {
            result.add(new CompletionProposal("**", offset, 0, 1, null, "* Bold", null, null));
            result.add(new CompletionProposal("~~~~", offset, 0, 2, null, "~~ Italic", null, null));
            result.add(new CompletionProposal("____", offset, 0, 2, null, "__ Underline", null, null));
            result.add(new CompletionProposal("----", offset, 0, 2, null, "-- Strikeout", null, null));
            result.add(new CompletionProposal("{table}\n{table}", offset, 0, 2, null, "{table}", null, null));
            result.add(new CompletionProposal("{code}\n{code}", offset, 0, 2, null, "{code}", null, null));
            result.add(new CompletionProposal("{pre}\n{/pre}", offset, 0, 2, null, "{pre}", null, null));
        }

        if (result.size() > 0) {
            return result.toArray(new ICompletionProposal[result.size()]);
        }

        return null;
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
