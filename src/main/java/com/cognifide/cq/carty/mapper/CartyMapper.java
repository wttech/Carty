package com.cognifide.cq.carty.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.cq.carty.Mapping;

public class CartyMapper {

    private final String mappingsRoot;

    private final ResourceResolver resolver;

    public CartyMapper(String mappingsRoot, ResourceResolver resourceResolver) {
        this.mappingsRoot = mappingsRoot;
        this.resolver = resourceResolver;
    }

    public MapperResult map(String path) {
        List<AppliedMappingEntry> mappings = map(path, resolver.getResource(mappingsRoot), "");
        Collections.sort(mappings, new Comparator<AppliedMappingEntry>() {
            @Override
            public int compare(AppliedMappingEntry o1, AppliedMappingEntry o2) {
                return o2.getMatchingInternalRedirect().length() - o1.getMatchingInternalRedirect().length();
            }
        });

        final String url;
        if (mappings.isEmpty()) {
            url = path;
        } else {
            url = mappings.get(0).getUrl();
        }
        return new MapperResult(url, mappings);
    }

    public List<AppliedMappingEntry> map(String path, Resource parentResource, String urlPrefix) {
        final List<AppliedMappingEntry> list = new ArrayList<AppliedMappingEntry>();
        for (final Resource resource : parentResource.getChildren()) {
            final Mapping mapping = new Mapping(resource);
            final String url = getUrl(mapping, urlPrefix);
            if (isValidMapping(mapping)) {
                list.addAll(findMatchingRedirects(path, mapping, url));
            }
            if (!url.endsWith("$")) {
                list.addAll(map(path, resource, url));
            }
        }
        return list;
    }

    private String getUrl(Mapping mapping, String urlPrefix) {
        final String urlSuffix;

        if (StringUtils.isBlank(mapping.getMatch())) {
            urlSuffix = mapping.getName();
        } else {
            urlSuffix = mapping.getMatch();
        }
        return String.format("%s/%s", urlPrefix, urlSuffix);
    }

    private List<AppliedMappingEntry> findMatchingRedirects(final String path, final Mapping mapping,
            final String url) {
        final List<AppliedMappingEntry> list = new ArrayList<AppliedMappingEntry>();
        final boolean isFinal = url.endsWith("$");
        final String urlWithNoDollar = StringUtils.removeEnd(url, "$");

        for (final String internalRedirect : mapping.getInternalRedirect()) {
            String fullUrl = null;
            if (isFinal && path.equals(internalRedirect)) {
                fullUrl = urlWithNoDollar;
            } else if (path.startsWith(internalRedirect)) {
                fullUrl = urlWithNoDollar + path.substring(internalRedirect.length());
            }
            if (fullUrl != null) {
                list.add(new AppliedMappingEntry(mapping, internalRedirect, fullUrl));
            }
        }
        return list;
    }

    private static boolean isValidMapping(Mapping mapping) {
        if (StringUtils.isNotBlank(mapping.getRedirect())) {
            return false;
        }
        if (mapping.getMatch() != null && isRegExp(mapping.getMatch())) {
            return false;
        }
        if (mapping.getInternalRedirect() == null) {
            return false;
        }
        return true;
    }

    private static boolean isRegExp(final String string) {
        for (int i = 0; i < string.length(); i++) {
            final char c = string.charAt(i);
            if (c == '\\') {
                i++; // just skip
            } else if ("+*?|()[]".indexOf(c) >= 0) {
                return true; // assume an unescaped pattern character
            }
        }
        return false;
    }

}
