# Task 5: Code review of `OldTest`

- **Original:** [`docs/original/OldTest.java`](original/OldTest.java) (kept unchanged, not compiled)
- **Fixed:** [`src/test/java/com/saucedemo/qa/tests/ReviewedTest.java`](../src/test/java/com/saucedemo/qa/tests/ReviewedTest.java)

The test is meant to check that **"Price (low to high)" sorts the products correctly**.
In its current form it **can never fail**, so it gives false confidence: it passes even when sorting is broken, the
page does not load, or a locator is wrong.

## Findings

All 10 findings are fixed in `ReviewedTest.java`. Each fix is marked there with a `// FIX #n` comment.

| # | Severity | Problem in `OldTest` | Why it matters | Fix in `ReviewedTest` |
|---|---|---|---|---|
| 1 | High | `static WebDriver driver = new ChromeDriver();` | Chrome starts when the class is **loaded**, not when a test runs. It is never closed, so every run leaves Chrome and chromedriver processes behind. The `static` driver is shared by all tests in the class, so they leak state (cookies, cart) into each other. If Chrome can't start, the class fails with a vague `ExceptionInInitializerError`. | `BaseTest`: a new driver in `@BeforeMethod`, `driver.quit()` in `@AfterMethod(alwaysRun = true)`. |
| 2 | Medium | Absolute XPath `/html/body/div/div/div[2]/div[1]/div/div/form/div[1]/input` | Breaks on any layout change (one extra wrapper `div`), and it's unreadable: you can't tell which field it is. The field has a stable `id`. | `By.id("user-name")` |
| 3 | Medium | `Thread.sleep(5000)` | Always wastes 5 s even when the page is ready in 300 ms, and still fails if the page takes 5.1 s. It is a source of slow and flaky tests. | `WebDriverWait` until the products list is visible (returns as soon as it is). |
| 4 | **High** | Prices are read **before** the sort is applied | `prices` is captured on the unsorted page and then the sort is changed. The test inspects elements from before the sort, not the sorted result, and can also hit `StaleElementReferenceException` if the list re-renders. | Sort first, wait until the dropdown shows `lohi`, **then** read the prices. |
| 5 | **High** | `first.compareTo(last) < 0` compares **Strings** | Lexical compare of `"$7.99"` vs `"$49.99"` looks at `'7'` vs `'4'` and decides `7.99 > 49.99`. A correctly sorted list is reported as **not** sorted, and `"$100.00" < "$9.99"`. | Parse with `Money.parse()` to `BigDecimal` and compare numbers. |
| 6 | Medium | Only the first and last price are compared | `[7.99, 49.99, 9.99, 15.99, 29.99, 50.00]` would pass. That isn't a sort check. | `PriceSortValidator.isSortedAscending()` checks every pair of neighbours (and is unit-tested). |
| 7 | **High** | `System.out.println("sorted")` + `Assert.assertTrue(true)` | The result is only printed and the assertion is always true. **The test passes whether or not the list is sorted.** | Real assertions: `assertTrue(isSortedAscending(prices), "...: " + prices)`, plus a product count check. |
| 8 | **High** | `catch (Exception e) { System.out.println("error"); }` | Every Selenium failure (element not found, timeout, stale element) is swallowed and the test is marked **passed**. The stack trace is lost. | No try/catch and no `throws Exception`. Any exception fails the test with its real cause. |
| 9 | Low | Method name `test1` | The report says nothing about what failed. | `sortByPriceLowToHighShowsAscendingPrices` plus a `description`. |
| 10 | Low | Never checks that login worked | If login fails, the test goes on to look for a sort dropdown that isn't there. The real cause (login) is hidden. | Assert the URL is `/inventory.html` before sorting. |

**Also noted (not changed):** credentials and URL hard-coded in the test (moved to `Config` in the fixed version),
the wildcard import `org.openqa.selenium.*`, and the unused `Exception` from `throws`.

## Why findings 4, 5 and 7 matter together

Findings **4** and **5** each make the check wrong, and **7** and **8** hide the result. So the original
test reports green regardless of whether the feature works, which is worse than having no test.

## Verifying the fix

```bash
mvn test -Dheadless=true -Dtest=ReviewedTest
```

To confirm the fixed test can actually **fail** (unlike the original), change `"lohi"` to `"hilo"` in
`ReviewedTest` and run it again. It fails with `Prices are not sorted low to high: [49.99, 29.99, ...]`.
