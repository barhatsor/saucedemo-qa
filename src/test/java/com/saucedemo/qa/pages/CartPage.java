package com.saucedemo.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CartPage {

    private static final By CART_LIST = By.className("cart_list");
    private static final By ITEM_NAME = By.cssSelector(".cart_item .inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/cart.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_LIST));
        return this;
    }

    public List<String> itemNames() {
        return driver.findElements(ITEM_NAME).stream().map(WebElement::getText).toList();
    }

    public CheckoutInfoPage checkout() {
        driver.findElement(CHECKOUT_BUTTON).click();
        return new CheckoutInfoPage(driver, wait).waitUntilLoaded();
    }
}
