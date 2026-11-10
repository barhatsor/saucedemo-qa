package com.saucedemo.qa.tests;

import com.saucedemo.qa.util.PriceSortValidator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.saucedemo.qa.util.PriceSortValidator.Direction.ASCENDING;
import static com.saucedemo.qa.util.PriceSortValidator.Direction.DESCENDING;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Task 3: unit tests for PriceSortValidator.
 * Plain TestNG: does NOT extend BaseTest, so no browser is ever started.
 */
public class PriceSortValidatorTest {

    private static List<BigDecimal> prices(String... values) {
        return Arrays.stream(values).map(BigDecimal::new).toList();
    }

    @DataProvider(name = "cases")
    public Object[][] cases() {
        return new Object[][]{
                // description,                       prices,                                      asc?   desc?
                {"empty list",                         prices(),                                     true,  true},
                {"single price",                       prices("9.99"),                               true,  true},
                {"strictly ascending",                 prices("7.99", "9.99", "15.99", "49.99"),     true,  false},
                {"strictly descending",                prices("49.99", "29.99", "15.99", "7.99"),    false, true},
                {"ascending with duplicates",          prices("7.99", "15.99", "15.99", "49.99"),    true,  false},
                {"descending with duplicates",         prices("49.99", "15.99", "15.99", "7.99"),    false, true},
                {"all equal",                          prices("15.99", "15.99", "15.99"),            true,  true},
                {"unsorted",                           prices("29.99", "9.99", "49.99", "7.99"),     false, false},
                {"only last pair out of order",        prices("7.99", "9.99", "15.99", "8.00"),      false, false},
                {"only first pair out of order",       prices("9.99", "7.99", "15.99", "49.99"),     false, false},
                {"same value, different scale",        prices("10", "10.00", "10.0"),                true,  true},
                // A String comparison would say "100.00" < "9.99"; numbers must be compared as numbers
                {"numeric not lexical order",          prices("9.99", "15.99", "100.00"),            true,  false},
                {"cent-level difference",              prices("7.99", "7.98"),                       false, true},
        };
    }

    @Test(dataProvider = "cases")
    public void detectsAscendingOrder(String description, List<BigDecimal> prices, boolean asc, boolean desc) {
        assertEquals(PriceSortValidator.isSorted(prices, ASCENDING), asc, description);
        assertEquals(PriceSortValidator.isSortedAscending(prices), asc, description);
    }

    @Test(dataProvider = "cases")
    public void detectsDescendingOrder(String description, List<BigDecimal> prices, boolean asc, boolean desc) {
        assertEquals(PriceSortValidator.isSorted(prices, DESCENDING), desc, description);
        assertEquals(PriceSortValidator.isSortedDescending(prices), desc, description);
    }

    @Test
    public void realSauceDemoPricesLowToHigh() {
        // The six real product prices, in the order "Price (low to high)" should show them
        List<BigDecimal> lohi = prices("7.99", "9.99", "15.99", "15.99", "29.99", "49.99");
        assertTrue(PriceSortValidator.isSortedAscending(lohi));
        assertFalse(PriceSortValidator.isSortedDescending(lohi));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void nullListIsRejected() {
        PriceSortValidator.isSorted(null, ASCENDING);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void nullDirectionIsRejected() {
        PriceSortValidator.isSorted(prices("1.00", "2.00"), null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void nullElementIsRejected() {
        PriceSortValidator.isSortedAscending(Arrays.asList(new BigDecimal("1.00"), null));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void singleNullElementIsRejected() {
        PriceSortValidator.isSortedAscending(Arrays.asList((BigDecimal) null));
    }
}
