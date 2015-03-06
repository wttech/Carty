package com.cognifide.cq.carty.template;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@SlingServlet(methods = "POST", resourceTypes = "carty/components/cartyApi", selectors = "createMappings", extensions = "json")
public class CreateMappingsServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        final JsonObject payload = GSON.fromJson(request.getReader(), JsonObject.class);
        final String mappingsRoot = payload.get("mappingsRoot").getAsString();
        final String templateName = payload.get("template").getAsString();
        final Map<String, String> data = new LinkedHashMap<String, String>();
        for (Entry<String, JsonElement> e : payload.get("data").getAsJsonObject().entrySet()) {
            data.put(e.getKey(), e.getValue().getAsString());
        }
        final Resource template = request.getResource().getChild("../jcr:content/templates/" + templateName);
        createMappings(template, mappingsRoot, data);
        response.setStatus(200);
    }

    private void createMappings(Resource templateRoot, String mappingsRootPath, Map<String, String> data)
            throws PersistenceException {
        final ResourceResolver resolver = templateRoot.getResourceResolver();
        final Resource mappingsRoot = resolver.getResource(mappingsRootPath);
        for (final Resource r : templateRoot.getChildren()) {
            copy(r, mappingsRoot, data);
        }
        resolver.commit();
    }

    private static void copy(Resource src, Resource parent, Map<String, String> values)
            throws PersistenceException {
        final ResourceResolver resolver = src.getResourceResolver();
        final String name = fillPlaceholders(src.getName(), values);

        final Map<String, Object> props = new LinkedHashMap<String, Object>();
        for (Entry<String, Object> e : src.adaptTo(ValueMap.class).entrySet()) {
            final String key = fillPlaceholders(e.getKey(), values);
            final Object value = fillPlaceholders(e.getValue(), values);
            props.put(key, value);
        }

        Resource dst = parent.getChild(name);
        if (dst == null) {
            dst = resolver.create(parent, name, props);
        }
        for (final Resource child : src.getChildren()) {
            copy(child, dst, values);
        }
    }

    private static Object fillPlaceholders(Object target, Map<String, String> values) {
        if (target instanceof String) {
            return fillPlaceholders((String) target, values);
        } else if (target instanceof String[]) {
            final String[] src = (String[]) target;
            final String[] dst = new String[src.length];
            int i = 0;
            for (final String text : src) {
                dst[i++] = fillPlaceholders(text, values);
            }
            return dst;
        } else {
            return target;
        }
    }

    private static String fillPlaceholders(String text, Map<String, String> values) {
        return StrSubstitutor.replace(text, values, "_", "_");
    }
}
