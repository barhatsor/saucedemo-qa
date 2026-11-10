package com.saucedemo.qa.util;

import java.math.BigDecimal;
import java.util.List;

/**
 * Checks whether a list of prices is already sorted. It does not sort anything:
 * it walks the list once and compares each price with the one before it.
 * Equal neighbours are allowed (non-strict order), since two products may share a price.
 */
public final class PriceSortValidator {

    public enum Direction { ASCENDING, DESCENDING }

    private PriceSortValidator() {
    }

    public static boolean isSorted(List<BigDecimal> prices, Direction direction) {
        if (prices == null) {
            throw new IllegalArgumentException("prices must not be null");
        }
        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }
        if (prices.contains(null)) {
            throw new IllegalArgumentException("prices must not contain null values");
        }

        for (int i = 1; i < prices.size(); i++) {
            int cmp = prices.get(i - 1).compareTo(prices.get(i));
            boolean outOfOrder = (direction == Direction.ASCENDING) ? cmp > 0 : cmp < 0;
            if (outOfOrder) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSortedAscending(List<BigDecimal> prices) {
        return isSorted(prices, Direction.ASCENDING);
    }

    public static boolean isSortedDescending(List<BigDecimal> prices) {
        return isSorted(prices, Direction.DESCENDING);
    }
}
