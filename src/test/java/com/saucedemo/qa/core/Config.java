package com.saucedemo.qa.core;

import java.time.Duration;

/**
 * Central place for environment settings and shared test data.
 * Values can be overridden from the command line with -D system properties.
 */
public final class Config {

    private Config() {
    }

    public static final String BASE_URL = System.getProperty("baseUrl", "https://www.saucedemo.com/");

    /** Run Chrome without a visible window: mvn test -Dheadless=true */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }

    public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(10);

    // Test users published on the SauceDemo login page
    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_OUT_USER = "locked_out_user";
    public static final String PASSWORD = "secret_sauce";
}
