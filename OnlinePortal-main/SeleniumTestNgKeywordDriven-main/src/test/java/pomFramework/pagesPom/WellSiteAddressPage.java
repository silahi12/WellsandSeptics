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
    @FindBy(id = "SearchAddress")
    private WebElement addressSearch;

    @FindBy(xpath = "//*[@id='Password']")
    private WebElement passwordField;

    @FindBy(xpath = "//*[@type='submit']")
    private WebElement loginButton;

    @FindBy(id = "err_summary")
    private WebElement errorMessage;

    // Locator to find ALL list items within the suggestions dropdown
    @FindBy(xpath = "//ul[@id='nameSuggestions']/li")
    private List<WebElement> addressSuggestionsList;
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
        wait.until(ExpectedConditions.visibilityOf(addressSearch));
        addressSearch.clear();
        addressSearch.sendKeys(partialAddress);

    }

    public void selectAddressFromDropdown(String exactAddressString) {
        // Construct the dynamic XPath using the data-addressstring attribute from your DOM
        String xpathLocator = String.format("//li[@data-addressstring='%s']", exactAddressString);

        // Wait specifically for that exact list item to appear in the DOM and become clickable
        WebElement dropdownItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathLocator)));

        // Click the suggestion
        dropdownItem.click();
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
}
