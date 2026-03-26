package pomFramework.pagesPom;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class PermitInfoPage extends BasePage {

    public String permitInfoPageUrl = "Application/PermitInfo";

    // WebElements on the PermitInfoPage, identified using @FindBy annotations.
    @FindBy(xpath = "//h1[@class='page-heading']")
    private WebElement permitInfoPageHeader;

    @FindBy(xpath = "//select[@id='SelectedPermit']")
    private WebElement permitDetailTypeDropdown;

    @FindBy(xpath = "//select[@class='form-select permit-action-list show']")
    private WebElement permitActionTypeDropdown;

    @FindBy(xpath = "//div[@class='card-body']/a[contains(text(),'Apply Now')]")
    private List<WebElement> applyNowButtons;


    public PermitInfoPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** -----functions----**/
    public String getHeaderText(){
        return getTextPom(permitInfoPageHeader);
    }

    public void selectPermitDetailType(String permitDetailType){
        selectDropdownOptionByVisibleTextPom(permitDetailTypeDropdown, permitDetailType);
    }

    public void selectPermitActionType(String permitActionType){
        selectDropdownOptionByVisibleTextPom(permitActionTypeDropdown, permitActionType);
    }

    public void clickApplyNowBtnDisplayed(){

        try {
            // First, ensure the list of elements is not empty and has been initialized by PageFactory.
            // PageFactory.initElements is called in BasePage constructor, so this list should be populated
            // if elements are present in the DOM.
            if (applyNowButtons.isEmpty()) {
                // If the list is empty, it means no elements were found by the @FindBy locator initially.
                // We can add a more specific wait here if needed, but for now, rely on PageFactory's init.
                throw new NoSuchElementException("No 'Apply Now' buttons found on the page.");
            }

            WebElement displayedApplyNowButton = null;
            for (WebElement button : applyNowButtons) {
                // Check if the button is displayed (visible and has height/width)
                if (button.isDisplayed()) {
                    displayedApplyNowButton = button;
                    break; // Found the displayed button, exit loop
                }
            }

            if (displayedApplyNowButton != null) {
                // Wait for the specific displayed button to be clickable before clicking
                // This ensures it's fully interactable
                scrollToElementPom(displayedApplyNowButton);
                Thread.sleep(2000);
                clickPom(displayedApplyNowButton);
                System.out.println("Clicked the displayed 'Apply Now' button.");
            } else {
                throw new RuntimeException("No 'Apply Now' button found that is currently displayed on the UI among the matched elements.");
            }

        } catch (NoSuchElementException e) {
            System.err.println("Error locating 'Apply Now' button: " + e.getMessage());
            throw new RuntimeException("Failed to locate or click the displayed 'Apply Now' button.", e);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while trying to click the 'Apply Now' button: " + e.getMessage());
            throw new RuntimeException("Failed to click 'Apply Now' button due to an unexpected error.", e);
        }
    }

}