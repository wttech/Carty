package com.cognifide.cq.carty.mapper;

import com.cognifide.cq.carty.Mapping;

public class AppliedMappingEntry {

    private final Mapping mapping;

    private final String matchingInternalRedirect;

    private final String url;

    public AppliedMappingEntry(Mapping mapping, String matchingInternalRedirect, String url) {
        this.mapping = mapping;
        this.matchingInternalRedirect = matchingInternalRedirect;
        this.url = url;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public String getMatchingInternalRedirect() {
        return matchingInternalRedirect;
    }

    public String getUrl() {
        return url.replaceFirst("^/([^/]+)/", "$1://");
    }
}
