package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import pomFramework.driverPom.DriverManagerPom;
import io.qameta.allure.Step;

public class WellInfoPage extends BasePage {

    public WellInfoPage() {
        PageFactory.initElements(DriverManagerPom.getDriverPom(), this);
    }

    // --- LOCATORS ---

    @FindBy(id = "WellType")
    private WebElement wellTypeDropdown;

    @FindBy(id = "pumpingrate")
    private WebElement pumpingRateInput;

    @FindBy(id = "avgdailyqty")
    private WebElement avgDailyQtyInput;

    @FindBy(id = "ApproximateDepthofWell")
    private WebElement wellDepthInput;

    @FindBy(id = "ApproximateDiameterofWell")
    private WebElement wellDiameterInput;

    @FindBy(id = "MethodWater")
    private WebElement methodOfDrillingDropdown;

    @FindBy(id = "SourceWater")
    private WebElement sourceOfWaterDropdown;

    // Replacement Radio: "This well will NOT Replace an Existing Well"
    @FindBy(id = "radio-1")
    private WebElement noReplacementRadio;

    // File Upload
    @FindBy(id = "fileInputId")
    private WebElement fileUploadInput;

    // Review Application Button
    @FindBy(id = "saveButton")
    private WebElement reviewApplicationBtn;


    // --- METHODS ---

    @Step("Filling out all mandatory Well Information and submitting")
    public void fillWellInfoAndSubmit(String wellType, String pumpRate, String dailyQty, String depth, String diameter, String drillMethod, String sourceWater, String filePath) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOf(wellTypeDropdown));
        new Select(wellTypeDropdown).selectByVisibleText(wellType);

        pumpingRateInput.clear();
        pumpingRateInput.sendKeys(pumpRate);

        avgDailyQtyInput.clear();
        avgDailyQtyInput.sendKeys(dailyQty);

        wellDepthInput.clear();
        wellDepthInput.sendKeys(depth);

        wellDiameterInput.clear();
        wellDiameterInput.sendKeys(diameter);

        new Select(methodOfDrillingDropdown).selectByVisibleText(drillMethod);
        new Select(sourceOfWaterDropdown).selectByVisibleText(sourceWater);

        // Click the 'No Replacement' radio button using JS (bypasses USWDS hidden input issues)
        JavascriptExecutor js = (JavascriptExecutor) DriverManagerPom.getDriverPom();
        js.executeScript("arguments[0].click();", noReplacementRadio);

        // Upload file by sending the path directly to the input element
        fileUploadInput.sendKeys(filePath);
        Thread.sleep(2000); // Give the UI/spinner a moment to process the file

        // Click Review Application
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", reviewApplicationBtn);
        Thread.sleep(1000);
        try {
            reviewApplicationBtn.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", reviewApplicationBtn);
        }
    }
}