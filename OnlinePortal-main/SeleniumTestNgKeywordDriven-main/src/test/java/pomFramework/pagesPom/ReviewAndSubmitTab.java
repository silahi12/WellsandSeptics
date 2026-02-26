package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import pomFramework.dataPom.FacilityDetails;
import pomFramework.utilsPom.EmailClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReviewAndSubmitTab extends BasePage {

    public ReviewAndSubmitTab() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    // WebElements on the ReviewAndSubmitTab, identified using @FindBy annotations.
    @FindBy(xpath = "//li[@class='nav-item']/a[text()='Review & Submit']")
    private WebElement ReviewAndSubmitTab;

    @FindBy(xpath = "//label[@for ='Application_StandardPermitInfo_FacilityName']")
    private WebElement facilityName;

    @FindBy(xpath="//label[@for ='Application_StandardPermitInfo_FacilityType']")
    private WebElement facilityType;

    @FindBy(xpath="//b[contains(text(),'Start Date')]/parent::label")
    private WebElement facilityStartDate;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_Line1']")
    private WebElement physicalAddressLine1;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_Line2']")
    private WebElement physicalAddressLine2;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_Line3']")
    private WebElement physicalAddressLine3;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_Municipality']")
    private WebElement physicalAddressMunicipality;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_CountyDesc']")
    private WebElement physicalAddressCounty;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_State']")
    private WebElement physicalAddressState;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_ZipCode']")
    private WebElement physicalAddressZipcode;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_Region']")
    private WebElement physicalAddressRegion;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_PhysicalAddress_FieldOffice']")
    private WebElement physicalAddressFieldOfc;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_Line1']")
    private WebElement mailingAddressLine1;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_Line2']")
    private WebElement mailingAddressLine2;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_Line3']")
    private WebElement mailingAddressLine3;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_Municipality']")
    private WebElement mailingAddressMunicipality;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_CountyDesc']")
    private WebElement mailingAddressCounty;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_State']")
    private WebElement mailingAddressState;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_ZipCode']")
    private WebElement mailingAddressZipcode;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_Region']")
    private WebElement mailingAddressRegion;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_MailingAddress_FieldOffice']")
    private WebElement mailingAddressFieldOfc;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_Latitude']")
    private WebElement xCord;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_Longitude']")
    private WebElement yCord;

    @FindBy(xpath = "//label[@for='Application_StandardPermitInfo_EjScore']")
    private WebElement ejsScore;

    @FindBy(xpath = "//h6[text()='Permit Documents']/following-sibling::div//li")
    private List<WebElement> userUploadFiles;

    @FindBy(id = "SignatureCheckbox_")
    private WebElement signatureCheckbox;

    @FindBy(xpath = "//div[@id='signatureVerification' and @style='display: block;']")
    private WebElement eSignaturePopup;

    @FindBy(xpath = "//input[@id='OTP_Code']")
    private WebElement codeInput;

    @FindBy(xpath = "//button[@id='requestCode']")
    private WebElement resendCodeBtn;

    @FindBy(id = "verifyCode")
    private WebElement verifyBtnOnESignaturePopup;

    @FindBy(xpath = "//div[normalize-space() = 'Signature completed successfully!']")
    private WebElement SignatureCompleteSuccessMsgAlert;

    @FindBy(id = "btnSubmit")
    private WebElement submitBtn;

    /** ------ Functions --------**/
    public boolean isReviewAndSubmitTabActive() throws InterruptedException{
        //return waitForAttributeContains(ReviewAndSubmitTab, "class", "active");
        //Fixing stale element issue as it is taking time to get loaded
        Thread.sleep(2000);
        By reviewAndSubmitTabLocator = By.xpath("//li[@class='nav-item']/a[text()='Review & Submit']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(reviewAndSubmitTabLocator));
        WebElement reviewAndSubmitTabElement = driver.findElement(reviewAndSubmitTabLocator);
        return waitForAttributeContains(reviewAndSubmitTabElement, "class", "active");
    }
    public String getDisplayedFacilityName() { return facilityName.getText(); }
    public String getFacilityType() { return facilityType.getText(); }
    public String getFacilityStartDate() { return facilityStartDate.getText(); }

    public String getPhysicalAddressLine1() { return physicalAddressLine1.getText();}
    public String getPhysicalAddressLine2() { return physicalAddressLine2.getText(); }
    public String getPhysicalAddressLine3() { return physicalAddressLine3.getText(); }
    public String getPhysicalAddressMunicipality() { return physicalAddressMunicipality.getText(); }
    public String getPhysicalAddressCounty() { return physicalAddressCounty.getText(); }
    public String getPhysicalAddressState() { return physicalAddressState.getText(); }
    public String getPhysicalAddressZipcode() { return physicalAddressZipcode.getText(); }
    public String getPhysicalAddressRegion() { return physicalAddressRegion.getText(); }
    public String getPhysicalAddressFieldOffice() { return physicalAddressFieldOfc.getText(); }

    public String getMailingAddressLine1() { return mailingAddressLine1.getText();}
    public String getMailingAddressLine2() { return mailingAddressLine2.getText(); }
    public String getMailingAddressLine3() { return mailingAddressLine3.getText(); }
    public String getMailingAddressMunicipality() { return mailingAddressMunicipality.getText();}
    public String getMailingAddressCounty() { return mailingAddressCounty.getText(); }
    public String getMailingAddressState() { return mailingAddressState.getText(); }
    public String getMailingAddressZipcode() { return mailingAddressZipcode.getText();}
    public String getMailingAddressRegion() { return mailingAddressRegion.getText(); }
    public String getMailingAddressFieldOffice() { return mailingAddressFieldOfc.getText();  }

    public String getXCord() { return xCord.getText(); }
    public String getYCord() { return yCord.getText(); }
    public String getEjsScore() { return ejsScore.getText(); }

    public void verifyFacilityDetails(String facilityPrefix){
        FacilityDetails expectedFacilityDetails = new FacilityDetails(facilityPrefix);
        Assert.assertEquals(getDisplayedFacilityName().replace("Facility Name:","").trim(), expectedFacilityDetails.getFacilityName(), "Facility name ");
        Assert.assertEquals(getFacilityType().replace("Facility Type:","").trim(), expectedFacilityDetails.getFacilityType(), "Facility type ");
        String expectedFacilityStartDate = expectedFacilityDetails.getFacilityStartDate();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(expectedFacilityStartDate, inputFormatter);
        String formattedExpectedFacilityStartDateDateString = date.format(outputFormatter);
        Assert.assertEquals(getFacilityStartDate().replace("Start Date:","").trim(), formattedExpectedFacilityStartDateDateString, "Facility start date ");
    }

    public void verifyFacilityPhysicalAddress(String facilityPrefix){
        FacilityDetails expectedFacilityDetails = new FacilityDetails(facilityPrefix);
        Assert.assertEquals(getPhysicalAddressLine1().replace("Line 1:","").trim(), expectedFacilityDetails.getPhysicalAddressLine1(), "Facility Physical address line 1 ");
        Assert.assertEquals(getPhysicalAddressLine2().replace("Line 2:","").trim(), expectedFacilityDetails.getPhysicalAddressLine2(), "Facility Physical address line 2 ");
        Assert.assertEquals(getPhysicalAddressLine3().replace("Line 3:","").trim(), expectedFacilityDetails.getPhysicalAddressLine3(), "Facility Physical address line 3 ");
        Assert.assertEquals(getPhysicalAddressMunicipality().replace("City:","").trim(), expectedFacilityDetails.getPhysicalAddressMunicipality(), "Facility Physical address Municipality ");
        Assert.assertEquals(getPhysicalAddressCounty().replace("County:","").trim(), expectedFacilityDetails.getPhysicalAddressCounty(), "Facility Physical address county ");
        Assert.assertEquals(getPhysicalAddressState().replace("State:","").trim(), expectedFacilityDetails.getPhysicalAddressState(), "Facility Physical address State ");
        Assert.assertEquals(getPhysicalAddressZipcode().replace("ZIP Code:","").trim(), expectedFacilityDetails.getPhysicalAddressZipcode().trim(), "Facility Physical address Zipcode ");
        //Assert.assertEquals(getPhysicalAddressRegion().replace("Region:","").trim(), expectedFacilityDetails.getPhysicalAddressRegion(), "Facility Physical address Region ");
        //Assert.assertEquals(getPhysicalAddressFieldOffice().replace("Field Office:","").trim(), expectedFacilityDetails.getPhysicalAddressFieldOffice(), "Facility Physical address Field Office ");
    }

    public void verifyFacilityMailingAddress(String facilityPrefix){
        FacilityDetails expectedFacilityDetails = new FacilityDetails(facilityPrefix);
        Assert.assertEquals(getMailingAddressLine1().replace("Line 1:","").trim(), expectedFacilityDetails.getMailingAddressLine1(), "Facility Mailing address line 1 ");
        Assert.assertEquals(getMailingAddressLine2().replace("Line 2:","").trim(), expectedFacilityDetails.getMailingAddressLine2(), "Facility Mailing address line 2 ");
        Assert.assertEquals(getMailingAddressLine3().replace("Line 3:","").trim(), expectedFacilityDetails.getMailingAddressLine3(), "Facility Mailing address line 3 ");
        Assert.assertEquals(getMailingAddressMunicipality().replace("City:","").trim(), expectedFacilityDetails.getMailingAddressMunicipality(), "Facility Mailing address Municipality ");
        Assert.assertEquals(getMailingAddressCounty().replace("County:","").trim(), expectedFacilityDetails.getMailingAddressCounty(), "Facility Mailing address county ");
        Assert.assertEquals(getMailingAddressState().replace("State:","").trim(), expectedFacilityDetails.getMailingAddressState(), "Facility Mailing address State ");
        Assert.assertEquals(getMailingAddressZipcode().replace("ZIP Code:","").trim(), expectedFacilityDetails.getMailingAddressZipcode().trim(), "Facility Mailing address Zipcode ");
        //Assert.assertEquals(getMailingAddressRegion().replace("Region:","").trim(), expectedFacilityDetails.getMailingAddressRegion(), "Facility Mailing address Region ");
        //Assert.assertEquals(getMailingAddressFieldOffice().replace("Field Office:","").trim(), expectedFacilityDetails.getMailingAddressFieldOffice(), "Facility Mailing address Field Office ");
    }

    public void verifyFacilityLocationInfo(String facilityPrefix){
        FacilityDetails expectedFacilityDetails = new FacilityDetails(facilityPrefix);
        Assert.assertEquals(getXCord().replace("Lat:","").trim(), expectedFacilityDetails.getXCord(), "Facility X Cord ");
        Assert.assertEquals(getYCord().replace("Long:","").trim(), expectedFacilityDetails.getYCord(), "Facility Y Cord ");
        String expectedEjsScore = expectedFacilityDetails.getEjsScore();
        double number = Double.parseDouble(expectedEjsScore);
        String formattedExpectedEjsScore = String.format("%.1f", number);
        Assert.assertEquals(getEjsScore().replace("Application EJ Score:","").trim(), formattedExpectedEjsScore, "Facility EJs Score ");
    }

    public boolean userUploadFilesExists(String userFileName){
        boolean exists = false;
        for (WebElement userUploadFileName : userUploadFiles){
            if (getTextPom(userUploadFileName).equals(userFileName)){
                exists = true;
                break;
            }
        }
        return exists;
    }

    public void clickSignatureCheckbox() throws InterruptedException {
        scrollToElementPom(signatureCheckbox);
        Thread.sleep(1000);
        clickPom(signatureCheckbox);
    }

    public boolean isESignaturePopupDisplayed() {
        return isElementDisplayedPom(eSignaturePopup);
    }

    public void clickVerify() {
        clickPom(verifyBtnOnESignaturePopup);
    }

    public void verifySignatureCompleteSuccessMsg(){
        Assert.assertTrue(isElementDisplayedPom(SignatureCompleteSuccessMsgAlert), "Electronic signature complete success message is not displayed");
    }

    /**
     * Submit E-signature
     */
    public void VerifyESignature(String username, String appPassword) throws InterruptedException{
        clickSignatureCheckbox();
        Assert.assertTrue(isESignaturePopupDisplayed(), "Electronic signature popup is not displayed");
        Verify2FAAuthenticationPage verify2FAAuthenticationPage = new Verify2FAAuthenticationPage();
        //Assert.assertTrue(currentUrl.contains(verify2FAAuthenticationPage.verify2FAAuthenticationPageUrl), "User not on 2FA authentication page. Current Url - " + currentUrl);
        EmailClient emailClient = new EmailClient(username, appPassword, 60);
        String otp = emailClient.retrieveLatestOtp();
        //Enter OTP and click Verify
        sendKeysPom(codeInput, otp);
        clickPom(verifyBtnOnESignaturePopup);
        waitForElementToDisappear(eSignaturePopup);
        verifySignatureCompleteSuccessMsg();
    }

    public void clickSubmitBtn() {
        //scrollToElementPom(submitBtn);
        //Thread.sleep(1000);
        //clickPom(submitBtn);
        WebElement submitButton = driver.findElement(By.xpath("//button[@id = 'btnSubmit']"));
        // Use JavaScript to click
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", submitBtn);
    }
}
