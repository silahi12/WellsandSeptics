package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GovolutionPagePaymentMethod extends BasePage {

    public String govolutionPageUrl = "demo.velocitypayment.com/vrelay/verify.do";
    public GovolutionPagePaymentMethod() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    // WebElements on the govolution page, identified using @FindBy annotations.
    @FindBy(id = "CC")
    private WebElement payByCCRadio;

    @FindBy(id = "payNowSubmit")
    private WebElement makePayment;

    //Functions
    public boolean isUserOnGovolutionPagePaymentMethodUrl(){
        return waitForUrlContainsPom(govolutionPageUrl);
    }

    public void selectCreditCardPayment() {
        // Check if it's already selected; if not, click it
        if (!payByCCRadio.isSelected()) {
            payByCCRadio.click();
        }
    }

    public void clickMakePayment(){
        waitForVisibilityPom(makePayment);
        clickPom(makePayment);
    }


}
