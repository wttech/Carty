package com.cognifide.cq.carty.mapper;

import com.cognifide.cq.carty.Mapping;

public class AppliedMappingEntry {

    private final Mapping mapping;

    private final String matchingInternalRedirect;

    private final String url;

    private final AppliedMappingEntry parent;

    public AppliedMappingEntry(Mapping mapping, String matchingInternalRedirect, String url,
            AppliedMappingEntry parent) {
        this.mapping = mapping;
        this.matchingInternalRedirect = matchingInternalRedirect;
        this.url = url;
        this.parent = parent;
    }

    public AppliedMappingEntry(Mapping mapping, String url, AppliedMappingEntry parent) {
        this(mapping, null, url, parent);
    }

    public Mapping getMapping() {
        return mapping;
    }

    public String getMatchingInternalRedirect() {
        return matchingInternalRedirect;
    }

    public String getUrl() {
        return url;
    }

    public AppliedMappingEntry getParent() {
        return parent;
    }
}
