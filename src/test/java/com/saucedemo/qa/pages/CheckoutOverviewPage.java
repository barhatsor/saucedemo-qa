package com.saucedemo.qa.pages;

import com.saucedemo.qa.util.Money;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.util.List;

/** Checkout step two: order summary with item total, tax and total. */
public class CheckoutOverviewPage {

    private static final By ITEM_NAME = By.cssSelector(".cart_item .inventory_item_name");
    private static final By ITEM_TOTAL = By.className("summary_subtotal_label");
    private static final By TAX = By.className("summary_tax_label");
    private static final By TOTAL = By.className("summary_total_label");
    private static final By FINISH_BUTTON = By.id("finish");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CheckoutOverviewPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public CheckoutOverviewPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(TOTAL));
        return this;
    }

    public List<String> itemNames() {
        return driver.findElements(ITEM_NAME).stream().map(WebElement::getText).toList();
    }

    /** "Item total: $7.99" -> 7.99 */
    public BigDecimal itemTotal() {
        return Money.parse(driver.findElement(ITEM_TOTAL).getText());
    }

    /** "Tax: $0.64" -> 0.64 */
    public BigDecimal tax() {
        return Money.parse(driver.findElement(TAX).getText());
    }

    /** "Total: $8.63" -> 8.63 */
    public BigDecimal total() {
        return Money.parse(driver.findElement(TOTAL).getText());
    }

    public CheckoutCompletePage finish() {
        driver.findElement(FINISH_BUTTON).click();
        return new CheckoutCompletePage(driver, wait).waitUntilLoaded();
    }
}
