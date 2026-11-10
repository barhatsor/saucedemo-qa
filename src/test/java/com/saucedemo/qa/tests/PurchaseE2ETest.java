package com.saucedemo.qa.tests;

import com.saucedemo.qa.core.BaseTest;
import com.saucedemo.qa.core.Config;
import com.saucedemo.qa.pages.CartPage;
import com.saucedemo.qa.pages.CheckoutCompletePage;
import com.saucedemo.qa.pages.CheckoutOverviewPage;
import com.saucedemo.qa.pages.InventoryPage;
import com.saucedemo.qa.pages.LoginPage;
import com.saucedemo.qa.pages.Product;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** Task 4: end-to-end purchase of the cheapest product. */
public class PurchaseE2ETest extends BaseTest {

    @Test(description = "Buy the cheapest product and verify Total = Item total + Tax")
    public void purchaseCheapestProduct() {
        // 1. Login
        InventoryPage inventory = new LoginPage(driver, wait)
                .open()
                .loginAs(Config.STANDARD_USER, Config.PASSWORD);

        // 2. Read every product's price from the page and let the code find the lowest.
        //    No product name or price is hard-coded.
        List<Product> products = inventory.products();
        assertTrue(products.size() > 1, "Expected several products to compare, found " + products.size());
        Product cheapest = inventory.cheapestProduct();
        System.out.printf("Cheapest of %d products: %s ($%s)%n",
                products.size(), cheapest.name(), cheapest.price());

        for (Product p : products) {
            assertTrue(cheapest.price().compareTo(p.price()) <= 0,
                    cheapest.name() + " is not cheaper than " + p.name());
        }

        // 3. Add it to the cart and go to checkout
        inventory.addToCart(cheapest.name());
        assertEquals(inventory.cartItemCount(), 1, "Cart badge should show 1 item");

        CartPage cart = inventory.openCart();
        assertEquals(cart.itemNames(), List.of(cheapest.name()), "Cart should hold only the cheapest product");

        CheckoutOverviewPage overview = cart.checkout().fillAndContinue("Test", "User", "12345");
        assertEquals(overview.itemNames(), List.of(cheapest.name()));

        // 4. Verify the totals with exact decimal arithmetic (BigDecimal, not double:
        //    with doubles 9.99 + 0.80 is 10.790000000000001, so an exact equals() check fails)
        BigDecimal itemTotal = overview.itemTotal();
        BigDecimal tax = overview.tax();
        BigDecimal total = overview.total();
        System.out.printf("Item total: %s, Tax: %s, Total: %s%n", itemTotal, tax, total);

        assertEquals(itemTotal.compareTo(cheapest.price()), 0,
                "Item total " + itemTotal + " should equal the product price " + cheapest.price());
        assertEquals(total.compareTo(itemTotal.add(tax)), 0,
                "Total " + total + " should equal Item total " + itemTotal + " + Tax " + tax
                        + " = " + itemTotal.add(tax));

        // 5. Complete the purchase
        CheckoutCompletePage complete = overview.finish();
        assertEquals(complete.header(), "Thank you for your order!");
        assertTrue(complete.isCartEmpty(), "Cart should be empty after the order is placed");
    }
}
