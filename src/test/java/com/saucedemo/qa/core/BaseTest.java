package com.saucedemo.qa.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Every UI test gets its own browser: opened in @BeforeMethod, closed in @AfterMethod.
 * This keeps tests independent of each other (no shared cookies, cart or session).
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void openBrowser() {
        driver = DriverFactory.createChromeDriver();
        wait = new WebDriverWait(driver, Config.EXPLICIT_WAIT);
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        DriverFactory.quitQuietly(driver);
        driver = null;
        wait = null;
    }
}
