# AI Usage Report

## Tools used

| Tool | Used for |
|---|---|
| **Claude** (Anthropic), in the Claude desktop app's Code tab (Claude Code, Opus model) | Reading the assignment, setting up the Maven project, writing the test code, reviewing `OldTest`, running the suite |

## What I asked the AI

1. **Read the assignment PDF and set up the project.** A Java + Selenium + TestNG Maven project, with the browser opened in
   `@BeforeMethod` and closed in `@AfterMethod`, and headless mode via `-Dheadless=true`.
2. **Write the tests for Tasks 2–4:** the three login cases, two sort options plus a sorted-check method with
   browser-free unit tests, and the end-to-end purchase of the cheapest product.
3. **Review `OldTest`:** list every problem with its impact and propose a fix, then write `ReviewedTest`.
4. **Run the full suite headless** and fix whatever failed.
5. **Review my STD document** (Task 1) for gaps in coverage and wording.

## How I used the answers

- I read every generated file and checked that I understand **why** each choice was made, not just that it compiles
  (for example why `BigDecimal` and not `double`, and why prices must be read *after* sorting).
- I **ran the suite** (`mvn test -Dheadless=true`) and confirmed it passes, instead of trusting the code by reading it.
- For Task 5, I checked each finding against the original code myself. For example, I confirmed that
  `"$7.99".compareTo("$49.99")` is positive, so the original test really does treat a sorted list as unsorted.
- I checked that `ReviewedTest` can actually **fail**: switching `lohi` to `hilo` makes it fail, which the original
  never could.

## AI suggestions I rejected or corrected

### 1. A wrong floating-point example in a code comment (corrected)

When it wrote the E2E test, the AI added a comment explaining why money must not be `double`:

> with doubles 29.99 + 2.40 is 32.389999999999997

I checked this with Python before keeping it, and `29.99 + 2.40` prints **`32.39`**. The claim was false. The
principle is right, but the example would have been wrong in my submission. I replaced it with an example I verified,
which uses a real SauceDemo price and tax: `9.99 + 0.80 = 10.790000000000001`.

**Lesson:** an explanation that sounds right still has to be checked.

### 2. Installing Maven with Homebrew (rejected by me)

The AI's first step to get Maven was `brew install maven`. On my Mac, Homebrew builds from source
(the machine is in Homebrew's "tier 3" support), so it started compiling OpenJDK, which takes a very long time,
even though I already had JDK 17 and 25 installed. I stopped it and asked for a faster way.
We switched to Apache's prebuilt Maven archive (checksum verified) and added the **Maven Wrapper** (`./mvnw`) to the project.

**This is better for the grader too:** anyone can run the tests with just a JDK and Chrome, without installing Maven.

### 3. An outdated Selenium version (corrected after running the tests)

The AI chose Selenium **4.33.0** because it was the newest version returned by the Maven Central search API.
The tests passed, but the log was full of `Unable to find CDP implementation matching 156`, which means that Selenium
version is older than my Chrome. Checking Maven Central's metadata file directly showed the search index was stale and
the real latest version was **4.52.0**. After upgrading, the warnings are gone and all 38 tests still pass.

**Lesson:** "it works" isn't the same as "it's right". Warnings in the output are worth reading.

