package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestLandingPage {

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
    public void testLandingPageNavigation() {

        // Verify heading text
        WebElement heading = driver.findElement(By.cssSelector(".landing-title"));
        assertEquals("Get your authorization for wood transport in a few easy steps!", heading.getText());

        // Click Register button and verify URL changes
        WebElement registerBtn = driver.findElement(By.id("registerBtn"));
        registerBtn.click();
        // Wait for navigation
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        assertTrue(driver.getCurrentUrl().endsWith("/register"));

        // Navigate back to landing page
        driver.navigate().back();

        // Click Login button and verify URL changes
        WebElement loginBtn = driver.findElement(By.id("loginBtn"));
        loginBtn.click();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        assertTrue(driver.getCurrentUrl().endsWith("/login"));
    }
}
