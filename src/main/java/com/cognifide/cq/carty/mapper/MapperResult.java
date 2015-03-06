package com.cognifide.cq.carty.mapper;

import java.util.List;

public class MapperResult {

    private final String mappedUrl;

    private final List<AppliedMappingEntry> mappings;

    public MapperResult(String mappedUrl, List<AppliedMappingEntry> mappings) {
        this.mappedUrl = mappedUrl;
        this.mappings = mappings;
    }

    public String getMappedUrl() {
        return mappedUrl;
    }

    public List<AppliedMappingEntry> getMappings() {
        return mappings;
    }

}
