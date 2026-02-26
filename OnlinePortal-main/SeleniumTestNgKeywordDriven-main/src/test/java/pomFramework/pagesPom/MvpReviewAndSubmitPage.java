package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MvpReviewAndSubmitPage extends BasePage {

    public MvpReviewAndSubmitPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }
    public String mvpReviewPageUrl = "/Review";

    // WebElements on the ReviewAndSubmitTab, identified using @FindBy annotations.

    @FindBy(xpath = "//p[text()='Submitter Name']/following-sibling::p[1]")
    private WebElement applicantName;

    @FindBy(xpath="//p[text()='Submitter Email']/following-sibling::p[1]")
    private WebElement applicantEmail;

    @FindBy(xpath="//p[contains(normalize-space(),'Application Fee')]/following-sibling::p[1]")
    private WebElement applicantionFee;

    @FindBy(xpath="//ul[contains(@class, 'submit-docs')]//a")
    private List<WebElement> userUploadFiles;

    @FindBy(id = "submitButton")
    private WebElement submitBtn;

    /** ------ Functions --------**/
    public boolean verifyApplicantNameMvp(String expectedName) throws InterruptedException {
        Thread.sleep(2000);
        String actualName = applicantName.getText().trim();
        System.out.println("Actual Applicant Name: " + actualName);
        return actualName.equalsIgnoreCase(expectedName);
    }

    public boolean verifyApplicantEmailMvp(String expectedEmail) {
        String actualEmail = applicantEmail.getText().trim();
        System.out.println("Actual Applicant Email: " + actualEmail);
        return actualEmail.equalsIgnoreCase(expectedEmail);
    }

    public boolean verifyApplicationFeeMvp(String expectedFee) {
        String actualFee = applicantionFee.getText().trim();
        System.out.println("Actual Applicantion Fee: " + actualFee);
        return actualFee.equalsIgnoreCase(expectedFee);
    }

    public boolean userUploadedFileExistsMvp(String userFileName){
        boolean exists = false;
        for (WebElement userUploadFileName : userUploadFiles){
            if (getTextPom(userUploadFileName).trim().equals(userFileName)){
                exists = true;
                break;
            }
        }
        return exists;
    }


    public void clickSubmitBtn() throws InterruptedException {
        scrollToElementPom(submitBtn);
        Thread.sleep(1000);
        clickPom(submitBtn);

    }

    public void waitForReviewPageUrl(){
        waitForUrlContainsPom(mvpReviewPageUrl);
    }
}
