package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import static org.junit.jupiter.api.Assertions.*;

public class TestRegister {

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

    @Test
    public void testRegisterNewUser() throws InterruptedException {
        driver.get("http://localhost:5173/register");

        // Fill out the form fields by name attribute
        driver.findElement(By.name("email")).sendKeys("testuser@example.com");
        driver.findElement(By.name("password")).sendKeys("TestPassword123!");
        driver.findElement(By.name("confirmPassword")).sendKeys("TestPassword123!");
        driver.findElement(By.name("phoneNumber")).sendKeys("0712345678");
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("User");
        driver.findElement(By.name("dateOfBirth")).sendKeys("1990-01-01");

        // Select county from dropdown by value (e.g. "AB" for Alba)
        WebElement countyDropdown = driver.findElement(By.name("county"));
        countyDropdown.click();
        countyDropdown.findElement(By.cssSelector("option[value='AB']")).click();

        driver.findElement(By.name("city")).sendKeys("Alba Iulia");
        driver.findElement(By.name("address")).sendKeys("123 Forest Street");
        driver.findElement(By.name("cnp")).sendKeys("1234567890123");

        // Upload file for ID document
        WebElement fileInput = driver.findElement(By.name("idDocument"));
        fileInput.sendKeys("image");

        // Submit form
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for the form submission and navigation
        Thread.sleep(3000);  // replace with explicit wait for production code

        // Assert navigation to login page or success indicator
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.endsWith("/login") || currentUrl.contains("/login"));
    }

    @Test
    public void testRegisterWithMissingFields() {
        driver.get("http://localhost:5173/register");

        // Fill only password fields, leave email empty
        driver.findElement(By.name("password")).sendKeys("TestPassword123!");
        driver.findElement(By.name("confirmPassword")).sendKeys("TestPassword123!");

        // Click submit button
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Assert URL did NOT change
        assertTrue(driver.getCurrentUrl().endsWith("/register"));
    }

    @Test
    public void testRegisterWithPasswordMismatch() throws InterruptedException {
        driver.get("http://localhost:5173/register");

        driver.findElement(By.name("email")).sendKeys("testuser@example.com");
        driver.findElement(By.name("password")).sendKeys("Password123");
        driver.findElement(By.name("confirmPassword")).sendKeys("DifferentPassword");

        // Fill rest of required fields with dummy valid data
        driver.findElement(By.name("phoneNumber")).sendKeys("0712345678");
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("User");
        driver.findElement(By.name("dateOfBirth")).sendKeys("1990-01-01");
        driver.findElement(By.name("county")).sendKeys("AB"); // select proper county value
        driver.findElement(By.name("city")).sendKeys("Alba Iulia");
        driver.findElement(By.name("address")).sendKeys("123 Forest St");
        driver.findElement(By.name("cnp")).sendKeys("1234567890123");

        // Upload file
        driver.findElement(By.name("idDocument")).sendKeys("image");

        // Submit form
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Thread.sleep(1000);

        WebElement errorMsg = driver.findElement(By.cssSelector(".error"));
        assertTrue(errorMsg.getText().contains("Passwords do not match"));
    }

    @Test
    public void testRegisterWithInvalidEmail() {
        driver.get("http://localhost:5173/register");

        driver.findElement(By.name("email")).sendKeys("invalid-email-format");

        // fill required fields except file upload to pass those validations
        driver.findElement(By.name("password")).sendKeys("Password123!");
        driver.findElement(By.name("confirmPassword")).sendKeys("Password123!");
        driver.findElement(By.name("phoneNumber")).sendKeys("0712345678");
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("User");
        driver.findElement(By.name("dateOfBirth")).sendKeys("1990-01-01");

        WebElement countyDropdown = driver.findElement(By.name("county"));
        countyDropdown.click();
        countyDropdown.findElement(By.cssSelector("option[value='AB']")).click();

        driver.findElement(By.name("city")).sendKeys("Alba Iulia");
        driver.findElement(By.name("address")).sendKeys("123 Forest St");
        driver.findElement(By.name("cnp")).sendKeys("1234567890123");

        WebElement fileInput = driver.findElement(By.name("idDocument"));
        fileInput.sendKeys("image");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // URL should not change (still on register page)
        assertTrue(driver.getCurrentUrl().endsWith("/register"));

        // Check validity of email input using JS executor
        WebElement emailInput = driver.findElement(By.name("email"));
        Boolean emailValid = (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].checkValidity();", emailInput);
        assertFalse(emailValid);
    }


    @Test
    public void testRegisterMissingIdDocumentUrlCheck() {
        driver.get("http://localhost:5173/register");

        // Fill all required fields except the file upload
        driver.findElement(By.name("email")).sendKeys("testuser@example.com");
        driver.findElement(By.name("password")).sendKeys("Password123!");
        driver.findElement(By.name("confirmPassword")).sendKeys("Password123!");
        driver.findElement(By.name("phoneNumber")).sendKeys("0712345678");
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("User");
        driver.findElement(By.name("dateOfBirth")).sendKeys("1990-01-01");

        WebElement countyDropdown = driver.findElement(By.name("county"));
        countyDropdown.click();
        countyDropdown.findElement(By.cssSelector("option[value='AB']")).click();

        driver.findElement(By.name("city")).sendKeys("Alba Iulia");
        driver.findElement(By.name("address")).sendKeys("123 Forest St");
        driver.findElement(By.name("cnp")).sendKeys("1234567890123");

        // Capture current URL before submitting
        String urlBeforeSubmit = driver.getCurrentUrl();

        // Submit form
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for any potential navigation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Capture the URL after submission attempt
        String urlAfterSubmit = driver.getCurrentUrl();

        // Assert the URL has NOT changed
        assertEquals(urlBeforeSubmit, urlAfterSubmit);
    }


}
