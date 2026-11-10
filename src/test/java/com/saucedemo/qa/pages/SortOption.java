package com.saucedemo.qa.pages;

/** The four values of the product sort dropdown on the inventory page. */
public enum SortOption {
    NAME_A_TO_Z("az"),
    NAME_Z_TO_A("za"),
    PRICE_LOW_TO_HIGH("lohi"),
    PRICE_HIGH_TO_LOW("hilo");

    private final String value;

    SortOption(String value) {
        this.value = value;
    }

    /** The &lt;option value="..."&gt; used by the site. */
    public String value() {
        return value;
    }
}
