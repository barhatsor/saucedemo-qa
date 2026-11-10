# SauceDemo QA Automation

Home assignment: automated tests for [SauceDemo](https://www.saucedemo.com) in **Java 17 + Selenium 4 + TestNG**, built with Maven.

## Requirements

- JDK 17 or newer
- Google Chrome. The matching chromedriver is downloaded automatically by Selenium Manager, so there's nothing to install by hand.

Maven doesn't need to be installed: the project ships the **Maven Wrapper** (`./mvnw`, or `mvnw.cmd` on Windows),
which downloads the right Maven version on first run. If you have Maven installed, `mvn` works the same way.

## Running the tests

Run everything with a visible browser:

```bash
./mvnw test
```

Run everything headless:

```bash
./mvnw test -Dheadless=true
```

Run only the unit tests (Task 3, no browser):

```bash
./mvnw test -Dsuite=testng-unit.xml
```

Run a single class:

```bash
./mvnw test -Dheadless=true -Dtest=PurchaseE2ETest
```

TestNG reports are written to `target/surefire-reports/` (open `index.html`).

Last full run: **38 tests, 0 failures** (31 unit + 7 UI) in about 35 s headless, on Chrome 156 / macOS.

## Assignment tasks

| Task | What | Where |
|---|---|---|
| 1. STD | Test design document for the login screen | [`docs/STD_Login.docx`](docs/STD_Login.docx) |
| 2. Infrastructure + login | Browser opened in `@BeforeMethod`, closed in `@AfterMethod`; headless with `-Dheadless=true`; valid login, wrong password, locked-out user | `core/BaseTest`, `core/DriverFactory`, `tests/LoginTest` |
| 3. Sorting + logic | UI tests for **Price low→high (`lohi`)** and **Price high→low (`hilo`)**; a method that checks whether a price list is sorted ascending/descending, with browser-free TestNG unit tests | `tests/SortingTest`, `util/PriceSortValidator`, `tests/PriceSortValidatorTest` |
| 4. E2E purchase | Login, read all prices from the page, let the code find the cheapest, add to cart, checkout, verify `Total == Item total + Tax` with exact decimals, finish | `tests/PurchaseE2ETest` |
| 5. AI code review | 10 problems found in `OldTest`, all fixed | `tests/ReviewedTest`, `docs/CODE_REVIEW.md`, `docs/original/OldTest.java` |
| AI usage | Which AI tools were used and how | `AI_REPORT.md` |

## Project structure

```
src/test/java/com/saucedemo/qa/
├── core/      Config, DriverFactory, BaseTest (browser lifecycle)
├── pages/     Page Objects: Login, Inventory, Cart, Checkout (info / overview / complete)
├── util/      Money (price parsing), PriceSortValidator (sorted check)
└── tests/     LoginTest, SortingTest, PriceSortValidatorTest, PurchaseE2ETest, ReviewedTest
```

## Design decisions

- **One browser per test.** Each test gets a clean session, so tests can run in any order and one failure doesn't break the next test.
- **Explicit waits only** (`WebDriverWait`), with no `Thread.sleep` and no implicit waits. Selenium's docs warn that mixing implicit and explicit waits leads to unpredictable timeouts.
- **Money is `BigDecimal`, never `double`.** In `double`, `9.99 + 0.80` is `10.790000000000001`. Totals are compared with `compareTo`, so `10.0` and `10.00` count as equal.
- **Nothing is hard-coded about products.** The E2E test finds the cheapest product from the prices on the page, so it still works if the catalogue changes.
- **Page Object Model** keeps locators in one place. `ReviewedTest` keeps its locators inline on purpose, so it can be compared line by line with the original `OldTest`.
- **Chrome's password-leak popup is disabled.** SauceDemo's public password triggers Chrome's "change your password" dialog, which blocks clicks after login.

## Not completed / scope notes

- Task 3 asks for two of the four sort options. `lohi` and `hilo` are automated. `az` and `za` are defined in `SortOption` but have no tests.
- Only Chrome is supported.
