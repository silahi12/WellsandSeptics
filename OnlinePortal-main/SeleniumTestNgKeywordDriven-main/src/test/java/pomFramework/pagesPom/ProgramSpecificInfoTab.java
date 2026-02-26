package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pomFramework.driverPom.DriverManagerPom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class ProgramSpecificInfoTab extends BasePage {

    public ProgramSpecificInfoTab() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    // WebElements on the ProgramSpecificInfoTab, identified using @FindBy annotations.
    @FindBy(xpath = "//li[@class='nav-item']/a[text()='Program Specific Info']")
    private WebElement programSpecificInfoTab;

    @FindBy(id="file-tab")
    private WebElement filesTab;

    @FindBy(id="fee-tab")
    private WebElement feeTab;

    @FindBy(xpath = "//h4[text()='Download Permit Files']/following-sibling::ul[@class='list-group']//strong")
    private List<WebElement> fileNamesUnderDownloadPermitFilesSec;

    @FindBy(xpath = "//h4[text()='Upload Permit Files']/following-sibling::div//label")
    private List<WebElement> fileNamesUnderUploadPermitFilesSec;

    @FindBy(xpath = "//a[contains(@id,'userFileNameLabel')]")
    private List<WebElement> userUploadFileNames;

    @FindBy(xpath = "//div[contains(@class,'modal-header')]/h5[@id='myModalLabel']")
    private WebElement uploadModalHeader;

    @FindBy(id="fileSelector")
    private WebElement chooseFileInput;

    @FindBy(id = "UserFileName")
    private WebElement userFileNameInput;

    @FindBy(xpath = "//div[contains(@class,'modal-footer')]//button[normalize-space(text())='Upload']")
    private WebElement uploadButtonInModal;

    @FindBy(xpath = "//label[@id='actionCompletedMessage' and text()='Your file is uploaded']")
    private WebElement uploadSuccessToastMsg;

    @FindBy(id = "nextButton")
    private WebElement nextBtnFilesSubTab;

    @FindBy(id = "feeButton")
    private WebElement nextBtnFeeSubTab;

    @FindBy(id = "feeTotal")
    private WebElement applicationTotalFeeAmount;

    /** ------ Functions --------**/
    public boolean isProgramSpecificTabActive(){
        //Fixing stale element issue as it is taking time to get loaded
        By programSpecificTabLocator = By.xpath("//li[@class='nav-item']/a[text()='Program Specific Info']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(programSpecificTabLocator));
        WebElement programSpecificTabElement = driver.findElement(programSpecificTabLocator);
        return waitForAttributeContains(programSpecificTabElement, "class", "active");
    }

    public boolean isFilesSubTabActive(){
        return validateAttributeContains(filesTab, "class", "active");
    }

    public boolean isFeeSubTabActive(){
        return validateAttributeContains(feeTab, "class", "active");
    }

    public boolean isFileNameDisplayedUnderDownloadPermitFiles(String fileName){
        boolean isDisplayed = false;
        for (WebElement displayedFileName : fileNamesUnderDownloadPermitFilesSec){
            if (getTextPom(displayedFileName).toLowerCase().trim().equals(fileName.toLowerCase().trim())){
                isDisplayed = true;
                break;
            }
        }
        return isDisplayed;
    }

    public boolean isFileNameDisplayedUnderUploadPermitFiles(String fileName){
        boolean isDisplayed = false;
        for (WebElement displayedFileName : fileNamesUnderUploadPermitFilesSec){
            if (getTextPom(displayedFileName).replace("*","").toLowerCase().trim().equals(fileName.toLowerCase().trim())){
                isDisplayed = true;
                break;
            }
        }
        return isDisplayed;
    }

    /**
     * Downloads a permit file by clicking its associated download icon.
     * This function dynamically constructs the locator for the download button
     * based on the provided file name text displayed on the page.
     * It also includes a basic wait for the file to appear in the configured download directory.
     *
     * @param fileNameText The exact visible text of the file name on the UI (e.g., "For Discharges from Mineral Quarries, Borrow Pits, and Concrete and Asphalt Plants Notice of Intent (NOI) File").
     * @param expectedDownloadedFileName The expected name of the file to be downloaded in the file system
     * (e.g., "Permit_NOI.pdf"). Note: This might need to be exact or a pattern,
     * depending on the browser's downloaded file name.
     * @return Path to the downloaded file, or null if download fails/times out.
     */
    public Path downloadPermitFile(String fileNameText, String expectedDownloadedFileName) {
        String downloadDirPath = DriverManagerPom.getDownloadDir();
        Path downloadedFilePath = Paths.get(downloadDirPath, expectedDownloadedFileName);
        File downloadedFile = downloadedFilePath.toFile();

        // Dynamically construct the By locator for the download button based on the file name text
        // Ensure the XPath correctly targets the download button for the given file name text.
        // Based on the screenshot and provided pattern:
        // //h4[text()='Download Permit Files']/following-sibling::ul[@class='list-group']//strong[text()=" + fileName + "]/following-sibling::button
        // Assuming the file name text is inside a <span> and the download icon is a sibling <a> with <i>
        By downloadIconLocator = By.xpath("//h4[text()='Download Permit Files']/following-sibling::ul[@class='list-group']//strong[normalize-space(text())='"+ fileNameText +"']/following-sibling::button");


        // 1. Clean up any existing file with the same name before starting download
        if (downloadedFile.exists()) {
            try {
                Files.delete(downloadedFilePath);
                System.out.println("Cleared existing file: " + expectedDownloadedFileName + " from download directory.");
            } catch (IOException e) {
                System.err.println("Could not delete existing file " + expectedDownloadedFileName + ": " + e.getMessage());
                // Proceed anyway, but log the issue
            }
        }

        // 2. Click the download icon
        try {
            WebElement downloadIcon = wait.until(ExpectedConditions.elementToBeClickable(downloadIconLocator));
            scrollToElementPom(downloadIcon);
            Thread.sleep(2000);
            clickPom(downloadIcon);
            //downloadIcon.click();
            System.out.println("Clicked download icon for file: " + fileNameText);
        } catch (Exception e) {
            System.err.println("Failed to click download icon for " + fileNameText + ". Error: " + e.getMessage());
            throw new RuntimeException("Could not initiate file download for: " + fileNameText, e);
        }

        // 3. Wait for the file to appear in the download directory
        // Use a longer wait for file downloads, as network and server speed can vary
        long downloadTimeoutSeconds = 10; // Adjust as needed
        WebDriverWait fileWait = new WebDriverWait(driver, Duration.ofSeconds(downloadTimeoutSeconds));

        try {
            fileWait.until(wd -> downloadedFile.exists() && downloadedFile.length() > 0);
            System.out.println("File downloaded successfully: " + downloadedFilePath.toAbsolutePath());
            return downloadedFilePath;
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for file '" + expectedDownloadedFileName + "' to download to " + downloadDirPath + ". Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while waiting for file download: " + e.getMessage());
            return null;
        }
    }

    /**
     * Uploads a file for a specific permit form on the "Program Specific Info" page.
     * This opens the upload modal, chooses the file, sets a user-defined file name, and uploads it.
     *
     * @param fileNameTextInTable The exact visible text of the file name in the table row (e.g., "Stormwater Pollution Prevention Plan (SWPPP) File").
     * @param filePathToUpload The absolute path to the file on your local system to be uploaded (e.g., "C:/test_files/myfile.pdf").
     * @param userDefinedFileNameInModal (Optional) The name to enter into the "File Name" field in the modal. Use null or empty string to skip.
     * @throws RuntimeException if the upload process fails at any step.
     */
    public void uploadPermitFile(String fileNameTextInTable, String filePathToUpload, String userDefinedFileNameInModal) {
        // Construct the dynamic XPath for the upload icon (up arrow) in the table row.
        // Assuming the structure is similar to download, but with a different icon/button class.
        // From screenshot, it looks like a similar <a> tag with an upload icon (fa-upload)
        By uploadIconLocator = By.xpath("//button[normalize-space(@data-file-name)='" + fileNameTextInTable.replace(" File","") + "']");

        // Convert the file path to an absolute path to ensure it's accessible by Selenium
        File fileToUpload = new File(filePathToUpload);
        if (!fileToUpload.exists()) {
            throw new RuntimeException("File to upload does not exist: " + filePathToUpload);
        }
        String absoluteFilePath = fileToUpload.getAbsolutePath();
        System.out.println("Attempting to upload file: " + absoluteFilePath);

        try {
            // 1. Click the upload icon in the table to open the modal
            WebElement uploadIcon = driver.findElement(uploadIconLocator);
            scrollToElementPom(uploadIcon);
            Thread.sleep(2000);
            clickPom(uploadIcon);
            System.out.println("Clicked upload icon for: " + fileNameTextInTable);

            // 2. Wait for the upload modal to appear
            wait.until(ExpectedConditions.visibilityOf(uploadModalHeader));
            System.out.println("Upload modal is displayed.");

            // 3. Send the file path to the <input type="file"> element
            // This is how Selenium interacts with native file dialogs
            if (!chooseFileInput.isDisplayed()) {
                // If the input type="file" is not directly visible, sometimes it's hidden
                // and activated via JavaScript. In such cases, executing JavaScript to make it visible
                // or clicking a label associated with it might be needed.
                // For direct sendKeys, it usually needs to be visible.
                System.err.println("Warning: chooseFileInput element might not be directly visible. Attempting sendKeys anyway.");
            }
            chooseFileInput.sendKeys(absoluteFilePath);
            System.out.println("Sent file path to input: " + absoluteFilePath);

            // 4. (Optional) Enter a user-defined file name in the modal
            if (userDefinedFileNameInModal != null && !userDefinedFileNameInModal.isEmpty()) {
                clearAndSendKeys(userFileNameInput, userDefinedFileNameInModal); // Use BasePage's method
                System.out.println("Entered user-defined file name: " + userDefinedFileNameInModal);
            }

            // 5. Click the "Upload" button in the modal
            clickPom(uploadButtonInModal);
            System.out.println("Clicked 'Upload' button in modal.");

            // 6. Wait for the modal to disappear
            waitForElementToDisappear(uploadModalHeader);
            System.out.println("Upload modal disappeared. File upload process completed.");

            // Optional: Add a wait for the file to appear in the table on the main page
            // Example: wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[normalize-space(text())='" + (userDefinedFileNameInModal != null && !userDefinedFileNameInModal.isEmpty() ? userDefinedFileNameInModal : fileToUpload.getName()) + "']")));
            // This XPath would depend on where the uploaded file's name appears in the table.

        } catch (Exception e) {
            System.err.println("Failed to upload file '" + absoluteFilePath + "' for '" + fileNameTextInTable + "'. Error: " + e.getMessage());
            throw new RuntimeException("Could not upload file.", e);
        }
    }

    public boolean userUploadFileNameExists(String userFileName){
        boolean exists = false;
        for (WebElement userUploadFileName : userUploadFileNames){
            if (getTextPom(userUploadFileName).equals(userFileName)){
                exists = true;
                break;
            }
        }
        return exists;
    }

    public void waitUntilSuccessUploadMsgAppear(){
        //wait until toast msg disappears
        waitForVisibilityPom(uploadSuccessToastMsg);
    }

    public void waitUntilSuccessUploadMsgDisappears() throws InterruptedException {
        //wait until toast msg disappears
        waitForElementToDisappear(uploadSuccessToastMsg);
        Thread.sleep(3000);
    }

    public void clickNextBtnFilesSubTab(){
        clickPom(nextBtnFilesSubTab);
    }

    // Fee related functions

    public void chooseFee(String feeType) throws InterruptedException {
        By feeRadioBtnLocator = By.xpath("//label[normalize-space(text())='" + feeType +"']/ancestor::div[contains(@class,'row ')]//input[@type='radio']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(feeRadioBtnLocator));
        wait.until(ExpectedConditions.elementToBeClickable(feeRadioBtnLocator));
        WebElement feeRadioBtn = driver.findElement(feeRadioBtnLocator);
        //scrollToElementPom(feeRadioBtn);
        Thread.sleep(2000);
        clickPom(feeRadioBtn);
        System.out.println("Clicked fee radio btn for: " + feeType);
    }

    public boolean verifyApplicationTotalFee(String expectedTotalFee){
        return verifyText(applicationTotalFeeAmount, expectedTotalFee);
    }

    public void clickNextBtnFeesSubTab(){
        clickPom(nextBtnFeeSubTab);
    }
}
