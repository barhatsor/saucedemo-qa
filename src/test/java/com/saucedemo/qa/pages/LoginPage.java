package com.saucedemo.qa.pages;

import com.saucedemo.qa.core.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public LoginPage open() {
        driver.get(Config.BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME));
        return this;
    }

    /** Fills the form and submits. Use when the outcome (success or error) is checked by the caller. */
    public void submit(String username, String password) {
        driver.findElement(USERNAME).clear();
        driver.findElement(USERNAME).sendKeys(username);
        driver.findElement(PASSWORD).clear();
        driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
    }

    /** Logs in and waits for the products page. */
    public InventoryPage loginAs(String username, String password) {
        submit(username, password);
        return new InventoryPage(driver, wait).waitUntilLoaded();
    }

    public String errorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)).getText();
    }

    public boolean isLoginButtonDisplayed() {
        return !driver.findElements(LOGIN_BUTTON).isEmpty()
                && driver.findElement(LOGIN_BUTTON).isDisplayed();
    }
}
