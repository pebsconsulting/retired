package org.xwiki.rest.resources;

import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;
import org.xwiki.rest.DomainObjectFactory;
import org.xwiki.rest.XWikiResource;
import org.xwiki.rest.model.Page;

import com.xpn.xwiki.api.Document;

/**
 * Resource for translations of a page.
 * 
 * @version $Id$
 */
public class TranslationsResource extends XWikiResource
{
    /**
     * Get all the available translations for a page.
     * 
     * @param variant The variant.
     * @return representation The XML containing the list of spaces.
     */
    @Override
    public Representation represent(Variant variant)
    {
        DocumentInfo documentInfo = getDocumentFromRequest(getRequest(), true);
        if (documentInfo == null) {
            /* If the document doesn't exist send a not found header */
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }

        Document doc = documentInfo.getDocument();

        /* Check if we have access to it */
        if (doc == null) {
            getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN);
            return null;
        }

        Page page = DomainObjectFactory.createPage(getRequest(), resourceClassRegistry, doc);
        if (page == null) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return null;
        }

        /*
         * Set the pageId attribute of the translations list. Normally this is not set because it is embedded in a page
         * element so it's redundant.
         */
        page.getTranslations().setPageFullName(page.getFullName());

        return getRepresenterFor(variant).represent(getContext(), getRequest(), getResponse(), page.getTranslations());
    }

}
