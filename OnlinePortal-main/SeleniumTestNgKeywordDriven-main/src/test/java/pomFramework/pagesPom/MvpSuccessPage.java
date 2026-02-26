package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class MvpSuccessPage extends BasePage {

    public String successPageUrl = "/Success";
    public MvpSuccessPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    // WebElements on the Success page, identified using @FindBy annotations.
    @FindBy(xpath = "//h3[text()='Success']/following-sibling::p[text()='Application submitted successfully!' and @class='usa-alert__text']")
    private WebElement submissionSuccessToastMsg;

    //Functions
    public boolean isUserOnSuccessUrl(){
        return waitForUrlContainsPom(successPageUrl);
    }
    public void verifyMvpPermitSubmissionSuccessMsgDisplayedSuccesspage() {
        Assert.assertTrue(isElementDisplayedPom(submissionSuccessToastMsg), "Application submitted success msg is not displayed");
    }
}