package org.xwiki.eclipse.editors;

import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.xwiki.eclipse.editors.contentassist.CompoundContentAssistProcessor;
import org.xwiki.eclipse.editors.contentassist.MacroContentAssistProcessor;
import org.xwiki.eclipse.editors.contentassist.VelocityVariableContentAssistProcessor;
import org.xwiki.eclipse.editors.contentassist.XWikiHeadingContentAssistProcessor;
import org.xwiki.eclipse.editors.contentassist.XWikiLinkContentAssistProcessor;
import org.xwiki.eclipse.editors.contentassist.XWikiStyleContentAssistProcessor;
import org.xwiki.eclipse.editors.contentassist.strategies.TableAutoEditStrategy;
import org.xwiki.eclipse.editors.contentassist.strategies.VelocityAutoEditStrategy;
import org.xwiki.eclipse.editors.contentassist.strategies.XWikiMarkupAutoEditStrategy;
import org.xwiki.eclipse.editors.scanners.VelocityScanner;
import org.xwiki.eclipse.editors.scanners.XWikiMarkupScanner;
import org.xwiki.eclipse.editors.scanners.XWikiPartitionScanner;

/**
 * @author fmancinelli, venkatesh, malaka
 */
public class XWikiSourceViewerConfiguration extends TextSourceViewerConfiguration
{
    private PageEditor pageEditor = null;

    public XWikiSourceViewerConfiguration(PageEditor pageEditor)
    {
        super();
        this.pageEditor = pageEditor;
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer)
    {
        PresentationReconciler reconciler = new PresentationReconciler();
        reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

        /* Use the XWiki markup for tables and default content. */
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new XWikiMarkupScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        reconciler.setDamager(dr, XWikiPartitionScanner.XWIKI_TABLE);
        reconciler.setRepairer(dr, XWikiPartitionScanner.XWIKI_TABLE);

        /* Use a uniform style for code blocks. */
        RuleBasedScanner codeScanner = new RuleBasedScanner();
        codeScanner.setDefaultReturnToken(new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.CODE)));
        dr = new DefaultDamagerRepairer(codeScanner);
        reconciler.setDamager(dr, XWikiPartitionScanner.XWIKI_CODE);
        reconciler.setRepairer(dr, XWikiPartitionScanner.XWIKI_CODE);

        /* Use a uniform style for pre blocks. */
        RuleBasedScanner preScanner = new RuleBasedScanner();
        preScanner.setDefaultReturnToken(new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.CODE)));
        dr = new DefaultDamagerRepairer(preScanner);
        reconciler.setDamager(dr, XWikiPartitionScanner.XWIKI_PRE);
        reconciler.setRepairer(dr, XWikiPartitionScanner.XWIKI_PRE);

        /* Use the Velocity scanner for Velocity blocks. */
        dr = new DefaultDamagerRepairer(new VelocityScanner());
        reconciler.setDamager(dr, XWikiPartitionScanner.VELOCITY);
        reconciler.setRepairer(dr, XWikiPartitionScanner.VELOCITY);

        return reconciler;
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
    {
        ContentAssistant contentAssistant = new ContentAssistant();

        CompoundContentAssistProcessor compoundContentAssistProcessor;

        /* Content assist for standard paragraphs and tables */
        compoundContentAssistProcessor = new CompoundContentAssistProcessor();
        compoundContentAssistProcessor.addContentAssistProcessor(new XWikiLinkContentAssistProcessor());
        compoundContentAssistProcessor
            .addContentAssistProcessor(new VelocityVariableContentAssistProcessor(pageEditor));
        compoundContentAssistProcessor.addContentAssistProcessor(new MacroContentAssistProcessor(pageEditor));
        compoundContentAssistProcessor.addContentAssistProcessor(new XWikiHeadingContentAssistProcessor());
        compoundContentAssistProcessor.addContentAssistProcessor(new XWikiStyleContentAssistProcessor());
        contentAssistant.setContentAssistProcessor(compoundContentAssistProcessor, IDocument.DEFAULT_CONTENT_TYPE);
        contentAssistant.setContentAssistProcessor(compoundContentAssistProcessor, XWikiPartitionScanner.XWIKI_TABLE);

        /* Content assist for Velocity partitions */
        compoundContentAssistProcessor = new CompoundContentAssistProcessor();
        compoundContentAssistProcessor
            .addContentAssistProcessor(new VelocityVariableContentAssistProcessor(pageEditor));
        compoundContentAssistProcessor.addContentAssistProcessor(new MacroContentAssistProcessor(pageEditor));
        contentAssistant.setContentAssistProcessor(compoundContentAssistProcessor, XWikiPartitionScanner.VELOCITY);

        contentAssistant.enableAutoActivation(true);

        return contentAssistant;
    }

    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType)
    {
        if (contentType.equals(IDocument.DEFAULT_CONTENT_TYPE)) {
            return new IAutoEditStrategy[] {new XWikiMarkupAutoEditStrategy()};
        } else if (contentType.equals(XWikiPartitionScanner.XWIKI_TABLE)) {
            return new IAutoEditStrategy[] {new TableAutoEditStrategy(), new XWikiMarkupAutoEditStrategy()};
        } else if (contentType.equals(XWikiPartitionScanner.VELOCITY)) {
            return new IAutoEditStrategy[] {new VelocityAutoEditStrategy()};
        }

        return super.getAutoEditStrategies(sourceViewer, contentType);
    }

    @Override
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
    {
        return new String[] {IDocument.DEFAULT_CONTENT_TYPE, XWikiPartitionScanner.XWIKI_TABLE,
        XWikiPartitionScanner.VELOCITY, XWikiPartitionScanner.XWIKI_CODE, XWikiPartitionScanner.XWIKI_PRE};
    }
}
