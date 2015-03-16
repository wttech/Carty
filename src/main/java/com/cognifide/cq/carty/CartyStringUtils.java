package com.cognifide.cq.carty;

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
}
