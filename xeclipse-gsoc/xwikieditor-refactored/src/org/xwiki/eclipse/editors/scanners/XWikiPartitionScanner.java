package org.xwiki.eclipse.editors.scanners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.xwiki.eclipse.editors.scanners.rules.BalancedParenthesisRule;

/**
 * @author fmancinelli, venkatesh, malaka
 */
public class XWikiPartitionScanner extends RuleBasedPartitionScanner
{
    public static final String XWIKI_CODE = "__xwiki_code";

    public static final String XWIKI_PRE = "__xwiki_pre";

    public static final String XWIKI_DL = "__xwiki_dl";

    public static final String XWIKI_TABLE = "__xwiki_table";

    public static final String XWIKI_STYLE = "__xwiki_style";

    public static final String XWIKI_FLOATINGBOX = "__xwiki_floatingbox";

    public static final String VELOCITY = "__velocity";

    public static final String[] ALL_PARTITIONS =
        {XWIKI_CODE, XWIKI_PRE, XWIKI_DL, XWIKI_TABLE, XWIKI_STYLE, XWIKI_FLOATINGBOX, VELOCITY};

    public XWikiPartitionScanner()
    {
        IToken codeToken = new Token(XWIKI_CODE);
        IToken preToken = new Token(XWIKI_PRE);
        IToken tableToken = new Token(XWIKI_TABLE);
        IToken dlToken = new Token(XWIKI_DL);
        IToken styleToken = new Token(XWIKI_STYLE);
        IToken floatingBoxToken = new Token(XWIKI_FLOATINGBOX);
        IToken velocityToken = new Token(VELOCITY);

        List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

        rules.add(new MultiLineRule("{code}", "{code}", codeToken));
        rules.add(new MultiLineRule("{pre}", "{/pre}", preToken));
        rules.add(new MultiLineRule("{table}", "{table}", tableToken));
        rules.add(new MultiLineRule("{style:", "{style}", styleToken));
        rules.add(new MultiLineRule("<dl>", "</dl>", dlToken));
        rules.add(new BalancedParenthesisRule('#', velocityToken));

        setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
    }
}
