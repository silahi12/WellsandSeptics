package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pomFramework.utilsPom.EmailClient;

public class MvpLoginPage extends BasePage {

    public String mvpLoginPageUrl = "/Account/LoginUser";

    // WebElements on the login page, identified using @FindBy annotations.
    @FindBy(id = "EmailId")
    private WebElement usernameField;

    @FindBy(id = "Password")
    private WebElement passwordField;

    @FindBy(id = "loginButton")
    private WebElement signInButton;

    @FindBy(id = "err_summary")
    private WebElement errorMessage;

    public MvpLoginPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** --------- Functions ------------- **/

    /**
     * Performs a login operation.
     *
     * @param username The username to enter.
     * @param password The password to enter.
     */
    public void mvpLogin(String username, String password) {
        sendKeysPom(usernameField, username);
        sendKeysPom(passwordField, password);
        clickPom(signInButton);
    }

    /**
     * Login with OTP
     */
    public void mvpLoginWithOtp(String username, String password, String appPassword) {
        sendKeysPom(usernameField, username);
        sendKeysPom(passwordField, password);
        clickPom(signInButton);
        //String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Verify2FAAuthenticationPage verify2FAAuthenticationPage = new Verify2FAAuthenticationPage();
        //Assert.assertTrue(currentUrl.contains(verify2FAAuthenticationPage.verify2FAAuthenticationPageUrl), "User not on 2FA authentication page. Current Url - " + currentUrl);
        EmailClient emailClient = new EmailClient(username, appPassword, 60);
        String otp = emailClient.retrieveLatestOtp();
        //Enter OTP and click Verify
        verify2FAAuthenticationPage.enterOtpClickVerify(otp);
    }
    /**
     * Gets the error message displayed on the login page.
     *
     * @return The error message text.
     */
    public String getMvpErrorMessage() {
        //return getTextPom(errorMessage).trim();
        // Get the text of the div and its children
        String fullText = getTextPom(errorMessage);
        // Split the text by newlines and return the last part, which is the message.
        String[] lines = fullText.split("\n");
        return lines[lines.length - 1].trim();
    }

    /**
     * Checks if the error message is displayed.
     *
     * @return true if error message is displayed, false otherwise.
     */
    public boolean isMVPErrorMessageDisplayed() {
        return isElementDisplayedPom(errorMessage);
    }

    public boolean isUserOnMvpLoginUrl(){
        return waitForUrlContainsPom(mvpLoginPageUrl);
    }
}