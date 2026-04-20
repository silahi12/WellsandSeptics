package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class DashboardPage extends BasePage {

    public String dashboardUrl = "https://wellsandsepticsdevinternal.mde.state.md.us/Application/SearchPermitTypes";

    // WebElements on the Dashboard page, identified using @FindBy annotations.
    @FindBy(xpath = "//li[@class='nav-item']/a[@href='/Account/Profile']")
    private WebElement profileBtn;

    @FindBy(xpath = "//a[@href='/Account/Logout']")
    private WebElement logoutBtn;

    @FindBy(xpath = "//h3[text()='My Permit Applications']")
    private WebElement myApplicationsHeader;

    @FindBy(xpath = "//a[@href='/Application/SearchPermitTypes']")
    private WebElement permitTab;

    @FindBy(xpath = "//label[@id='toastMessage' and text()='Application submitted successfully!']")
    private WebElement submissionSuccessToastMsg;

    @FindBy(xpath = "//a[normalize-space()='New Application']")
    private WebElement newApplication;


    public DashboardPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** -----functions----**/
    public boolean isProfileBtnDisplayed() {
        return isElementDisplayedPom(profileBtn);
        }

    public void clickPermitTab(){
        clickPom(permitTab);
    }
    public void clickNewApplication(){
        clickPom(newApplication);
    }
    public boolean isMyApplicationsHeaderDisplayed() {
        return isElementDisplayedPom(myApplicationsHeader);
    }

    public void clickLogout(){
        clickPom(logoutBtn);
    }

    public boolean isUserOnDashboardUrl(){
        return waitForUrlContainsPom(dashboardUrl);
    }

    public void verifyPermitSubmissionSuccessMsgDisplayed(){
        Assert.assertTrue(isElementDisplayedPom(submissionSuccessToastMsg),"Application submitted success msg is not displayed");
        //wait until toast msg disappears
        waitForElementToDisappear(submissionSuccessToastMsg);
    }

}