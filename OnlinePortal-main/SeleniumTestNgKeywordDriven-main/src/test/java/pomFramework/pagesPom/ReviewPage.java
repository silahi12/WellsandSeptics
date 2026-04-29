package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pomFramework.driverPom.DriverManagerPom;
import io.qameta.allure.Step;

public class ReviewPage extends BasePage {

    public ReviewPage() {
        PageFactory.initElements(DriverManagerPom.getDriverPom(), this);
    }

    // --- LOCATORS ---

    @FindBy(id = "legal-agreement")
    private WebElement legalAgreementCheckbox;

    @FindBy(id = "signature-name")
    private WebElement signatureInput;

    @FindBy(id = "saveSignature-btn")
    private WebElement submitAndPayBtn;

    // --- METHODS ---

    @Step("Signing the application and clicking Submit & Pay")
    public void signAndSubmit(String signatureName) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();

        // 1. Scroll down to the electronic signature section
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", legalAgreementCheckbox);
        Thread.sleep(1000);

        // 2. Check the legal agreement checkbox
        // Using JS click because USWDS checkboxes often hide the actual <input> tag
        js.executeScript("arguments[0].click();", legalAgreementCheckbox);

        // Wait a brief moment for the site's JS to remove the 'readonly' attribute from the text box
        Thread.sleep(500);

        // 3. Enter the signature
        signatureInput.clear();
        signatureInput.sendKeys(signatureName);

        // Ensure the site's JavaScript recognizes the text was typed so it enables the button
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", signatureInput);
        Thread.sleep(1000);

        // 4. Click Submit & Pay
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", submitAndPayBtn);

        // Wait for the button to become enabled
        wait.until(ExpectedConditions.elementToBeClickable(submitAndPayBtn));

        try {
            submitAndPayBtn.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", submitAndPayBtn);
        }
    }
}