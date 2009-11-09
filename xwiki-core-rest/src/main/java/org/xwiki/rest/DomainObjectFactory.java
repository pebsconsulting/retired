package org.xwiki.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.xwiki.rest.model.HistorySummary;
import org.xwiki.rest.model.Link;
import org.xwiki.rest.model.Page;
import org.xwiki.rest.model.PageSummary;
import org.xwiki.rest.model.Relations;
import org.xwiki.rest.model.Space;
import org.xwiki.rest.model.Translations;
import org.xwiki.rest.model.Wikis;
import org.xwiki.rest.model.XWikiRoot;
import org.xwiki.rest.resources.PageResource;
import org.xwiki.rest.resources.PageTranslationResource;
import org.xwiki.rest.resources.PageTranslationVersionResource;
import org.xwiki.rest.resources.PageVersionResource;
import org.xwiki.rest.resources.PagesResource;
import org.xwiki.rest.resources.RootResource;
import org.xwiki.rest.resources.SpaceResource;
import org.xwiki.rest.resources.SpacesResource;
import org.xwiki.rest.resources.WikisResource;

import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.api.Document;
import com.xpn.xwiki.doc.rcs.XWikiRCSNodeId;

/**
 * @version $Id$
 */
public class DomainObjectFactory
{
    public static XWikiRoot createXWikiRoot(Request request, com.xpn.xwiki.api.XWiki xwikiApi,
        XWikiResourceClassRegistry registry)
    {
        XWikiRoot xwikiRoot = new XWikiRoot(xwikiApi.getVersion());

        String fullUri =
            String.format("%s%s", request.getRootRef(), registry.getUriPatternForResourceClass(WikisResource.class));
        Link link = new Link(Utils.formatUriTemplate(fullUri));
        link.setRel(Relations.WIKIS);
        xwikiRoot.addLink(link);

        fullUri =
            String.format("%s%s", request.getRootRef(), registry.getUriPatternForResourceClass(RootResource.class));
        link = new Link(Utils.formatUriTemplate(fullUri));
        link.setRel(Relations.WADL);
        link.setType(MediaType.APPLICATION_WADL_XML.toString());
        xwikiRoot.addLink(link);

        return xwikiRoot;
    }

    public static Wikis createWikis(Request request, com.xpn.xwiki.XWiki xwiki,
        com.xpn.xwiki.XWikiContext xwikiContext, XWikiResourceClassRegistry registry)
    {
        Wikis wikis = new Wikis();

        List<String> databaseNames = new ArrayList<String>();

        try {
            databaseNames = xwiki.getVirtualWikisDatabaseNames(xwikiContext);
        } catch (XWikiException e) {
            /* Ignore */
        }

        for (String databaseName : databaseNames) {
            String fullUri =
                String.format("%s%s", request.getRootRef(), registry
                    .getUriPatternForResourceClass(SpacesResource.class));
            Link link = new Link(Utils.formatUriTemplate(fullUri));
            link.setRel(Relations.SPACES);
            wikis.addLink(link);
        }

        return wikis;

    }

    public static Space createSpace(Request request, XWikiResourceClassRegistry resourceClassRegistry, String wiki,
        String spaceName, String home, int numberOfPages)
    {
        Space space = new Space(wiki, spaceName, home, numberOfPages);

        String fullUri =
            String.format("%s%s", request.getRootRef(), resourceClassRegistry
                .getUriPatternForResourceClass(PagesResource.class));
        Link link =
            new Link(Utils.formatUriTemplate(fullUri, Constants.WIKI_NAME_PARAMETER, wiki,
                Constants.SPACE_NAME_PARAMETER, spaceName));
        link.setRel(Relations.PAGES);

        space.addLink(link);

        return space;
    }

    public static PageSummary createPageSummary(Request request, XWikiResourceClassRegistry resourceClassRegistry,
        Document doc)
    {
        try {
            PageSummary pageSummary = new PageSummary();

            pageSummary.setWiki(doc.getWiki());
            pageSummary.setId(doc.getFullName());
            pageSummary.setFullId(doc.getPrefixedFullName());
            pageSummary.setSpace(doc.getSpace());
            pageSummary.setName(doc.getName());
            pageSummary.setTitle(doc.getTitle());

            Translations translations = pageSummary.getTranslations();

            List<String> languages = doc.getTranslationList();

            if (!languages.isEmpty()) {
                if (!doc.getDefaultLanguage().equals("")) {
                    translations.setDefaultTranslation(doc.getDefaultLanguage());
                }
            }

            String fullUri;
            Link link;

            for (String language : languages) {
                fullUri =
                    String.format("%s%s", request.getRootRef(), resourceClassRegistry
                        .getUriPatternForResourceClass(PageTranslationResource.class));
                link =
                    new Link(Utils.formatUriTemplate(fullUri, Constants.WIKI_NAME_PARAMETER, doc.getWiki(),
                        Constants.SPACE_NAME_PARAMETER, doc.getSpace(), Constants.PAGE_NAME_PARAMETER, doc.getName(),
                        Constants.LANGUAGE_ID_PARAMETER, language));
                link.setRel(Relations.TRANSLATION);
                link.setHrefLang(language);
                translations.addLink(link);
            }

            fullUri =
                String.format("%s%s", request.getRootRef(), resourceClassRegistry
                    .getUriPatternForResourceClass(PageResource.class));
            link =
                new Link(Utils.formatUriTemplate(fullUri, Constants.WIKI_NAME_PARAMETER, doc.getWiki(),
                    Constants.SPACE_NAME_PARAMETER, doc.getSpace(), Constants.PAGE_NAME_PARAMETER, doc.getName()));
            link.setRel(Relations.PAGE);
            pageSummary.addLink(link);

            fullUri =
                String.format("%s%s", request.getRootRef(), resourceClassRegistry
                    .getUriPatternForResourceClass(SpaceResource.class));
            link =
                new Link(Utils.formatUriTemplate(fullUri, Constants.WIKI_NAME_PARAMETER, doc.getWiki(),
                    Constants.SPACE_NAME_PARAMETER, doc.getSpace()));
            link.setRel(Relations.SPACE);
            pageSummary.addLink(link);

            String parent = doc.getParent();
            if (parent != null && parent.indexOf('.') != -1) {
                pageSummary.setParent(doc.getParent());

                String[] components = doc.getParent().split("\\.");
                fullUri =
                    String.format("%s%s", request.getRootRef(), resourceClassRegistry
                        .getUriPatternForResourceClass(PageResource.class));
                link =
                    new Link(Utils.formatUriTemplate(fullUri, Constants.WIKI_NAME_PARAMETER, doc.getWiki(),
                        Constants.SPACE_NAME_PARAMETER, components[0], Constants.PAGE_NAME_PARAMETER, components[1]));
                link.setRel(Relations.PARENT);
                pageSummary.addLink(link);
            }

            return pageSummary;
        } catch (Exception e) {
            return null;
        }
    }

    public static Page createPage(Request request, XWikiResourceClassRegistry resourceClassRegistry, Document doc)
    {
        try {
            Page page = new Page();

            page.setWiki(doc.getWiki());
            page.setId(doc.getFullName());
            page.setFullId(doc.getPrefixedFullName());
            page.setSpace(doc.getSpace());
            page.setName(doc.getName());
            page.setTitle(doc.getTitle());
            page.setVersion(doc.getRCSVersion().at(0));
            page.setMinorVersion(doc.getRCSVersion().at(1));
            page.setLanguage(doc.getLanguage());
            page.setXWikiUrl(doc.getExternalURL("view"));
            page.setCreator(doc.getCreator());
            page.setCreated(doc.getCreationDate().getTime());
            page.setModifier(doc.getContentAuthor());
            page.setModified(doc.getContentUpdateDate().getTime());
            page.setContent(doc.getContent());

            Translations translations = page.getTranslations();

            List<String> languages = doc.getTranslationList();

            if (!languages.isEmpty()) {
                if (!doc.getDefaultLanguage().equals("")) {
                    translations.setDefaultTranslation(doc.getDefaultLanguage());
                }
            }

            String fullUri;
            Link link;

            for (String language : languages) {
                fullUri =
                    String.format("%s%s", request.getRootRef(), resourceClassRegistry
                        .getUriPatternForResourceClass(PageTranslationResource.class));
                link =
                    new Link(Utils.formatUriTemplate(fullUri, Constants.WIKI_NAME_PARAMETER, doc.getWiki(),
                        Constants.SPACE_NAME_PARAMETER, doc.getSpace(), Constants.PAGE_NAME_PARAMETER, doc.getName(),
                        Constants.LANGUAGE_ID_PARAMETER, language));
                link.setRel(Relations.TRANSLATION);
                link.setHrefLang(language);
                translations.addLink(link);
            }

            link = new Link(request.getResourceRef().getIdentifier());
            link.setRel(Relations.SELF);
            page.addLink(link);

            fullUri =
                String.format("%s%s", request.getRootRef(), resourceClassRegistry
                    .getUriPatternForResourceClass(SpaceResource.class));
            link = new Link(Utils.formatUriTemplate(fullUri, Constants.SPACE_NAME_PARAMETER, doc.getSpace()));
            link.setRel(Relations.SPACE);
            page.addLink(link);

            String parent = doc.getParent();
            if (parent != null && parent.indexOf('.') != -1) {
                page.setParent(doc.getParent());

                String[] components = doc.getParent().split("\\.");
                fullUri =
                    String.format("%s%s", request.getRootRef(), resourceClassRegistry
                        .getUriPatternForResourceClass(PageResource.class));
                link =
                    new Link(Utils.formatUriTemplate(fullUri, Constants.SPACE_NAME_PARAMETER, components[0],
                        Constants.PAGE_NAME_PARAMETER, components[1]));
                link.setRel(Relations.PARENT);
                page.addLink(link);
            }

            return page;
        } catch (Exception e) {
            return null;
        }
    }

    public static HistorySummary createHistorySummary(Request request,
        XWikiResourceClassRegistry resourceClassRegistry, String languageId, Object[] fields)
    {
        String pageId = (String) fields[0];

        String[] components = pageId.split("\\.");
        String spaceName = components[0];
        String pageName = components[1];

        XWikiRCSNodeId nodeId = (XWikiRCSNodeId) fields[1];
        int version = nodeId.getVersion().at(0);
        int minorVersion = nodeId.getVersion().at(1);

        Timestamp timestamp = (Timestamp) fields[2];
        String author = (String) fields[3];

        HistorySummary historySummary = new HistorySummary();
        historySummary.setPageId(pageId);
        historySummary.setModified(timestamp.getTime());
        historySummary.setModifier(author);
        historySummary.setVersion(version);
        historySummary.setMinorVersion(minorVersion);

        String fullUri = null;
        Link link = null;

        if (languageId != null) {
            fullUri =
                String.format("%s%s", request.getRootRef(), resourceClassRegistry
                    .getUriPatternForResourceClass(PageTranslationVersionResource.class));
            link =
                new Link(Utils.formatUriTemplate(fullUri, Constants.SPACE_NAME_PARAMETER, spaceName,
                    Constants.PAGE_NAME_PARAMETER, pageName, Constants.LANGUAGE_ID_PARAMETER, languageId,
                    Constants.VERSION_PARAMETER, String.format("%d.%d", version, minorVersion)));
        } else {
            fullUri =
                String.format("%s%s", request.getRootRef(), resourceClassRegistry
                    .getUriPatternForResourceClass(PageVersionResource.class));
            link =
                new Link(Utils.formatUriTemplate(fullUri, Constants.SPACE_NAME_PARAMETER, spaceName,
                    Constants.PAGE_NAME_PARAMETER, pageName, Constants.VERSION_PARAMETER, String.format("%d.%d",
                        version, minorVersion)));
        }

        historySummary.addLink(link);

        return historySummary;
    }

}
