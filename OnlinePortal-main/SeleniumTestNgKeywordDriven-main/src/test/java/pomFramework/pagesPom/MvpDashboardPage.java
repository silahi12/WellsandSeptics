package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class MvpDashboardPage extends BasePage {

    public String mvpDashboardUrl = "/Dashboard";

    // WebElements on the Dashboard page, identified using @FindBy annotations.

    @FindBy(xpath = "//button[contains(@onclick,'/Account/Logout')]")
    private WebElement logoutBtn;

    @FindBy(xpath = "//h1[text()='My Permit Applications']")
    private WebElement myApplicationsHeader;

    @FindBy(xpath = "//a[@href='/Application/SearchPermitTypes']")
    private WebElement permitTab;

    @FindBy(xpath = "//h3[text()='Success']/following-sibling::p[text()='Application submitted successfully!' and @class='usa-alert__text']")
    private WebElement submissionSuccessToastMsg;

    @FindBy(xpath = "//td[normalize-space(.)='Application Received']/preceding-sibling::td/a[contains(@aria-label,'View application') and contains(@href,'Review')]")
    private List<WebElement> applicationNums;

    public MvpDashboardPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** -----functions----**/


    public void clickMvpPermitsTab(){
        clickPom(permitTab);
    }

    public boolean isMvpMyApplicationsHeaderDisplayed() {
        return isElementDisplayedPom(myApplicationsHeader);
    }

    public void clickMvpLogout(){
        clickPom(logoutBtn);
    }

    public boolean isUserOnMvpDashboardUrl(){
        return waitForUrlContainsPom(mvpDashboardUrl);
    }

    public void verifyMvpPermitSubmissionSuccessMsgDisplayed(){
        Assert.assertTrue(isElementDisplayedPom(submissionSuccessToastMsg),"Application submitted success msg is not displayed");
        //wait until toast msg disappears
        //MVPTodo: commenting out the below due to bug or new behaviour
        //waitForElementToDisappear(submissionSuccessToastMsg);
    }

    public void waitUntilSuccessAlertDisappear(){
        waitForElementToDisappear(submissionSuccessToastMsg);
    }

    //Get the latest application id from dashboard table
    public String getLatestApplicationID(){
        return getTextPom(applicationNums.get(0));
    }
}