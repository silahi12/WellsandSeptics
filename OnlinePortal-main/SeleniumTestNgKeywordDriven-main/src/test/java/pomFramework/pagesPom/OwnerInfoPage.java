package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pomFramework.driverPom.DriverManagerPom;
import io.qameta.allure.Step;

public class OwnerInfoPage extends BasePage {

    public OwnerInfoPage() {
        PageFactory.initElements(DriverManagerPom.getDriverPom(), this);
    }

    // --- LOCATORS ---

    // Contact Information
    @FindBy(id = "PersonContact_Email")
    private WebElement emailInput;

    @FindBy(id = "PersonContact_Phone")
    private WebElement phoneInput;

    // Added based on the screenshot (Update ID if different in your DOM)
    @FindBy(xpath = "//label[contains(text(), 'Address Correction Needed')]/preceding-sibling::input")
    private WebElement addressCorrectionCheckbox;

    // Save and Continue Button
    @FindBy(xpath = "//button[normalize-space()='Save and Continue']")
    private WebElement saveAndContinueBtn;

    @FindBy(xpath = "//label[@for='OwnerAddressCorrectionNeeded']")
    WebElement ownerAddressCorrectionCheckboxLabel;

    @FindBy(id = "OwnerAddressCorrectionNotes")
    WebElement addressCorrectionNotesTextarea;



    // --- VALIDATION ERROR LOCATORS ---

    // Top Banner Errors
    @FindBy(xpath = "//li[normalize-space()='Email is required.']")
    private WebElement bannerErrorEmail;

    @FindBy(xpath = "//li[normalize-space()='Phone number is required.']")
    private WebElement bannerErrorPhone;

    // Inline Field Errors (Using the OR '|' operator to handle standard error tag types)
    @FindBy(xpath = "//span[normalize-space()='Email is required.'] | //div[contains(@class, 'error-message') and contains(text(), 'Email is required.')]")
    private WebElement inlineErrorEmail;

    @FindBy(xpath = "//span[normalize-space()='Phone number is required.'] | //div[contains(@class, 'error-message') and contains(text(), 'Phone number is required.')]")
    private WebElement inlineErrorPhone;






    // --- METHODS ---

    @Step("Entering Owner Email: {0}")
    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    @Step("Entering Owner Phone Number: {0}")
    public void enterPhoneNumber(String phoneNumber) {
        wait.until(ExpectedConditions.visibilityOf(phoneInput));
        phoneInput.clear();
        phoneInput.sendKeys(phoneNumber);
    }

    @Step("Clicking Save and Continue on Owner Info Page")
    public void clickSaveAndContinue() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", saveAndContinueBtn);

        Thread.sleep(1000); // Wait for scroll to finish
        wait.until(ExpectedConditions.elementToBeClickable(saveAndContinueBtn));

        try {
            saveAndContinueBtn.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            // Using JS click as a fallback for USWDS design system elements
            js.executeScript("arguments[0].click();", saveAndContinueBtn);
        }
    }

    @Step("Filling out Owner Contact Information and submitting")
    public void fillOwnerContactInfoAndSubmit(String email, String phone) throws InterruptedException {
        enterEmail(email);
        enterPhoneNumber(phone);
        clickSaveAndContinue();
    }

    // --- METHODS ---

    @Step("Verify validation messages for blank submission on Owner Info page")
    public void verifyBlankSubmissionErrors() {
        // Wait for the banner error to appear after clicking Save and Continue
        wait.until(ExpectedConditions.visibilityOf(bannerErrorEmail));

        // Assert Top Banner Errors
        Assert.assertTrue(bannerErrorEmail.isDisplayed(), "Top banner is missing 'Email is required.' error.");
        Assert.assertTrue(bannerErrorPhone.isDisplayed(), "Top banner is missing 'Phone number is required.' error.");

        // Assert Inline Errors
        Assert.assertTrue(inlineErrorEmail.isDisplayed(), "Inline error 'Email is required.' is missing below the Email field.");
        Assert.assertTrue(inlineErrorPhone.isDisplayed(), "Inline error 'Phone number is required.' is missing below the Phone field.");

        System.out.println("Successfully verified all expected validation errors on the Owner Information page.");
    }


    @Step("Clicking Owner Address Correction Checkbox")
    public void clickOwnerAddressCorrectionCheckbox() {
        // 1. Wait for the label to be present in the DOM
        // Make sure this xpath matches the one in your @FindBy above
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[@for='OwnerAddressCorrectionNeeded']")));

        JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();

        // 2. Scroll it into view to ensure it is visible
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", ownerAddressCorrectionCheckboxLabel);

        // 3. ONLY use the JavaScript click (Bypasses all interception errors)
        js.executeScript("arguments[0].click();", ownerAddressCorrectionCheckboxLabel);

        System.out.println("Owner Address Correction checkbox clicked successfully via JavaScript.");
    }


    @Step("Entering Owner Address Correction Notes")
    public void enterOwnerAddressCorrectionNotes(String notes) {

        // 1. Wait for the textarea to become VISIBLE after the checkbox click reveals it
        wait.until(ExpectedConditions.visibilityOf(addressCorrectionNotesTextarea));

        // 2. Clear the field (best practice before typing)
        addressCorrectionNotesTextarea.clear();

        // 3. Enter the discrepancy notes
        addressCorrectionNotesTextarea.sendKeys(notes);

        System.out.println("Owner Address Correction notes entered successfully.");
    }
}