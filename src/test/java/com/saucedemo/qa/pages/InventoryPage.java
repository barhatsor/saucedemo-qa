package com.saucedemo.qa.pages;

import com.saucedemo.qa.util.Money;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class InventoryPage {

    private static final By PAGE_TITLE = By.cssSelector("[data-test='title']");
    private static final By INVENTORY_LIST = By.className("inventory_list");
    private static final By ITEM = By.className("inventory_item");
    private static final By ITEM_NAME = By.className("inventory_item_name");
    private static final By ITEM_PRICE = By.className("inventory_item_price");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector("button[data-test^='add-to-cart']");
    private static final By SORT_DROPDOWN = By.className("product_sort_container");
    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By CART_BADGE = By.className("shopping_cart_badge");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public InventoryPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public InventoryPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(INVENTORY_LIST));
        return this;
    }

    public String title() {
        return driver.findElement(PAGE_TITLE).getText();
    }

    public InventoryPage sortBy(SortOption option) {
        new Select(driver.findElement(SORT_DROPDOWN)).selectByValue(option.value());
        // The sort re-renders the list, so wait until the dropdown reports the new value
        wait.until(d -> option.value().equals(
                new Select(d.findElement(SORT_DROPDOWN)).getFirstSelectedOption().getDomAttribute("value")));
        return this;
    }

    /** Prices of all products, in the order they are displayed. Read fresh on every call. */
    public List<BigDecimal> prices() {
        return driver.findElements(ITEM_PRICE).stream()
                .map(WebElement::getText)
                .map(Money::parse)
                .toList();
    }

    /** Name and price of every product currently displayed. */
    public List<Product> products() {
        return driver.findElements(ITEM).stream()
                .map(item -> new Product(
                        item.findElement(ITEM_NAME).getText(),
                        Money.parse(item.findElement(ITEM_PRICE).getText())))
                .toList();
    }

    /** Finds the cheapest product by reading every price on the page (nothing is assumed). */
    public Product cheapestProduct() {
        return products().stream()
                .min(Comparator.comparing(Product::price))
                .orElseThrow(() -> new IllegalStateException("No products are displayed"));
    }

    public InventoryPage addToCart(String productName) {
        WebElement item = driver.findElements(ITEM).stream()
                .filter(i -> i.findElement(ITEM_NAME).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productName));
        item.findElement(ADD_TO_CART_BUTTON).click();
        return this;
    }

    public int cartItemCount() {
        List<WebElement> badge = driver.findElements(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText());
    }

    public CartPage openCart() {
        driver.findElement(CART_LINK).click();
        return new CartPage(driver, wait).waitUntilLoaded();
    }
}
