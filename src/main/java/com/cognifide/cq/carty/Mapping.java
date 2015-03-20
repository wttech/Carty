package com.cognifide.cq.carty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class Mapping {

    private static final String[] SUPPORTED_SCHEME = new String[] { "http", "https" };

    private final String path;

    private final String name;

    private final String match;

    private final String[] internalRedirect;

    private final String redirect;

    private final boolean schemeMapping;

    private final boolean domainMapping;

    private final int domainPort;

    private transient final Resource resource;

    public Mapping(final Resource resource) {
        final ValueMap map = resource.adaptTo(ValueMap.class);
        this.path = resource.getPath();
        this.name = resource.getName();
        this.match = map.get("sling:match", String.class);
        this.internalRedirect = map.get("sling:internalRedirect", String[].class);
        this.redirect = map.get("sling:redirect", String.class);
        this.schemeMapping = ArrayUtils.contains(SUPPORTED_SCHEME, resource.getName());

        final String parentName = resource.getParent().getName();
        if (ArrayUtils.contains(SUPPORTED_SCHEME, parentName)) {
            this.domainMapping = true;
            if ("http".equals(parentName)) {
                domainPort = 80;
            } else if ("https".equals(parentName)) {
                domainPort = 443;
            } else {
                domainPort = -1;
            }
        } else {
            domainMapping = false;
            domainPort = -1;
        }
        this.resource = resource;
    }

    public Matcher matcher(String uri) {
        final StringBuilder regexp = new StringBuilder("/?");
        if (StringUtils.isBlank(getMatch())) {
            regexp.append(Pattern.quote(name));
            if (domainPort != -1 && !name.matches(".+\\.\\d+$")) {
                regexp.append("\\." + domainPort);
            }
        } else {
            regexp.append(match);
        }
        final Pattern pattern = Pattern.compile(regexp.toString());
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

    public boolean isDomainMapping() {
        return domainMapping;
    }

    public Resource getResource() {
        return resource;
    }

    public String getUrlSegment() {
        return domainMapping ? getDomainNameWithPort() : getMatchOrDomain();
    }

    public String getMatchOrDomain() {
        return StringUtils.defaultIfEmpty(match, name);
    }

    public String getDomainNameWithPort() {
        final String domainName = getMatchOrDomain();
        if (domainName.matches(".+\\.\\d+$") || domainPort == -1) {
            return domainName;
        } else {
            return domainName + "." + domainPort;
        }
    }

    public boolean isFinal() {
        return getMatchOrDomain().endsWith("$");
    }
}
