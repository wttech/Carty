package com.cognifide.cq.carty.resolver;

import java.util.List;

import org.apache.sling.api.resource.Resource;

public class ResolverResult {
    private final List<AppliedResolutionEntry> appliedMappings;

    private final String parsedUrl;

    private transient final Resource resource;

    public ResolverResult(List<AppliedResolutionEntry> appliedMappings, String parsedUrl, Resource resource) {
        this.appliedMappings = appliedMappings;
        this.parsedUrl = parsedUrl;
        this.resource = resource;
    }

    public List<AppliedResolutionEntry> getAppliedMappings() {
        return appliedMappings;
    }

    public String getParsedUrl() {
        return parsedUrl;
    }

    public Resource getResource() {
        return resource;
    }
}
