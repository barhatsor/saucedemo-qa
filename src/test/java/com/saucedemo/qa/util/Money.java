package com.saucedemo.qa.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses prices shown on the site ("$29.99", "Item total: $29.99", "Tax: $2.40").
 * Uses BigDecimal, never double, so money comparisons are exact.
 */
public final class Money {

    private static final Pattern AMOUNT = Pattern.compile("-?\\d+(?:\\.\\d+)?");

    private Money() {
    }

    public static BigDecimal parse(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Price text must not be null");
        }
        Matcher m = AMOUNT.matcher(text.replace(",", ""));
        if (!m.find()) {
            throw new IllegalArgumentException("No amount found in: '" + text + "'");
        }
        return new BigDecimal(m.group());
    }
}
