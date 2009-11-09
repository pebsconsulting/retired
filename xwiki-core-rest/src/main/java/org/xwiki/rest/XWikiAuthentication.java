package org.xwiki.rest;

import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.Guard;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.Request;
import org.xwiki.rest.resources.BrowserAuthenticationResource;

import com.noelios.restlet.http.HttpConstants;
import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;

/**
 * This guard performs an authentication using the HTTP_BASIC method against XWiki. If no Authorization header is
 * provided then Guest is assumed. In order to make plain browser capable of authenticate themselves a special URI is
 * used. If this URI is get, then a challenge is sent back to the browser giving the user the chance to type a username
 * and a password.
 * 
 * @version $Id$
 */
public class XWikiAuthentication extends Guard
{

    public XWikiAuthentication(Context context) throws IllegalArgumentException
    {
        super(context, ChallengeScheme.HTTP_BASIC, "XWiki");
    }

    @Override
    public int authenticate(Request request)
    {
        if (request.getResourceRef().getPath().endsWith(BrowserAuthenticationResource.URI_PATTERN)) {
            return super.authenticate(request);
        }

        Form headers = (Form) request.getAttributes().get(HttpConstants.ATTRIBUTE_HEADERS);

        Form queryParameters = request.getResourceRef().getQueryAsForm();
        if (queryParameters.getValues("login") == null) {
            if (headers.getValues(HttpConstants.HEADER_AUTHORIZATION) == null) {
                headers.add(HttpConstants.HEADER_AUTHORIZATION, "Basic R3Vlc3Q6");
            }
        }

        return super.authenticate(request);
    }

    @Override
    public boolean checkSecret(Request request, String identifier, char[] secret)
    {
        XWikiContext xwikiContext = (XWikiContext) getContext().getAttributes().get(Constants.XWIKI_CONTEXT);
        XWiki xwiki = (XWiki) getContext().getAttributes().get(Constants.XWIKI);

        try {
            if (identifier.equals("Guest")) {
                xwikiContext.setUser("XWiki.XWikiGuest");
                getLogger().log(Level.INFO, String.format("Authenticated as '%s'.", identifier));
                return true;
            }

            if (xwiki.getAuthService().authenticate(identifier, new String(secret), xwikiContext) != null) {
                xwikiContext.setUser(String.format("XWiki.%s", identifier));
                getLogger().log(Level.INFO, String.format("Authenticated as '%s'.", identifier));
                return true;
            }
        } catch (XWikiException e) {
            e.printStackTrace();
        }

        getLogger().log(Level.INFO, String.format("Cannot authenticate '%s'.", identifier));

        return false;
    }
}
