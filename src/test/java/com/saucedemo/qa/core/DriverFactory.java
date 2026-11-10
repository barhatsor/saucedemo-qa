package com.saucedemo.qa.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates a fresh ChromeDriver per test.
 * The matching chromedriver binary is resolved automatically by Selenium Manager (Selenium 4.6+).
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        System.out.println("Starting Chrome (headless=" + Config.isHeadless() + ")");
        if (Config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        // A fixed viewport keeps layout identical between headed and headless runs
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-first-run", "--no-default-browser-check");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--disable-notifications");

        // SauceDemo's public password triggers Chrome's "change your password" leak popup,
        // which blocks clicks after login. Turn the password manager off for the test profile.
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-features=PasswordLeakDetection,PasswordCheck");

        return new ChromeDriver(options);
    }

    /** Quits the driver without letting a teardown error hide the real test result. */
    public static void quitQuietly(WebDriver driver) {
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } catch (RuntimeException e) {
            System.err.println("Warning: failed to quit WebDriver cleanly: " + e.getMessage());
        }
    }
}
