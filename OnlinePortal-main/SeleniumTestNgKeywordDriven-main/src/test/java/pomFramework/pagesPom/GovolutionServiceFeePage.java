package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GovolutionServiceFeePage extends BasePage {

    public String govolutionServiceFeeUrl = "demo.velocitypayment.com/vrelay/bootstrap/processCC.do";

    public GovolutionServiceFeePage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    // WebElements on the govolution payment info page, identified using @FindBy annotations.
    @FindBy(xpath = "//input[@value='Accept Fee and Process Payment']")
    private WebElement acceptFeeBtn;

    //Functions
    public boolean isUserOnGovolutionServicefeeUrl(){
        return waitForUrlContainsPom(govolutionServiceFeeUrl);
    }
    public void clickAcceptFeeBtn(){
        waitForVisibilityPom(acceptFeeBtn);
        scrollToAndClickPom(acceptFeeBtn);
    }
}