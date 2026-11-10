package com.saucedemo.qa.tests;

import com.saucedemo.qa.core.BaseTest;
import com.saucedemo.qa.core.Config;
import com.saucedemo.qa.pages.InventoryPage;
import com.saucedemo.qa.pages.LoginPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/** Task 2: login scenarios. */
public class LoginTest extends BaseTest {

    @Test(description = "standard_user with the correct password reaches the Products page")
    public void validLoginOpensProductsPage() {
        InventoryPage inventory = new LoginPage(driver, wait)
                .open()
                .loginAs(Config.STANDARD_USER, Config.PASSWORD);

        assertTrue(driver.getCurrentUrl().endsWith("/inventory.html"),
                "Unexpected URL after login: " + driver.getCurrentUrl());
        assertEquals(inventory.title(), "Products");
        assertFalse(inventory.products().isEmpty(), "Products page shows no products");
    }

    @Test(description = "A wrong password shows an error and keeps the user on the login page")
    public void wrongPasswordShowsError() {
        LoginPage login = new LoginPage(driver, wait).open();
        login.submit(Config.STANDARD_USER, "wrong_password");

        assertEquals(login.errorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
        assertTrue(login.isLoginButtonDisplayed(), "User should stay on the login page");
        assertFalse(driver.getCurrentUrl().contains("/inventory.html"), "User must not be logged in");
    }

    @Test(description = "locked_out_user is refused with a 'locked out' error")
    public void lockedOutUserIsRefused() {
        LoginPage login = new LoginPage(driver, wait).open();
        login.submit(Config.LOCKED_OUT_USER, Config.PASSWORD);

        assertEquals(login.errorMessage(), "Epic sadface: Sorry, this user has been locked out.");
        assertTrue(login.isLoginButtonDisplayed(), "User should stay on the login page");
        assertFalse(driver.getCurrentUrl().contains("/inventory.html"), "User must not be logged in");
    }
}
