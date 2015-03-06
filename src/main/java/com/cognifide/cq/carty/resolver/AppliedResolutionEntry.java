package com.cognifide.cq.carty.resolver;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;

import com.cognifide.cq.carty.Mapping;

public class AppliedResolutionEntry {
    private final Mapping mapping;

    private final int from;

    private final int to;

    private transient final String[] groups;

    private transient final String[] searchList;

    public AppliedResolutionEntry(Mapping mapping, int from, int to, String[] groups) {
        this.mapping = mapping;
        this.from = from;
        this.to = to;
        this.groups = groups;

        searchList = new String[groups.length];
        for (int i = 0; i < searchList.length; i++) {
            searchList[i] = String.format("$%d", i + 1);
        }
    }

    public Mapping getMapping() {
        return mapping;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public Resource getResource(String relPath, ResourceResolver resolver) {
        Resource res = null;

        for (final String r : mapping.getInternalRedirect()) {
            final String path = fillPattern(r) + relPath;
            res = resolver.resolve(path);
            if (!ResourceUtil.isNonExistingResource(res)) {
                return res;
            }
        }
        return res;
    }

    private String fillPattern(String subject) {
        return StringUtils.replaceEach(subject, searchList, groups);
    }
}
