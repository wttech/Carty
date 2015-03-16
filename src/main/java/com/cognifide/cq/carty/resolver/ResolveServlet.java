package com.cognifide.cq.carty.resolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.ServerException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static com.cognifide.cq.carty.CartyStringUtils.multiSubstring;

@SlingServlet(methods = "GET", resourceTypes = "carty/components/cartyApi", selectors = "resolve", extensions = "json")
public class ResolveServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = -4571406107865187279L;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        final JsonElement json;
        try {
            json = resolve(request);
        } catch (URISyntaxException e) {
            throw new ServerException("Can't parse path", e);
        }

        response.setContentType("application/json");
        response.getWriter().print(GSON.toJson(json));
    }

    private JsonElement resolve(SlingHttpServletRequest request) throws URISyntaxException {
        final String url = request.getParameter("url");
        final String mappingsRoot = request.getParameter("mappingsRoot");
        final CartyResolver cartyResolver = new CartyResolver(mappingsRoot, request.getResourceResolver());
        final ResolverResult result = cartyResolver.resolve(url);

        final JsonObject json = new JsonObject();
        final Resource resource = result.getResource();
        if (resource != null) {
            json.addProperty("resourcePath", resource.getPath());
            json.addProperty("resourceType", resource.getResourceType());
            json.addProperty("resourceSuperType", resource.getResourceSuperType());
            json.addProperty("class", resource.getClass().getName());
        }

        final JsonArray mappings = new JsonArray();
        final String parsedUrl = result.getParsedUrl();
        for (AppliedResolutionEntry m : result.getAppliedMappings()) {
            final JsonObject n = new JsonObject();
            n.add("mapping", GSON.toJsonTree(m.getMapping()));

            final String[] path = multiSubstring(parsedUrl, m.getFrom(), m.getTo());
            n.add("path", GSON.toJsonTree(path));
            mappings.add(n);
        }
        json.add("mappings", mappings);

        return json;
    };
}
