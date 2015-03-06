package com.cognifide.cq.carty;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class Mapping {

    private final String path;

    private final String name;

    private final String match;

    private final String[] internalRedirect;

    private final String redirect;

    private final boolean schemeMapping;

    public Mapping(final Resource resource) {
        final ValueMap map = resource.adaptTo(ValueMap.class);
        this.path = resource.getPath();
        this.name = resource.getName();
        this.match = map.get("sling:match", String.class);
        this.internalRedirect = map.get("sling:internalRedirect", String[].class);
        this.redirect = map.get("sling:redirect", String.class);
        this.schemeMapping = Arrays.asList("http", "https").contains(resource.getName());
    }

    public Matcher matcher(String uri) {
        final Pattern pattern = Pattern.compile("/?" + StringUtils.defaultString(match, name));
        return pattern.matcher(uri);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getMatch() {
        return match;
    }

    public String[] getInternalRedirect() {
        return internalRedirect;
    }

    public String getRedirect() {
        return redirect;
    }

    public boolean isSchemeMapping() {
        return schemeMapping;
    }
}
