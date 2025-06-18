package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestNewAuthorizationRequest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/home");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void loginAs(String email, String password) {
        driver.get("http://localhost:5173/login");

        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(password);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/dashboard"));
    }

    // Set date field with JS + fire input event
    private void setDateField(WebElement dateInput, String isoDate) {
        String script =
                "const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));";
        ((JavascriptExecutor) driver).executeScript(script, dateInput, isoDate);
    }

    @Test
    public void testNewAuthorizationFormSuccessful() {
        loginAs("testuser@example.com", "TestPassword123!");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click request button
        WebElement requestBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Request a New Authorization')]")
        ));
        requestBtn.click();

        // Wait for form
        WebElement formHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(), 'Request a New Authorization')]")
        ));
        assertTrue(formHeading.isDisplayed());

        // Fill county
        WebElement countySelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("county")));
        countySelect.click();
        countySelect.findElement(By.xpath(".//option[@value='BV']")).click();

        // Fill normal fields
        driver.findElement(By.name("licensePlateNumber")).sendKeys("BV12AUTOTEST");
        driver.findElement(By.name("volume")).sendKeys("25");
        driver.findElement(By.name("description")).sendKeys("Positive test by automation");

        // Date fields
        setDateField(driver.findElement(By.name("fromDate")), "2025-07-01");
        setDateField(driver.findElement(By.name("untilDate")), "2025-07-10");


        // Submit
        WebElement submitBtn = driver.findElement(By.xpath("//button[contains(text(), 'Submit Request')]"));
        submitBtn.click();

        // Wait for redirect to dashboard
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertTrue(driver.getCurrentUrl().contains("/dashboard"));

        System.out.println("Positive test passed: redirected to dashboard.");
    }

    @Test
    public void testNewAuthorizationFormMissingRequiredField() {
        loginAs("testuser@example.com", "TestPassword123!");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click request button
        WebElement requestBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Request a New Authorization')]")
        ));
        requestBtn.click();

        // Wait for form
        WebElement formHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(), 'Request a New Authorization')]")
        ));
        assertTrue(formHeading.isDisplayed());

        // Fill county
        WebElement countySelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("county")));
        countySelect.click();
        countySelect.findElement(By.xpath(".//option[@value='BV']")).click();

        // Date fields
        setDateField(driver.findElement(By.name("fromDate")), "2025-07-01");
        setDateField(driver.findElement(By.name("untilDate")), "2025-07-10");


        // Other fields except missing license plate
        driver.findElement(By.name("volume")).sendKeys("25");
        driver.findElement(By.name("description")).sendKeys("Negative test - missing plate");

        // Submit
        WebElement submitBtn = driver.findElement(By.xpath("//button[contains(text(), 'Submit Request')]"));
        submitBtn.click();

        // Wait and verify still on same page
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        assertTrue(driver.getCurrentUrl().contains("/new-authorization"));

        System.out.println("Negative test passed: stayed on form due to missing required field.");
    }

}
