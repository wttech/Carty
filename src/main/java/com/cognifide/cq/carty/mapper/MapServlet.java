package com.cognifide.cq.carty.mapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.ServerException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import com.cognifide.cq.carty.CartyStringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static com.cognifide.cq.carty.CartyStringUtils.multiSubstring;

@SlingServlet(methods = "GET", resourceTypes = "carty/components/cartyApi", selectors = "map", extensions = "json")
public class MapServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = -4571406107865187279L;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        final JsonElement json;
        try {
            json = map(request);
        } catch (URISyntaxException e) {
            throw new ServerException("Can't parse path", e);
        }

        response.setContentType("application/json");
        response.getWriter().print(GSON.toJson(json));
    }

    private JsonElement map(SlingHttpServletRequest request) throws URISyntaxException {
        final String path = request.getParameter("path");
        final String host = request.getParameter("host");
        final String mappingsRoot = request.getParameter("mappingsRoot");
        final CartyMapper cartyResolver = new CartyMapper(mappingsRoot, request.getResourceResolver());
        final MapperResult result = cartyResolver.map(path, prepareUrlPrefix(host));

        final JsonObject json = new JsonObject();
        json.addProperty("url", getNiceUrl(result.getMappedUrl()));
        final JsonArray mappings = new JsonArray();

        int i = 0;
        for (AppliedMappingEntry m : result.getMappings()) {
            final JsonObject n = new JsonObject();
            final String urlPart = m.getMapping().getUrlSegment();
            final String[] splitUrl = multiSubstring(result.getMappedUrl(), i, i + urlPart.length());
            i += urlPart.length() + 1; // "1" for the slash

            n.add("url", GSON.toJsonTree(splitUrl));
            n.add("mapping", GSON.toJsonTree(m.getMapping()));
            if (m.getMatchingInternalRedirect() != null) {
                final String[] splitPath = multiSubstring(path, m.getMatchingInternalRedirect().length());
                n.add("matchingPath", GSON.toJsonTree(splitPath));
            }
            mappings.add(n);
        }
        json.add("mappings", mappings);
        return json;
    }

    private String prepareUrlPrefix(String host) throws URISyntaxException {
        final StringBuilder prefix = new StringBuilder();
        if (StringUtils.isBlank(host)) {
            return null;
        }
        if (!host.startsWith("http://") && !host.startsWith("https://")) {
            prefix.append("http://");
        }
        prefix.append(host);
        final int lastChar = prefix.length() - 1;
        if ("/".equals(prefix.substring(lastChar))) {
            prefix.deleteCharAt(lastChar);
        }
        return CartyStringUtils.urlToMappingForm(prefix.toString());
    };

    private static String getNiceUrl(String url) {
        return url.replaceFirst("^(https?)/(.+)\\$?$", "$1://$2");
    }
}
