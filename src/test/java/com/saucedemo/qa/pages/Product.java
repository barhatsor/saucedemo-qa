package com.saucedemo.qa.pages;

import java.math.BigDecimal;

/** A product as read from the inventory page. */
public record Product(String name, BigDecimal price) {
}
