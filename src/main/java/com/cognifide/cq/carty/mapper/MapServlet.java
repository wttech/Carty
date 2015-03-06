package com.cognifide.cq.carty.mapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.ServerException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
        final String mappingsRoot = request.getParameter("mappingsRoot");
        final CartyMapper cartyResolver = new CartyMapper(mappingsRoot, request.getResourceResolver());
        final MapperResult result = cartyResolver.map(path);

        final JsonObject json = new JsonObject();
        json.addProperty("url", result.getMappedUrl());
        final JsonArray mappings = new JsonArray();
        for (AppliedMappingEntry m : result.getMappings()) {
            final JsonObject n = new JsonObject();
            n.addProperty("url", m.getUrl());

            n.add("mapping", GSON.toJsonTree(m.getMapping()));
            final String[] matchingPath = new String[2];
            final int matchingInternalRedirectLength = m.getMatchingInternalRedirect().length();
            matchingPath[0] = path.substring(0, matchingInternalRedirectLength);
            matchingPath[1] = path.substring(matchingInternalRedirectLength);
            n.add("matchingPath", GSON.toJsonTree(matchingPath));
            mappings.add(n);
        }
        json.add("mappings", mappings);

        return json;
    };
}
