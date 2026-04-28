package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pomFramework.driverPom.DriverManagerPom;
import io.qameta.allure.Step;

public class OwnerInfoPage extends BasePage {

    public OwnerInfoPage() {
        PageFactory.initElements(DriverManagerPom.getDriverPom(), this);
    }

    // --- LOCATORS ---

    // 1. Owner is a Business Checkbox
    // Note: The HTML shows this is checked by default.
    @FindBy(id = "IsBusiness")
    private WebElement isBusinessCheckbox;

    // 2. Individual Fields (Only visible if IsBusiness is unchecked)
    @FindBy(id = "PersonContact_FirstName")
    private WebElement firstNameInput;

    @FindBy(id = "PersonContact_LastName")
    private WebElement lastNameInput;

    // 3. Business Fields (Only visible if IsBusiness is checked)
    @FindBy(id = "PersonContact_CompanyName")
    private WebElement companyNameInput;

    // 4. Contact Information
    @FindBy(id = "PersonContact_Email")
    private WebElement emailInput;

    @FindBy(id = "PersonContact_Phone")
    private WebElement phoneInput;

    // 5. Sync Well Site Address Checkbox
    @FindBy(id = "SyncWellSiteAddress")
    private WebElement syncWellSiteAddressCheckbox;

    // 6. Save and Continue Button
    @FindBy(id = "nextButton")
    private WebElement saveAndContinueBtn;

    // --- METHODS ---

    @Step("Setting 'Owner is a Business' checkbox to: {0}")
    public void setBusinessCheckbox(boolean shouldBeChecked) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("IsBusiness")));

        // If the checkbox state doesn't match what we want, click it
        if (isBusinessCheckbox.isSelected() != shouldBeChecked) {
            JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();
            js.executeScript("arguments[0].click();", isBusinessCheckbox);
        }
    }

    @Step("Entering Owner Phone Number: {0}")
    public void enterPhoneNumber(String phoneNumber) {
        wait.until(ExpectedConditions.visibilityOf(phoneInput));
        phoneInput.clear();
        phoneInput.sendKeys(phoneNumber);
    }

    @Step("Entering Owner Email: {0}")
    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    @Step("Entering Individual Name: {0} {1}")
    public void enterIndividualName(String firstName, String lastName) {
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);

        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
    }

    @Step("Entering Business Name: {0}")
    public void enterBusinessName(String businessName) {
        wait.until(ExpectedConditions.visibilityOf(companyNameInput));
        companyNameInput.clear();
        companyNameInput.sendKeys(businessName);
    }

    @Step("Checking 'Same as Well Site Address'")
    public void checkSameAsWellSiteAddress() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SyncWellSiteAddress")));

        if (!syncWellSiteAddressCheckbox.isSelected()) {
            // Using JS click because standard click often gets intercepted by USWDS labels
            JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();
            js.executeScript("arguments[0].click();", syncWellSiteAddressCheckbox);
        }
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
            js.executeScript("arguments[0].click();", saveAndContinueBtn);
        }
    }

    @Step("Filling out Business Owner Information and submitting")
    public void fillBusinessOwnerInfoAndSubmit(String businessName, String phone, String email) throws InterruptedException {
        setBusinessCheckbox(true);
        enterBusinessName(businessName);
        enterPhoneNumber(phone);
        enterEmail(email);
        checkSameAsWellSiteAddress();

        // Wait a moment for the javascript to auto-fill the address dropdowns
        Thread.sleep(3000);

        clickSaveAndContinue();
    }

}