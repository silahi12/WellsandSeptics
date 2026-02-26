package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

public class MvpFeePage extends BasePage {

    public MvpFeePage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    public String mvpFeePageUrl = "/Fee";

    // WebElements on the Fee page, identified using @FindBy annotations.

    @FindBy(xpath="//span[@class='usa-step-indicator__heading-text']")
    private WebElement stepIndicatorTextOnFeePage;

    @FindBy(xpath = "//h4[text()='Download Permit Files']/following-sibling::ul[@class='list-group']//strong")
    private List<WebElement> fileNamesUnderDownloadPermitFilesSec;

    @FindBy(xpath = "//h4[text()='Upload Permit Files']/following-sibling::div//label")
    private List<WebElement> fileNamesUnderUploadPermitFilesSec;

    @FindBy(xpath = "//a[contains(@id,'userFileNameLabel')]")
    private List<WebElement> userUploadFileNames;

    @FindBy(xpath = "//div[contains(@class,'modal-header')]/h5[@id='myModalLabel']")
    private WebElement uploadModalHeader;


    @FindBy(id = "feeNextButton")
    private WebElement nextBtnFeePage;

    @FindBy(id = "feeTotal")
    private WebElement applicationTotalFeeAmount;

    /** ------ Functions --------**/

    // Fee related functions

    public void waitForFeePageUrl(){
        waitForUrlContainsPom(mvpFeePageUrl);
    }

    public String getStepIndicatorTextOnFeePageMvp(){
        return getTextPom(stepIndicatorTextOnFeePage);
    }

    public void verifyStepIndicatorTextOnFeePageMvp(){
        String actualHeaderText = getStepIndicatorTextOnFeePageMvp();
        if (actualHeaderText.equals("Fee Information")){
            System.out.println("Fee page Step indicator contains expected text");
        }else { Assert.fail("FAIL: Fee page Text indicator header does NOT contain the expected");}

    }

    public void chooseFeeMvp(String feeType) throws InterruptedException {
        By feeRadioBtnLocator = By.xpath("//td[normalize-space()='"+ feeType +"']/preceding-sibling::td//input[@type='radio']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(feeRadioBtnLocator));
        wait.until(ExpectedConditions.elementToBeClickable(feeRadioBtnLocator));
        WebElement feeRadioBtn = driver.findElement(feeRadioBtnLocator);
        //scrollToElementPom(feeRadioBtn);
        Thread.sleep(2000);
        clickPom(feeRadioBtn);
        System.out.println("Clicked fee radio btn for: " + feeType);
    }

    public boolean verifyApplicationTotalFee(String expectedTotalFee){
        return verifyText(applicationTotalFeeAmount, expectedTotalFee);
    }

    public void clickNextBtnFeesSubTab(){
        scrollToAndClickPom(nextBtnFeePage);
    }
}
