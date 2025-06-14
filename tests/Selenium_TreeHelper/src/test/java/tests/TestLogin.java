package tests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
public class TestLogin {

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
    public void testSuccessfulLogin() throws InterruptedException {
        driver.get("http://localhost:5173/login");

        driver.findElement(By.cssSelector("input[type='email']")).sendKeys("email");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("password");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for navigation
        Thread.sleep(3000);

        assertTrue(driver.getCurrentUrl().endsWith("/dashboard"));
    }

    @Test
    public void testLoginMissingFields() {
        driver.get("http://localhost:5173/login");

        // Leave email and password empty, then submit
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Capture current URL before submit
        String urlBeforeSubmit = driver.getCurrentUrl();

        // Wait for any navigation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Capture URL after submit attempt
        String urlAfterSubmit = driver.getCurrentUrl();

        // Assert URL did NOT change
        assertEquals(urlBeforeSubmit, urlAfterSubmit);
    }

    @Test
    public void testLoginInvalidCredentials() {
        driver.get("http://localhost:5173/login");

        driver.findElement(By.cssSelector("input[type='email']")).sendKeys("wronguser@example.com");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("wrongPassword");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for error message to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error")));

        assertTrue(errorMsg.getText().contains("Login failed. Please try again."));
    }
}
