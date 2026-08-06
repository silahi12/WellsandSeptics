package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pomFramework.driverPom.DriverManagerPom;
import io.qameta.allure.Step;

public class SubmissionSuccessPage extends BasePage {

    public SubmissionSuccessPage() {
        PageFactory.initElements(DriverManagerPom.getDriverPom(), this);
    }

    // --- LOCATORS ---

    // 1. Main Success Header
    @FindBy(xpath = "//*[normalize-space()='Digital Application Received']")
    private WebElement successHeader;

    // 2. Pending Payment Status Banner
    @FindBy(xpath = "//*[contains(text(), 'Pending Payment (Physical)')]")
    private WebElement pendingPaymentStatus;

    // 3. Application ID Value
    // Note: This XPath assumes a standard sibling structure (like a table or grid).
    // You may need to tweak it based on the exact DOM tags (e.g., div, td, span).
    @FindBy(xpath = "//*[contains(text(), 'Application ID')]/following-sibling::*[1]")
    private WebElement applicationIdValue;


    // --- METHODS ---

    @Step("Verifying successful submission on Confirmation Page")
    public void verifySuccessfulSubmission() {
        // Wait for the final page to fully load by checking for the main header
        wait.until(ExpectedConditions.visibilityOf(successHeader));

        // Hard Assertions - Test will fail immediately if these are false
        Assert.assertTrue(successHeader.isDisplayed(),
                "Fail: 'Digital Application Received' header is missing!");

        Assert.assertTrue(pendingPaymentStatus.isDisplayed(),
                "Fail: 'Pending Payment (Physical)' status is not visible!");

        System.out.println("Assertion Passed: Digital Application Successfully Received.");
    }

    @Step("Extracting and Logging Application ID")
    public String getApplicationId() {
        try {
            wait.until(ExpectedConditions.visibilityOf(applicationIdValue));
            String appId = applicationIdValue.getText().trim();
            System.out.println("SUCCESS - Generated Application ID: " + appId);
            return appId;
        } catch (Exception e) {
            System.out.println("Warning: Could not extract Application ID. Check the DOM structure for the locator.");
            return null;
        }
    }
}