package org.xwiki.eclipse.editors.scanners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.xwiki.eclipse.editors.Constants;
import org.xwiki.eclipse.editors.Preferences;
import org.xwiki.eclipse.editors.scanners.rules.BalancedParenthesisRule;
import org.xwiki.eclipse.editors.scanners.rules.RegExRule;

/**
 * @author fmancinelli, venkatesh, malaka
 */
public class XWikiMarkupScanner extends RuleBasedScanner
{
    public XWikiMarkupScanner()
    {
        IToken boldToken = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.BOLD));

        IToken italicToken = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.ITALIC));

        IToken linkToken = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.LINK));

        IToken listBulletToken = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.LIST_BULLET));

        IToken heading1Token = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.HEADING1));
        IToken heading2Token = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.HEADING2));
        IToken heading3Token = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.HEADING3));
        IToken heading4Token = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.HEADING4));
        IToken heading5Token = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.HEADING5));
        IToken heading6Token = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.HEADING6));

        IToken imageToken = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.IMAGE));

        IToken identifierToken = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.IDENTIFIER));

        IToken otherStyleToken = new Token(Preferences.getDefault().getTextAttribute(Preferences.Style.UNDERLINE));

        List<IRule> rules = new ArrayList<IRule>();

        /* RegEx rules work better with respect to SingleLineRules */
        RegExRule regExRule = new RegExRule("1 .*\n?", heading1Token);
        regExRule.setColumnConstraint(0);
        rules.add(regExRule);

        regExRule = new RegExRule("1.1 .*\n?", heading2Token);
        regExRule.setColumnConstraint(0);
        rules.add(regExRule);

        regExRule = new RegExRule("1.1.1 .*\n?", heading3Token);
        regExRule.setColumnConstraint(0);
        rules.add(regExRule);

        regExRule = new RegExRule("1.1.1.1 .*\n?", heading4Token);
        regExRule.setColumnConstraint(0);
        rules.add(regExRule);

        regExRule = new RegExRule("1.1.1.1.1 .*\n?", heading5Token);
        regExRule.setColumnConstraint(0);
        rules.add(regExRule);

        regExRule = new RegExRule("1.1.1.1.1.1 .*\n?", heading6Token);
        regExRule.setColumnConstraint(0);
        rules.add(regExRule);

        regExRule = new RegExRule(Constants.LIST_BULLET_PATTERN, listBulletToken);
        regExRule.setColumnConstraint(0);
        rules.add(regExRule);

        rules.add(new SingleLineRule("*", "*", boldToken, '\\'));
        rules.add(new SingleLineRule("~~", "~~", italicToken, '\\'));
        rules.add(new SingleLineRule("[", "]", linkToken, '\\'));
        rules.add(new SingleLineRule("__", "__", otherStyleToken, '\\'));
        rules.add(new SingleLineRule("--", "--", otherStyleToken, '\\'));
        rules.add(new SingleLineRule("<tt>", "</tt>", otherStyleToken, '\\'));
        rules.add(new SingleLineRule("<sub>", "</sub>", otherStyleToken, '\\'));
        rules.add(new SingleLineRule("<sup>", "</sup>", otherStyleToken, '\\'));

        rules.add(new SingleLineRule("{image:", "}", imageToken, '\\'));

        rules.add(new BalancedParenthesisRule('$', identifierToken));

        setRules(rules.toArray(new IRule[rules.size()]));
    }
}
