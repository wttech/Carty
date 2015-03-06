package com.cognifide.cq.carty.resolver;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.ArrayUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.cq.carty.Mapping;

public class CartyResolver {

    private final String mappingsRoot;

    private final ResourceResolver resolver;

    public CartyResolver(final String mappingsRoot, final ResourceResolver resolver) {
        this.mappingsRoot = mappingsRoot;
        this.resolver = resolver;
    }

    public ResolverResult resolve(String url) {
        final Resource mapping = resolver.getResource(mappingsRoot);
        final URI uri;
        final String uriToParse;
        try {
            uri = new URI(url);
            if (uri.getScheme() == null) {
                uriToParse = null;
            } else {
                uriToParse = String.format("%s/%s%s", uri.getScheme(), uri.getHost(), uri.getPath());
            }
        } catch (URISyntaxException e) {
            return null;
        }

        final List<AppliedResolutionEntry> applied = new ArrayList<AppliedResolutionEntry>();

        if (uriToParse != null) {
            applied.addAll(resolve(uriToParse, 0, mapping));
        }

        final AppliedResolutionEntry lastMapping = getLastValidEntry(applied);
        final Resource resource;
        if (lastMapping == null) {
            resource = resolver.resolve(uri.getPath());
        } else {
            final String relPath = uriToParse.substring(lastMapping.getTo());
            resource = lastMapping.getResource(relPath, resolver);
        }

        return new ResolverResult(applied, uriToParse, resource);
    }

    private AppliedResolutionEntry getLastValidEntry(List<AppliedResolutionEntry> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            AppliedResolutionEntry entry = list.get(i);
            if (ArrayUtils.isNotEmpty(entry.getMapping().getInternalRedirect())) {
                return entry;
            }
        }
        return null;
    }

    private List<AppliedResolutionEntry> resolve(String uri, int from, Resource parentResource) {
        final List<AppliedResolutionEntry> result = new ArrayList<AppliedResolutionEntry>();
        for (final Resource child : parentResource.getChildren()) {
            final Mapping mapping = new Mapping(child);
            final Matcher matcher = mapping.matcher(uri).region(from, uri.length());
            if (!matcher.lookingAt()) {
                continue;
            }

            final List<AppliedResolutionEntry> list = resolve(uri, matcher.end(), child);
            final String[] groups = new String[matcher.groupCount()];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i);
            }

            result.add(new AppliedResolutionEntry(mapping, matcher.start(), matcher.end(), groups));
            result.addAll(list);
            break;
        }
        return result;
    }
}
