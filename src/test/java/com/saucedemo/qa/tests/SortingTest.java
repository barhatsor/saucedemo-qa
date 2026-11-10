package com.saucedemo.qa.tests;

import com.saucedemo.qa.core.BaseTest;
import com.saucedemo.qa.core.Config;
import com.saucedemo.qa.pages.InventoryPage;
import com.saucedemo.qa.pages.LoginPage;
import com.saucedemo.qa.pages.SortOption;
import com.saucedemo.qa.util.PriceSortValidator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Task 3 (UI part): two of the four sort options are automated, the two price-based ones
 * (lohi, hilo), because they are verified with PriceSortValidator.
 */
public class SortingTest extends BaseTest {

    private static final int EXPECTED_PRODUCT_COUNT = 6;

    private InventoryPage inventory;

    // Runs after BaseTest.openBrowser() (TestNG runs superclass @BeforeMethod first)
    @BeforeMethod(alwaysRun = true)
    public void login() {
        inventory = new LoginPage(driver, wait).open().loginAs(Config.STANDARD_USER, Config.PASSWORD);
    }

    @Test(description = "Sort 'Price (low to high)' shows prices in ascending order")
    public void sortPriceLowToHigh() {
        List<BigDecimal> prices = inventory.sortBy(SortOption.PRICE_LOW_TO_HIGH).prices();

        assertEquals(prices.size(), EXPECTED_PRODUCT_COUNT, "Unexpected number of products");
        assertTrue(PriceSortValidator.isSortedAscending(prices),
                "Prices are not in ascending order: " + prices);
    }

    @Test(description = "Sort 'Price (high to low)' shows prices in descending order")
    public void sortPriceHighToLow() {
        List<BigDecimal> prices = inventory.sortBy(SortOption.PRICE_HIGH_TO_LOW).prices();

        assertEquals(prices.size(), EXPECTED_PRODUCT_COUNT, "Unexpected number of products");
        assertTrue(PriceSortValidator.isSortedDescending(prices),
                "Prices are not in descending order: " + prices);
    }
}
