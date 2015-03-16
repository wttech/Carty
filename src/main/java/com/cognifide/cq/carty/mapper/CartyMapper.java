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

    private final Mapping root;

    public CartyMapper(String mappingsRoot, ResourceResolver resourceResolver) {
        this.root = new Mapping(resourceResolver.getResource(mappingsRoot));
    }

    public MapperResult map(String path, String urlPrefix) {
        final List<AppliedMappingEntry> mappings = map(path, new AppliedMappingEntry(root, "", null));
        if (mappings.isEmpty()) {
            return new MapperResult(path, mappings);
        }

        final List<AppliedMappingEntry> filtered;
        if (StringUtils.isBlank(urlPrefix)) {
            filtered = mappings;
        } else {
            filtered = filter(mappings, urlPrefix);
        }
        Collections.sort(filtered, new Comparator<AppliedMappingEntry>() {
            @Override
            public int compare(AppliedMappingEntry o1, AppliedMappingEntry o2) {
                return o2.getMatchingInternalRedirect().length() - o1.getMatchingInternalRedirect().length();
            }
        });

        final AppliedMappingEntry appliedMapping = mappings.get(0);
        final List<AppliedMappingEntry> ancestors = getAncestors(appliedMapping);
        Collections.reverse(ancestors);
        return new MapperResult(appliedMapping.getUrl(), ancestors);
    }

    private List<AppliedMappingEntry> getAncestors(AppliedMappingEntry appliedMapping) {
        final List<AppliedMappingEntry> ancestors = new ArrayList<AppliedMappingEntry>();
        AppliedMappingEntry currentMapping = appliedMapping;
        do {
            ancestors.add(currentMapping);
            currentMapping = currentMapping.getParent();
        } while (currentMapping.getMapping() != root);
        return ancestors;
    }

    private List<AppliedMappingEntry> filter(List<AppliedMappingEntry> mappings, String urlPrefix) {
        final List<AppliedMappingEntry> filtered = new ArrayList<>();
        for (final AppliedMappingEntry e : mappings) {
            if (e.getUrl().startsWith(urlPrefix)) {
                filtered.add(e);
            }
        }
        if (filtered.isEmpty()) {
            filtered.addAll(mappings);
        }
        return filtered;
    }

    public List<AppliedMappingEntry> map(String path, AppliedMappingEntry parent) {
        final List<AppliedMappingEntry> list = new ArrayList<AppliedMappingEntry>();
        for (final Resource resource : parent.getMapping().getResource().getChildren()) {
            final Mapping mapping = new Mapping(resource);
            final String url = getUrl(mapping, parent.getUrl());
            if (isValidMapping(mapping)) {
                list.addAll(findMatchingRedirects(path, mapping, url, parent));
            }
            if (!url.endsWith("$")) {
                final AppliedMappingEntry currentMapping = new AppliedMappingEntry(mapping, url, parent);
                list.addAll(map(path, currentMapping));
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
        if (StringUtils.isBlank(urlPrefix)) {
            return urlSuffix;
        } else {
            return String.format("%s/%s", urlPrefix, urlSuffix);
        }
    }

    private List<AppliedMappingEntry> findMatchingRedirects(final String path, final Mapping mapping,
            final String url, final AppliedMappingEntry parent) {
        final List<AppliedMappingEntry> list = new ArrayList<AppliedMappingEntry>();
        final boolean isFinal = url.endsWith("$");

        for (final String internalRedirect : mapping.getInternalRedirect()) {
            String fullUrl = null;
            if (path.equals(internalRedirect)) {
                fullUrl = url;
            } else if (!isFinal && path.startsWith(internalRedirect)) {
                fullUrl = url + path.substring(internalRedirect.length());
            }
            if (fullUrl != null) {
                list.add(new AppliedMappingEntry(mapping, internalRedirect, fullUrl, parent));
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
