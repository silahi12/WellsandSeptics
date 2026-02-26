package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class EtsOnlineStagingPage extends BasePage {

    public String etsOnlineStagingPageUrl = "/OnlinePortal/PendingApplications";


    // WebElements on the login page, identified using @FindBy annotations.
    @FindBy(xpath = "//h3[text()='Online Portal Staging Area']")
    private WebElement onlineStagingHeader;

    @FindBy(xpath = "//input[@value='ONLINE_ID']/following-sibling::input")
    private WebElement onlineIdColumnFilterField;

    @FindBy(xpath = "//a[@title='View Application']")
    private WebElement viewAppSearchIcon;


    public EtsOnlineStagingPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    public void waitForOnlineStagingPage(){
        waitForUrlContainsPom(etsOnlineStagingPageUrl);
        waitForPageLoadPom();
        waitForVisibilityPom(onlineStagingHeader);
    }

    public void searchAppID(String appID){
        sendKeysPom(onlineIdColumnFilterField , appID);
        onlineIdColumnFilterField.sendKeys(Keys.ENTER);
        waitForSingleSearchResult();

    }

    public void waitForSingleSearchResult() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Waits until the number of rows (tr) in the tbody is exactly 1
        wait.until(d -> driver.findElements(By.xpath("//tbody/tr")).size() == 1);
    }

    public void searchAndViewApp(String appId){
        searchAppID(appId);
        clickPom(viewAppSearchIcon);
    }
}
