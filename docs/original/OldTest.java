// ORIGINAL CODE FROM THE ASSIGNMENT (Task 5), kept unchanged for reference.
// It is outside src/ on purpose so Maven never compiles or runs it.
// The reviewed version is src/test/java/com/saucedemo/qa/tests/ReviewedTest.java
// The findings are in docs/CODE_REVIEW.md

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class OldTest {
    static WebDriver driver = new ChromeDriver();

    @Test
    public void test1() throws Exception {
        try {
            driver.get("https://www.saucedemo.com");
            driver.findElement(By.xpath(
                "/html/body/div/div/div[2]/div[1]/div/div/form/div[1]/input"))
                .sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            Thread.sleep(5000);
            List<WebElement> prices =
                driver.findElements(By.className("inventory_item_price"));
            new Select(driver.findElement(By.className("product_sort_container")))
                .selectByValue("lohi");
            String first = prices.get(0).getText();
            String last = prices.get(prices.size() - 1).getText();
            if (first.compareTo(last) < 0) {
                System.out.println("sorted");
            }
            Assert.assertTrue(true);
        } catch (Exception e) {
            System.out.println("error");
        }
    }
}
