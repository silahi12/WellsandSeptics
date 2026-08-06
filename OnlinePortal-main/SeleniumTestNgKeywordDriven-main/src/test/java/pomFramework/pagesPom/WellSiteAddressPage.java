package pomFramework.pagesPom;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pomFramework.driverPom.DriverManagerPom;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * BasePage class provides common functionalities and initializes Page Objects.
 * All other page classes should extend this class.
 */
public class WellSiteAddressPage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public WellSiteAddressPage() {
        this.driver = DriverManagerPom.getDriverPom(); // Get the WebDriver instance from ThreadLocal
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Initialize WebDriverWait
        PageFactory.initElements(driver, this); // Initialize WebElements declared in page classes
    }

    public String loginPageUrl = "/Account/Login";

    // WebElements on the login page, identified using @FindBy annotations.
   // @FindBy(id = "SearchAddress")  -   old locators
   // private WebElement addressSearch;

// For new PAge
    @FindBy(xpath = "//input[@id='rbMethodAddress']")
    private WebElement addressSearchRadioInput;

    @FindBy(xpath = "//*[@id='SearchAddress']")
    private WebElement addressSearchTextBox;

    @FindBy(xpath = "//label[@for='rbMethodAddress']")
    private WebElement addressSearchRadioLabel;

    @FindBy(xpath = "//*[@id='SearchAccount']")
    private WebElement propertyNumberRadioInput;

    @FindBy(xpath = "//label[@for='rbMethodAccount']")
    private WebElement propertyNumberRadioLabel;

    @FindBy(xpath = "//input[@id='SearchAccount']")
    private WebElement propertyNumberField;

//    @FindBy(xpath = "//*[@id='AddressCorrectionNeeded']")
//    private WebElement addressCorrectionNeededCheckboxInput;

    @FindBy(xpath = "//label[@for='AddressCorrectionNeeded']")
    private WebElement addressCorrectionNeededCheckboxLabel;

    @FindBy(xpath = "//*[@id='nextButton']")
    private WebElement saveAndContinueButton;

    @FindBy(xpath = "//a[normalize-space()='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//*[@id='AddressCorrectionNotes']")
    private WebElement correctionNotesField;

    @FindBy(xpath = "//*[@id='btnSearchByAccount']")
    private WebElement searchButton;



    // --- VALIDATION ERROR LOCATORS ---

    // Top Banner Errors
    @FindBy(xpath = "//li[normalize-space()='County is required.']")
    private WebElement bannerErrorCounty;

    @FindBy(xpath = "//li[normalize-space()='Property Number is required.']")
    private WebElement bannerErrorPropertyNumber;

    // Inline Field Errors (Matches text explicitly shown under the fields)
    // The '|' acts as an OR operator to catch common error tag types (span, div, or strong)
    @FindBy(xpath = "//span[normalize-space()='County is required.'] | //div[contains(@class, 'error-message') and contains(text(), 'County is required.')]")
    private WebElement inlineErrorCounty;

    @FindBy(xpath = "//span[normalize-space()='Property Number is required.'] | //div[contains(@class, 'error-message') and contains(text(), 'Property Number is required.')]")
    private WebElement inlineErrorPropertyNumber;

    @FindBy(xpath = "//div[@id='search-success-message']//p[@class='usa-alert__text']")
    private WebElement successMessageText;

    @FindBy(xpath = "//*[@id='Password']")
    private WebElement passwordField;

    @FindBy(xpath = "//*[@type='submit']")
    private WebElement loginButton;

    @FindBy(id = "err_summary")
    private WebElement errorMessage;

    // Locator to find ALL list items within the suggestions dropdown
    @FindBy(xpath = "//ul[@id='nameSuggestions']/li")
    private List<WebElement> addressSuggestionsList;

    // --- ADD THESE NEW LOCATORS ---
    // Update the IDs/XPaths to match your actual web page
    @FindBy(id = "PHYSICAL_PARISH_OR_COUNTY_CODE")
    private WebElement countyDropdown;

    @FindBy(id = "PHYSICAL_ADDRESS_MUNICIPALITY")
    private WebElement cityDropdown;

    @FindBy(id = "physicalZip")
    private WebElement zipCodeDropdown;


    // New methods

    /**
     * Selects the 'Property Number' search method radio button.
     */
    public void selectPropertySearchRadio() {
        propertyNumberRadioLabel.click();
    }

    /**
     * Enters the property number into the search field.
     * @param propertyNumber The 16-character account identifier.
     */
    public void searchByPropertyNumber(String propertyNum) {

        // 1. Wait for the text box to be visible AND enabled (clickable)
        wait.until(ExpectedConditions.visibilityOf(propertyNumberRadioInput));
        wait.until(ExpectedConditions.elementToBeClickable(propertyNumberRadioInput));

        // 2. Clear and send keys
        propertyNumberRadioInput.clear();
        propertyNumberRadioInput.sendKeys(propertyNum);

        // 3. Click Search
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchButton.click();
    }

    /**
     * Selects the 'Address' search method radio button.
     */
    public void selectAddressSearchRadio() {
        addressSearchRadioLabel.click();
    }

    /**
     * Clicks the 'Address Correction Needed' checkbox.
     */
    public void clickAddressCorrectionCheckbox() {
        // 1. Wait for the label to be present in the DOM first
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[@for='AddressCorrectionNeeded']")));

        JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();

        // 2. Scroll it into view just to be safe
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", addressCorrectionNeededCheckboxLabel);

        // 3. ONLY use the JavaScript click (Bypasses all interception errors)
        js.executeScript("arguments[0].click();", addressCorrectionNeededCheckboxLabel);

        System.out.println("Address Correction checkbox clicked successfully via JavaScript.");

        // Notice that addressCorrectionNeededCheckboxLabel.click(); is GONE!
    }

    /**
     * Enters notes explaining the address discrepancy.
     * @param notes The text to input into the correction notes text area.
     */
    public void enterCorrectionNotes(String notes) {
        correctionNotesField.clear();
        correctionNotesField.sendKeys(notes);
    }


//    @FindBy(xpath = "//button[contains(text(), 'Save and Continue')]")
//    private WebElement saveAndContinueBtn;

    // --- ADD THESE NEW METHODS ---

    public void selectCounty(String countyName) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", countyDropdown);
        wait.until(ExpectedConditions.visibilityOf(countyDropdown));
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(countyDropdown);

        // ADD THIS TO PRINT ALL OPTIONS:
        for (WebElement option : select.getOptions()) {
            System.out.println("Available County Option: [" + option.getText() + "]");
        }

        select.selectByVisibleText(countyName);
    }

    public void selectCity(String cityName) {
        wait.until(ExpectedConditions.visibilityOf(cityDropdown));
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(cityDropdown);

        // ADD THIS TO PRINT ALL CITY OPTIONS:
        System.out.println("--- AVAILABLE CITIES FOR THIS COUNTY ---");
        for (WebElement option : select.getOptions()) {
            System.out.println("Available City Option: [" + option.getText() + "]");
        }

        select.selectByVisibleText(cityName);
    }

    public void selectZipCode(String zipCode) {
        wait.until(ExpectedConditions.visibilityOf(zipCodeDropdown));
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(zipCodeDropdown);

        // Select the Zip Code from the dropdown
        select.selectByVisibleText(zipCode);
        System.out.println("Selected Zip Code: " + zipCode);
    }

    public void clickSaveAndContinue() throws InterruptedException {
        // 1. Scroll the Save button into view so it is on the screen
        JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", saveAndContinueButton);

        // 2. Wait a brief moment for the smooth scrolling animation to finish
        Thread.sleep(1000);

        // 3. Wait until the button is actually clickable, then click it
        wait.until(ExpectedConditions.elementToBeClickable(saveAndContinueButton));
        saveAndContinueButton.click();

        System.out.println("Clicked the Save and Continue button.");

        // 4. Wait for some time (e.g., 5 seconds) after clicking to let the next page load
        Thread.sleep(5000);
    }
    /** --------- Functions ------------- **/

    /**
     * Performs a login operation.
     *
     *
     */
    public void addressSearchText(String partialAddress) {
        //wait.until(ExpectedConditions.visibilityOf(addressSearch)).sendKeys(Address);
//        sendKeysPom(usernameField, username);
//
//        sendKeysPom(passwordField, password);
//        clickPom(loginButton);
//        wait.until(ExpectedConditions.visibilityOf(addressSearchRadioLabel));
//        addressSearchRadioInput.clear();
//        addressSearchRadioInput.sendKeys(partialAddress);

        addressSearchTextBox.clear();
        addressSearchTextBox.sendKeys(partialAddress);

    }

    public void selectAddressFromDropdown(String exactAddressString) {
        // 1. Construct the dynamic XPath
        String xpathLocator = String.format("//li[@data-addressstring='%s']", exactAddressString);

        // 2. Wait for the element to exist in the DOM (presence, not visibility)
        WebElement dropdownItem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));

        // 3. FORCE THE CLICK using JavascriptExecutor to bypass Jenkins resolution/overlap issues
        JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();
        js.executeScript("arguments[0].click();", dropdownItem);
    }

    // --- METHODS ---


    public void verifyBlankSubmissionErrors() {
        // Wait for the banner error to appear after clicking save
        wait.until(ExpectedConditions.visibilityOf(bannerErrorCounty));

        // Assert Top Banner Errors
        Assert.assertTrue(bannerErrorCounty.isDisplayed(), "Top banner is missing 'County is required.' error.");
        Assert.assertTrue(bannerErrorPropertyNumber.isDisplayed(), "Top banner is missing 'Property Number is required.' error.");

        // Assert Inline Errors
        Assert.assertTrue(inlineErrorCounty.isDisplayed(), "Inline error 'County is required.' is missing below the County field.");
        Assert.assertTrue(inlineErrorPropertyNumber.isDisplayed(), "Inline error 'Property Number is required.' is missing below the PID field.");

        System.out.println("Successfully verified all expected validation errors on the Well Site Address page.");
    }

    public void clickSearchButton() {
        searchButton.click();
    }

    /**
     * Traverses through all dropdown suggestions and returns their text values.
     *
     * @return A list of strings containing the text of each dropdown option.
     */
    public List<String> getAllDropdownValues() {
        // Wait until the parent ul element is visible so we know the dropdown has triggered
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameSuggestions")));

        List<String> suggestionTexts = new ArrayList<>();

        // Traverse (loop) through each WebElement in the list
        for (WebElement suggestion : addressSuggestionsList) {
            // Get the visible text of the list item
            String textValue = suggestion.getText();
            suggestionTexts.add(textValue);

            // Optional: Print to console to verify during execution
            System.out.println("Found address option: " + textValue);
        }

        return suggestionTexts;
    }


   // @Step("Check if property search success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        try {
            // Wait up to the default timeout for the success banner to become visible
            wait.until(ExpectedConditions.visibilityOf(successMessageText));
            return successMessageText.isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Success message did not appear within the timeout period.");
            return false;
        }
    }

   // @Step("Get property search success message text")
    public String getSuccessMessageText() {
        wait.until(ExpectedConditions.visibilityOf(successMessageText));
        return successMessageText.getText().trim();
    }

}
