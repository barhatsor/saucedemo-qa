package com.saucedemo.qa.tests;

import com.saucedemo.qa.core.BaseTest;
import com.saucedemo.qa.core.Config;
import com.saucedemo.qa.util.Money;
import com.saucedemo.qa.util.PriceSortValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Task 5: the reviewed and fixed version of OldTest (original in docs/original/OldTest.java).
 * Each "FIX #n" comment matches a finding in docs/CODE_REVIEW.md.
 *
 * FIX #1: no more "static WebDriver driver = new ChromeDriver()". BaseTest opens a new
 * browser in @BeforeMethod and always quits it in @AfterMethod.
 *
 * Locators are kept inline (no page objects) on purpose, so this file can be compared
 * line by line with the original.
 */
public class ReviewedTest extends BaseTest {

    private static final By USERNAME = By.id("user-name");               // FIX #2: id, not absolute XPath
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By INVENTORY_LIST = By.className("inventory_list");
    private static final By SORT_DROPDOWN = By.className("product_sort_container");
    private static final By PRICE = By.className("inventory_item_price");

    @Test(description = "After sorting by 'Price (low to high)' every price is <= the next one") // FIX #9: descriptive name
    public void sortByPriceLowToHighShowsAscendingPrices() {                                      // FIX #8: no try/catch, no 'throws Exception'
        driver.get(Config.BASE_URL);
        wait.until(visibilityOfElementLocated(USERNAME)).sendKeys(Config.STANDARD_USER);
        driver.findElement(PASSWORD).sendKeys(Config.PASSWORD);
        driver.findElement(LOGIN_BUTTON).click();

        // FIX #3: wait for the products list instead of Thread.sleep(5000)
        wait.until(visibilityOfElementLocated(INVENTORY_LIST));
        // FIX #10: confirm the login actually worked before testing the next feature
        assertTrue(driver.getCurrentUrl().endsWith("/inventory.html"),
                "Login failed, current URL: " + driver.getCurrentUrl());

        // FIX #4: sort FIRST, wait until the new sort is applied ...
        new Select(driver.findElement(SORT_DROPDOWN)).selectByValue("lohi");
        wait.until(d -> "lohi".equals(
                new Select(d.findElement(SORT_DROPDOWN)).getFirstSelectedOption().getDomAttribute("value")));

        // ... THEN read the prices that are displayed after sorting.
        // FIX #5: parse "$7.99" into a number (BigDecimal) instead of comparing Strings
        List<BigDecimal> prices = driver.findElements(PRICE).stream()
                .map(WebElement::getText)
                .map(Money::parse)
                .toList();

        // FIX #7: real assertions that fail the test, instead of println + assertTrue(true)
        assertEquals(prices.size(), 6, "Unexpected number of products: " + prices);
        // FIX #6: check every neighbouring pair, not only the first and last price
        assertTrue(PriceSortValidator.isSortedAscending(prices),
                "Prices are not sorted low to high: " + prices);
    }
}
