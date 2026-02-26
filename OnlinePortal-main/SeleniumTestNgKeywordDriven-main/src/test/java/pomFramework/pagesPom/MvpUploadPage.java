package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import java.io.File;
import java.util.List;

public class MvpUploadPage extends BasePage {

    public MvpUploadPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    public String mvpUploadPageUrl = "/File/GetRequiredFiles";

    // WebElements on the upload page, identified using @FindBy annotations.
    @FindBy(xpath = "//h1[@class='usa-heading-xl']")
    private WebElement uploadPageHeader;


    @FindBy(xpath = "//ul[@id='permitFiles']//h3[@class='margin-bottom-1']/span[@class='text-red']/parent::h3")
    private List<WebElement> requiredFileNamesUnderUploadPermitFilesSec;

    @FindBy(id = "fileUploadNextButton")
    private WebElement nextBtn;


    /** -----functions----**/
    public String getHeaderTextOnUploadPageMvp(){
        return getTextPom(uploadPageHeader);
    }

    public void verifyPermitTypeHeaderOnUploadPageMvp(String permitType){
        String actualHeaderText = getHeaderTextOnUploadPageMvp();
        if (actualHeaderText.contains(permitType)){
            System.out.println("Upload page header contains expected permit type " + permitType);
        }else { Assert.fail("FAIL: Upload page header does NOT contain the expected permit type. " +
                "Expected to find: [" + permitType + "], " + "but found: [" + actualHeaderText + "]");}

    }


    public boolean isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(String fileName){
        boolean isDisplayed = false;
        for (WebElement displayedFileName : requiredFileNamesUnderUploadPermitFilesSec){
            if (getTextPom(displayedFileName).split("\\*")[1].toLowerCase().trim().equals(fileName.toLowerCase().trim())){
                isDisplayed = true;
                break;
            }
        }
        return isDisplayed;
    }


    /**
     * Uploads a file for a specific permit form on the "Upload" page.
     * This opens the upload modal, chooses the file and uploads it.
     *
     * @param fileName The exact visible text of the file name  (e.g., "Stormwater Pollution Prevention Plan (SWPPP) File").
     * @param filePathToUpload The absolute path to the file on your local system to be uploaded (e.g., "C:/test_files/myfile.pdf").
     * @throws RuntimeException if the upload process fails at any step.
     */
    public void uploadPermitFileMvp(String fileName, String filePathToUpload) {
        // Construct the dynamic XPath for the upload icon (up arrow) in the table row.
        By uploadLocator = By.xpath("//ul[@id='permitFiles']//input[@value='"+ fileName +"' and @name='fileDesc']/following-sibling::div//div[@class='usa-file-input__target']/input");
        // Convert the file path to an absolute path to ensure it's accessible by Selenium
        File fileToUpload = new File(filePathToUpload);
        if (!fileToUpload.exists()) {
            throw new RuntimeException("File to upload does not exist: " + filePathToUpload);
        }
        String absoluteFilePath = fileToUpload.getAbsolutePath();
        System.out.println("Attempting to upload file: " + absoluteFilePath);

        try {

            // 1. Send the file path to the <input type="file"> element
            // This is how Selenium interacts with native file dialogs
            WebElement chooseFileInput = driver.findElement(uploadLocator);
            chooseFileInput.sendKeys(absoluteFilePath);
            System.out.println("Sent file path to input: " + absoluteFilePath);

        } catch (Exception e) {
            System.err.println("Failed to upload file '" + absoluteFilePath + "' for '" + fileName + "'. Error: " + e.getMessage());
            throw new RuntimeException("Could not upload file.", e);
        }
    }

    /**
     * Uploads additional file for a specific permit form on the "Upload" page.
     * This opens the upload modal, chooses the file and uploads it.
     *.
     * @param filePathToUpload The absolute path to the file on your local system to be uploaded (e.g., "C:/test_files/myfile.pdf").
     * @throws RuntimeException if the upload process fails at any step.
     */
    public void uploadAdditionalFileMvp(String filePathToUpload) {
        // Construct the dynamic XPath for the upload icon (up arrow) in the table row.
        By uploadLocator = By.xpath("//input[@id='additional-files']");
        // Convert the file path to an absolute path to ensure it's accessible by Selenium
        File fileToUpload = new File(filePathToUpload);
        if (!fileToUpload.exists()) {
            throw new RuntimeException("File to upload does not exist: " + filePathToUpload);
        }
        String absoluteFilePath = fileToUpload.getAbsolutePath();
        System.out.println("Attempting to upload file: " + absoluteFilePath);

        try {

            // 1. Send the file path to the <input type="file"> element
            // This is how Selenium interacts with native file dialogs
            WebElement chooseFileInput = driver.findElement(uploadLocator);
            chooseFileInput.sendKeys(absoluteFilePath);
            System.out.println("Sent file path to input: " + absoluteFilePath);

        } catch (Exception e) {
            System.err.println("Failed to upload file '" + absoluteFilePath + "' in additional files section'. Error: " + e.getMessage());
            throw new RuntimeException("Could not upload file.", e);
        }
    }


    public void clickNextBtnOnUploadPageMvp(){
        //scrollToAndClickPom(nextBtn);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nextBtn);
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextBtn);
    }

}

