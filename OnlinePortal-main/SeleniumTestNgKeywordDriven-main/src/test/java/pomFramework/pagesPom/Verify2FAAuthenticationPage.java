package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Verify2FAAuthenticationPage extends BasePage {

    public String verify2FAAuthenticationPageUrl = "/Verify";

    // WebElements on the Verify2FAAuthenticationPage, identified using @FindBy annotations.
    @FindBy(xpath = "//input[@id='Code']")
    private WebElement codeInput;

    @FindBy(xpath = "//a[@href='/Account/RefreshLoginOTP']")
    private WebElement resendCodeBtn;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement verifyBtn;

    public Verify2FAAuthenticationPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /**
     * Enter otp and click verify.
     *
     * @param otp
     */
    public void enterOtpClickVerify(String otp) {
        sendKeysPom(codeInput, otp);
        clickPom(verifyBtn);
    }

}
