package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TestDashboard {
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

        // Fill email and password
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(password);

        // Click submit
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for navigation to /dashboard
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/dashboard"));
    }

    @Test
    public void testAdminDashboard() {
        loginAs("email", "password");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for Users and Profile tabs
        WebElement usersTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(), 'Users')]")
        ));
        WebElement profileTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(), 'Profile')]")
        ));
        assertTrue(usersTab.isDisplayed());
        assertTrue(profileTab.isDisplayed());

        // Change role
        WebElement roleSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".role-select")
        ));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                roleSelect, "AUTHORITY"
        );

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            System.out.println("Role change alert text: " + alertText);
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert appeared for role change.");
        }

        // Wait for Users list
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'All Users')]")));

        // Locate testuser View ID button
        WebElement viewIdBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[strong[text()='Email:'] and contains(., 'testuser@example.com')]/ancestor::li//button[contains(@class, 'doc-btn')]")
        ));

        viewIdBtn.click();
        wait.until(ExpectedConditions.urlContains("/documents?userId="));
        driver.navigate().back();

        // Wait for Users list again after back
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'All Users')]")));

        // Use fresh XPath for testuser DELETE button
        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[strong[text()='Email:'] and contains(., 'testuser@example.com')]/ancestor::li//button[contains(@class, 'delete-btn')]")
        ));

        deleteBtn.click();

        // Handle confirm alert
        Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
        String confirmText = confirmAlert.getText();
        System.out.println("Confirm alert text: " + confirmText);
        confirmAlert.accept();

        // Handle success alert
        Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
        String successText = successAlert.getText();
        System.out.println("Delete success alert: " + successText);
        successAlert.accept();

        // Find new Profile tab
        WebElement newProfileTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(), 'Profile')]")
        ));
        newProfileTab.click();

        // Log out
        WebElement logOutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Log Out')]")
        ));
        logOutBtn.click();
        wait.until(ExpectedConditions.urlContains("/login"));
    }


    @Test
    public void testRequestorDashboard() {
        loginAs("email", "password");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement authTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Authorizations')]")));
        WebElement profileTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Profile')]")));
        assertTrue(authTab.isDisplayed());
        assertTrue(profileTab.isDisplayed());

        WebElement requestBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Request a New Authorization')]")));
        assertTrue(requestBtn.isDisplayed());

        profileTab.click();
        WebElement logOutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Log Out')]")));
        logOutBtn.click();
        wait.until(ExpectedConditions.urlContains("/login"));
    }

    @Test
    public void testAuthorityDashboard() {
        loginAs("email", "password");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement authTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Authorizations')]")));
        WebElement profileTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Profile')]")));
        assertTrue(authTab.isDisplayed());
        assertTrue(profileTab.isDisplayed());

        // Pick first request and approve it with comment
        WebElement approveBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".status-btn.approve")));
        approveBtn.click();

        WebElement commentBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("textarea")));
        commentBox.sendKeys("Approved for test purposes.");

        WebElement submitBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Submit')]")));
        submitBtn.click();

        // Handle status update alert
        Alert statusAlert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Status updated successfully!", statusAlert.getText());
        statusAlert.accept();

        // Click View ID Document for same request
        WebElement viewIdBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".doc-btn")
        ));
        viewIdBtn.click();

        wait.until(ExpectedConditions.urlContains("/documents?userId="));
        assertTrue(driver.getCurrentUrl().contains("/documents?userId="));

        // Navigate back
        driver.navigate().back();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(), 'Authorizations')]")
        ));

        // Re-find Profile tab
        WebElement newProfileTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(), 'Profile')]")
        ));
        newProfileTab.click();

        // Log out
        WebElement logOutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Log Out')]")
        ));
        logOutBtn.click();
        wait.until(ExpectedConditions.urlContains("/login"));
    }

}
