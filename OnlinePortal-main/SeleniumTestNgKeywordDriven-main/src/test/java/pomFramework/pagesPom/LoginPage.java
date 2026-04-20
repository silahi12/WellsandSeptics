package pomFramework.pagesPom;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.utilsPom.EmailClient;

import java.time.Duration;

public class LoginPage extends BasePage {

    public String loginPageUrl = "/Account/Login";

    // WebElements on the login page, identified using @FindBy annotations.
    @FindBy(id = "EmailId")
    private WebElement usernameField;

    @FindBy(xpath = "//*[@id='Password']")
    private WebElement passwordField;

    @FindBy(xpath = "//*[@type='submit']")
    private WebElement loginButton;

    @FindBy(id = "err_summary")
    private WebElement errorMessage;

    public LoginPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** --------- Functions ------------- **/

    /**
     * Performs a login operation.
     *
     * @param username The username to enter.
     * @param password The password to enter.
     */
    public void login(String username, String password) {
        sendKeysPom(usernameField, username);

        sendKeysPom(passwordField, password);
        clickPom(loginButton);
    }

    /**
     * Login with OTP
     */
    public void loginWithOtp(String username, String password, String appPassword) {
        sendKeysPom(usernameField, username);
        sendKeysPom(passwordField, password);
        clickPom(loginButton);
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
    public String getErrorMessage() {
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
    public boolean isErrorMessageDisplayed() {
        // Wait up to 5 seconds for the element to become visible before returning true/false
        try {
            WebDriverWait wait = new WebDriverWait(DriverManagerPom.getDriverPom(), Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isUserOnLoginUrl(){
        return waitForUrlContainsPom(loginPageUrl);
    }
}