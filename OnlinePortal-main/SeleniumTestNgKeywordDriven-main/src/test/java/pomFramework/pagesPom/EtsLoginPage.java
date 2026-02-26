package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pomFramework.utilsPom.ConfigReader;

public class EtsLoginPage  extends BasePage {

    public String etsLoginPageUrl = "/Login/Index";
    //ETS info
    private static final String ETS_LOGIN_URL = ConfigReader.getProperty("base.EtsLoginUrl");
    private static final String ETS_USERNAME = ConfigReader.getProperty("test.ets.username");
    private static final String ETS_PASSWORD = ConfigReader.getProperty("test.ets.password");

    // WebElements on the login page, identified using @FindBy annotations.
    @FindBy(id = "userName")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "btnSubmit")
    private WebElement submitBtn;

    @FindBy(xpath = "//a[contains(@class,'Logoutclass')]")
    private WebElement logoutBtn;

    @FindBy(xpath = "//a[@href='/OnlinePortal/PendingApplications']")
    private WebElement onlineStaging;

    public EtsLoginPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    public void loginToEts(){
        navigateToPOM(ETS_LOGIN_URL);
        waitForPageLoadPom();
        waitForVisibilityPom(usernameField);
        sendKeysPom(usernameField , ETS_USERNAME);
        waitForVisibilityPom(passwordField);
        sendKeysPom(passwordField , ETS_PASSWORD);
        clickPom(submitBtn);
        waitForVisibilityPom(logoutBtn);
    }

    public void goToOnlineStaging(){
        clickPom(onlineStaging);
    }

    public void logoutEts(){
        clickPom(logoutBtn);
    }

}
