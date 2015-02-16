package uk.me.thega.controller;

import org.codehaus.jettison.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.me.thega.model.repository.Application;
import uk.me.thega.model.repository.Family;
import uk.me.thega.model.repository.Resource;
import uk.me.thega.model.repository.Version;
import uk.me.thega.model.util.MetadataHelper;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(UrlMappings.ROOT_LIST)
public class ListController extends AbstractController {

    /**
     * Get the URL of the request with a given relative appended on the end.
     * <p/>
     * Eg. http://example.org/server/a/b/c.json with a relative of d.war becomes<br>
     * http://example.org/server/a/b/c/d.war
     *
     * @param req the HTTP request object.
     * @param relative the relative resource.
     *
     * @return the new URL.
     */
    private static String getURL(final HttpServletRequest req, final String relative) {
        final StringBuilder url = new StringBuilder();

        final String contextPath = req.getContextPath(); // /mywebapp
        final String servletPath = req.getServletPath(); // /servlet/MyServlet
        final String pathInfo = req.getPathInfo(); // /a/b;c=123

        url.append(contextPath).append(servletPath);
        if (pathInfo != null) {
            url.append(pathInfo);
        }
        final String urlString = url.toString();
        if (urlString.endsWith(".json")) {
            final CharSequence c = urlString.subSequence(0, urlString.length() - 5);
            return c.toString() + '/' + relative + ".json";
        } else if (urlString.endsWith("/")) {
            final CharSequence c = urlString.subSequence(0, urlString.length() - 1);
            return c.toString() + '/' + relative;
        } else {
            return urlString + '/' + relative;
        }
    }

    @RequestMapping(value = UrlMappings.PRODUCT, method = RequestMethod.GET)
    @ResponseBody
    public String browseApplicationGet(final HttpServletRequest request, @PathVariable final String family, @PathVariable final String application) throws IOException {
        final List<Version> versions;
        if (application.equals("all")) {
            versions = getRepository().allVersions(family);
        } else {
            versions = getRepository().versions(family, application);
        }

        final List<String> list = new ArrayList<String>();
        for (final Version version : versions) {
            final String name = version.getName();
            if (!list.contains(name)) {
                list.add(getURL(request, name));
            }
        }

        return new JSONArray(list).toString();
    }

    @RequestMapping(value = UrlMappings.FAMILY, method = RequestMethod.GET)
    @ResponseBody
    public String browseFamilyGet(final HttpServletRequest request, @PathVariable final String family) throws IOException, JAXBException {
        final List<String> list = new ArrayList<String>();
        for (final Application application : getRepository().applications(family)) {
            list.add(getURL(request, application.getName()));
        }

        return new JSONArray(list).toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String browseGet() throws IOException {
        final List<String> list = new ArrayList<String>();
        for (final Family family : getRepository().families()) {
            list.add(family.getName());
        }
        return new JSONArray(list).toString();
    }

    @RequestMapping(value = UrlMappings.VERSION, method = RequestMethod.GET)
    @ResponseBody
    public String browseVersionGet(final HttpServletRequest request, @PathVariable final String family, @PathVariable final String application, @PathVariable final String version) throws IOException {
        final List<Resource> resources;
        if (application.equals("all")) {
            resources = getRepository().allResources(family, version);
        } else {
            resources = getRepository().resources(family, application, version);
        }

        final List<String> list = new ArrayList<String>();
        final List<String> excluded = MetadataHelper.excludedFiles();
        for (final Resource resource : resources) {
            if (!excluded.contains(resource.getName()) && resource.isFile()) {
                list.add(getURL(request, resource.getName()).replace(UrlMappings.ROOT_LIST + "/", UrlMappings.ROOT_DOWNLOAD + "/"));
            }
        }
        return new JSONArray(list).toString();
    }

}
