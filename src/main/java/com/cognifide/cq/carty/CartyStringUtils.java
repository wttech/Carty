package com.cognifide.cq.carty;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public final class CartyStringUtils {
    private CartyStringUtils() {
    }

    public static String[] multiSubstring(final String string, final int... indices) {
        final List<Integer> closedIndices = new ArrayList<>();
        closedIndices.add(0);
        for (final int i : indices) {
            closedIndices.add(i);
        }
        closedIndices.add(string.length());

        final String[] result = new String[closedIndices.size() - 1];
        for (int i = 0; i < result.length; i++) {
            final int from = closedIndices.get(i);
            final int to = closedIndices.get(i + 1);
            result[i] = string.substring(from, to);
        }
        return result;
    }

    public static String urlToMappingForm(String url) throws URISyntaxException {
        final URI uri = new URI(url);
        if (uri.getScheme() == null) {
            return url;
        }

        final StringBuilder builder = new StringBuilder();
        builder.append(uri.getScheme());
        builder.append('/');
        builder.append(uri.getHost());
        final int port = getPort(uri);
        if (port != -1) {
            builder.append('.').append(port);
        }
        builder.append(uri.getPath());
        return builder.toString();
    }

    private static int getPort(URI uri) {
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equals(uri.getScheme())) {
                port = 80;
            } else if ("https".equals(uri.getScheme())) {
                port = 443;
            }
        }
        return port;
    }

}
