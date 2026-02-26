package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GovolutionPagePaymentInfoPage extends BasePage {

    public String govolutionPagePaymentInfoUrl = "demo.velocitypayment.com/vrelay/bootstrap/select.do";
    public GovolutionPagePaymentInfoPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    // WebElements on the govolution payment info page, identified using @FindBy annotations.
    @FindBy(id = "billingName")
    private WebElement cardholderNameInput;

    @FindBy(id = "cardNumber")
    private WebElement cardNumberInput;

    @FindBy(id = "spc")
    private WebElement securityCodeInput;

    @FindBy(xpath = "//select[@name='cardExpMonth']")
    private WebElement expirationMonthDropdown;

    @FindBy(xpath = "//select[@name='cardExpYear']")
    private WebElement expirationYearDropdown;

    @FindBy(id = "billingAddress")
    private WebElement addressLine1Input;

    @FindBy(id = "billing-zip-input")
    private WebElement zipCodeInput;

    @FindBy(id = "billing-city-input")
    private WebElement cityInput;

    @FindBy(id = "billingState-select")
    private WebElement stateDropdown;

    @FindBy(id = "emailAddress")
    private WebElement receiptEmailAddr;

    @FindBy(id = "checkedAcceptCondition")
    private WebElement termsAndConditionsCheckBox;

    @FindBy(xpath = "//span[@id='recaptcha-anchor']")
    private WebElement notARobot;

    @FindBy(xpath = "//input[@value='Continue']")
    private WebElement continueBtn;

    //Functions
    public boolean isUserOnGovolutionPagePaymentInfoUrl(){
        return waitForUrlContainsPom(govolutionPagePaymentInfoUrl);
    }

    /**
     * Fills out all mandatory fields and Receipt email address on the Payment Information form.
     */
    public void enterPaymentInfoMandatoryFields(String name, String cardNum, String cvv,
                                     String month, String year, String address,
                                     String zip, String receiptEmail) {

        sendKeysPom(cardholderNameInput, name);
        sendKeysPom(cardNumberInput, cardNum);
        sendKeysPom(securityCodeInput, cvv);
        selectDropdownOptionByVisibleTextPom(expirationMonthDropdown, month);
        selectDropdownOptionByVisibleTextPom(expirationYearDropdown, year);
        sendKeysPom(addressLine1Input, address);
        sendKeysPom(zipCodeInput, zip);
        sendKeysPom(receiptEmailAddr, receiptEmail);

    }

    public void clickTermsAndConditions() throws InterruptedException {
        waitForVisibilityPom(termsAndConditionsCheckBox);
        scrollToElementPom(termsAndConditionsCheckBox);
        Thread.sleep(500);
        clickPom(termsAndConditionsCheckBox);
    }

    public void clickImNotRobot() throws InterruptedException {
        Thread.sleep(2000);
        // 1. Switch to the reCAPTCHA iframe (usually the first one on the page)
        // You can also use By.xpath("//iframe[contains(@title, 'reCAPTCHA')]")
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='reCAPTCHA']")));

        // 2. Now interact with the element
        waitForVisibilityPom(notARobot);
        scrollToAndClickPom(notARobot);

        // 3. IMPORTANT: Switch back to the main content after clicking
        driver.switchTo().defaultContent();
        Thread.sleep(2000);
    }

    public void clickContinueBtn(){
        waitForVisibilityPom(continueBtn);
        clickPom(continueBtn);
    }
}



