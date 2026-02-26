package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GovolutionPaymentConfirmationPage extends BasePage {

    public String govolutionPaymentConfirmationUrl = "demo.velocitypayment.com/vrelay/bootstrap/processCC.do";
    public GovolutionPaymentConfirmationPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    // WebElements on the govolution payment info page, identified using @FindBy annotations.
    @FindBy(xpath = "//input[@value='Confirm']")
    private WebElement confirmBtn;

    //Functions
    public boolean isUserOnGovolutionPaymentConfirmUrl(){
        return waitForUrlContainsPom(govolutionPaymentConfirmationUrl);
    }
    public void clickConfirmBtn(){
        waitForVisibilityPom(confirmBtn);
        scrollToAndClickPom(confirmBtn);
    }
}
