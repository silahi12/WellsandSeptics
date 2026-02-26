package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SearchPermitsPage extends BasePage {

    public String searchPermitsPageUrl = "Application/SearchPermitTypes";

    // WebElements on the Search permits page, identified using @FindBy annotations.
    @FindBy(xpath = "//h1[text()='Search Permits']")
    private WebElement searchPermitPageHeader;

    @FindBy(xpath = "//input[@name='searchTerm']")
    private WebElement searchBarField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchBtn;


    public SearchPermitsPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** -----functions----**/
    public boolean isSearchPermitPageHeaderDisplayed() {
        return isElementDisplayedPom(searchPermitPageHeader);
    }

    public void search(String searchTerm) {
        sendKeysPom(searchBarField, searchTerm);
        clickPom(searchBtn);
    }

    /**
     * Clicks the "Details" button for a specific permit type on the Permit Search page.
     * The method constructs the XPath dynamically using the provided permit type.
     *
     * @param permitType The visible text of the permit type (e.g., "General Discharge Permit").
     * @throws RuntimeException if the details button is not found or cannot be clicked.
     */
    public void clickDetailsButtonForPermitType(String permitType) {
        // Construct the dynamic XPath for the details button
        //the structure: <h4>Permit Type</h4> /sibling link with specific href part
        By detailsButtonLocator = By.xpath("//h4[normalize-space(text())='" + permitType + "']/following-sibling::a[contains(@href,'Application/PermitInfo')]");

        try {
            // Wait for the element to be visible and clickable before clicking
            WebElement detailsButton = wait.until(ExpectedConditions.elementToBeClickable(detailsButtonLocator));
            detailsButton.click();
            System.out.println("Clicked 'Details' button for Permit Type: '" + permitType + "'");
        } catch (Exception e) {
            System.err.println("Failed to click 'Details' button for Permit Type: '" + permitType + "'. Error: " + e.getMessage());
            throw new RuntimeException("Could not click details button for permit type: " + permitType, e);
        }
    }



}