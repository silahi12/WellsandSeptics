package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class MvpProcessOverviewPage extends BasePage {

    public String mvpProcessOverviewPageUrl = "Application/ProcessOverview";

    // WebElements on the ProcessOverviewPage, identified using @FindBy annotations.
    @FindBy(xpath = "//h1[contains(@class,'heading')]")
    private WebElement processOverviewPageHeader;

    @FindBy(xpath = "//a[contains(@href,'/File/GetRequiredFiles')]")
    private WebElement nextUploadBtn;


    public MvpProcessOverviewPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** -----functions----**/
    public String getHeaderTextOnProcessOverviewPageMvp(){
        return getTextPom(processOverviewPageHeader);
    }

    public void verifyPermitTypeHeader(String permitType){
        String actualHeaderText = getHeaderTextOnProcessOverviewPageMvp();
        if (actualHeaderText.contains(permitType)){
            System.out.println("Process overview page header contains expected permit type " + permitType);
        }else { Assert.fail("FAIL: Process overview page header does NOT contain the expected permit type. " +
            "Expected to find: [" + permitType + "], " + "but found: [" + actualHeaderText + "]");}

    }

    public void clickNextUploadBtn(){
        clickPom(nextUploadBtn);
    }
}
