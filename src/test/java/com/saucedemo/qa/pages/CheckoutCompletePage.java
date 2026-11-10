package com.saucedemo.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutCompletePage {

    private static final By COMPLETE_HEADER = By.className("complete-header");
    private static final By CART_BADGE = By.className("shopping_cart_badge");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CheckoutCompletePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public CheckoutCompletePage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(COMPLETE_HEADER));
        return this;
    }

    public String header() {
        return driver.findElement(COMPLETE_HEADER).getText();
    }

    public boolean isCartEmpty() {
        return driver.findElements(CART_BADGE).isEmpty();
    }
}
