package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pomFramework.driverPom.DriverManagerPom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class MvpPermitInfoPage extends BasePage {

    public String mvpPermitInfoPageUrl = "Application/PermitInfoDetails";

    // WebElements on the PermitInfoPage, identified using @FindBy annotations.
    @FindBy(xpath = "//h1[contains(@class,'heading')]")
    private WebElement permitInfoPageHeader;

    @FindBy(xpath = "//select[@id='SelectedPermit']")
    private WebElement permitDetailTypeDropdown;

    @FindBy(xpath = "//select[contains(@id,'permit-actions-')]")
    private WebElement permitActionTypeDropdown;

    @FindBy(xpath = "//div[@class='permit-files-container']//ul")
    private WebElement downloadSection;

    @FindBy(xpath = "//a[@id='applyPermitTop']")
    private List<WebElement> startApplicationButtonsOnTop;



    public MvpPermitInfoPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** -----functions----**/
    public String getHeaderTextMvp(){
        return getTextPom(permitInfoPageHeader);
    }

    public void selectPermitDetailTypeMvp(String permitDetailType) throws InterruptedException {
        Thread.sleep(1500);
        boolean dropdown = validateAttributeContains(permitDetailTypeDropdown, "style", "block");
        if (dropdown) {selectDropdownOptionByVisibleTextPom(permitDetailTypeDropdown, permitDetailType);}
        else{
            By permitDetailTypeLocator = By.xpath("//div[@id='permit-type-question']/fieldset//label[normalize-space(.)='"+ permitDetailType +"']");
            try {
                // Wait for the element to be visible and clickable before clicking
                WebElement permitDetailTypeRadioButton = wait.until(ExpectedConditions.elementToBeClickable(permitDetailTypeLocator));
                permitDetailTypeRadioButton.click();
                System.out.println("Clicked Permit detail Type Radio button for Permit detail Type: '" + permitDetailType + "'");
            } catch (Exception e) {
                System.err.println("Failed to click Permit detail Type Radio button for Permit detail Type: '" + permitDetailType + "'. Error: " + e.getMessage());
                throw new RuntimeException("Could not click Permit detail Type Radio button for Permit detail Type: " + permitDetailType, e);
            }

        }
    }

    public void selectPermitActionTypeMvp(String permitActionType) throws InterruptedException {
        Thread.sleep(1500);
        By permitActionTypeRadioParentLocator = By.xpath("//div[@id='action-type-question']/fieldset");
        wait.until(ExpectedConditions.presenceOfElementLocated(permitActionTypeRadioParentLocator));
        List<WebElement> labelElementsList = driver.findElements(permitActionTypeRadioParentLocator);
        boolean radioBtn = false;
        for (WebElement parentElement : labelElementsList) {
            radioBtn = validateAttributeContains(parentElement, "style", "block");
            if (radioBtn){ break;}
        }
        if (radioBtn) {
            By permitActionTypeRadioLocator = By.xpath("//div[@id='action-type-question']/fieldset[contains(@style, 'block')]//label[normalize-space(.)='"+ permitActionType +"']");

            try {
                // Wait for the element to be visible and clickable before clicking
                WebElement permitActionTypeRadioButton = wait.until(ExpectedConditions.visibilityOfElementLocated(permitActionTypeRadioLocator));
                scrollToElementPom(permitActionTypeRadioButton);
                wait.until(ExpectedConditions.elementToBeClickable(permitActionTypeRadioLocator));
                permitActionTypeRadioButton.click();
                System.out.println("Clicked Permit Action Type Radio button for Permit Action Type: '" + permitActionType + "'");
            } catch (Exception e) {
                System.err.println("Failed to click Permit Action Type Radio button for Permit Action Type: '" + permitActionType + "'. Error: " + e.getMessage());
                throw new RuntimeException("Could not click Permit Action Type Radio button for Permit Action Type: " + permitActionType, e);
            }
        }
        else{
            selectPermitActionTypeDropdownMvp(permitActionType);
        }
        Thread.sleep(2000);
    }

    public void selectPermitActionTypeContainsMvp(String permitActionType) throws InterruptedException {
        Thread.sleep(1500);
        By permitActionTypeRadioParentLocator = By.xpath("//div[@id='action-type-question']/fieldset");
        wait.until(ExpectedConditions.presenceOfElementLocated(permitActionTypeRadioParentLocator));
        List<WebElement> labelElementsList = driver.findElements(permitActionTypeRadioParentLocator);
        boolean radioBtn = false;
        for (WebElement parentElement : labelElementsList) {
            radioBtn = validateAttributeContains(parentElement, "style", "block");
            if (radioBtn){ break;}
        }
        if (radioBtn) {
            By permitActionTypeRadioLocator = By.xpath("//div[@id='action-type-question']/fieldset[contains(@style, 'block')]//label[contains(normalize-space(.),'"+ permitActionType +"')]");

            try {
                // Wait for the element to be visible and clickable before clicking
                WebElement permitActionTypeRadioButton = wait.until(ExpectedConditions.elementToBeClickable(permitActionTypeRadioLocator));
                permitActionTypeRadioButton.click();
                System.out.println("Clicked Permit Action Type Radio button for Permit Action Type: '" + permitActionType + "'");
            } catch (Exception e) {
                System.err.println("Failed to click Permit Action Type Radio button for Permit Action Type: '" + permitActionType + "'. Error: " + e.getMessage());
                throw new RuntimeException("Could not click Permit Action Type Radio button for Permit Action Type: " + permitActionType, e);
            }
        }
        else{
            selectPermitActionTypeDropdownMvp(permitActionType);
        }
        Thread.sleep(2000);
    }

    public void selectPermitActionTypeDropdownMvp(String permitActionType){
        selectDropdownOptionByVisibleTextPom(permitActionTypeDropdown, permitActionType);
    }

    public void waitForDownloadsectionToBeVisible(){
        waitForVisibilityPom(downloadSection);
        scrollToElementPom(downloadSection);
    }

    public void verifyFilesInDownloadSection(List<String> fileNames) {
        //verify each filename exists
        for (String fileName : fileNames) {
            String dynamicXpath = "//div[@class='permit-files-container']//ul//a[normalize-space(@title)='Download " + fileName + "']";
            By fileLocator = By.xpath(dynamicXpath);
            // We use findElements() because it returns a list of size 0 if not found,
            // instead of throwing an exception, which allows us to handle the failure gracefully.
            List<WebElement> elementsFound = driver.findElements(fileLocator);
            if (elementsFound.isEmpty()) {
                // If the list is empty (size 0), the file element was not found.
                String errorMessage = String.format("Error: The expected file '%s' was NOT found in the download section.", fileName);

                // Throw an error to fail the test immediately
                throw new AssertionError(errorMessage);

            } else {
                // Element found (list size > 0)
                System.out.println("Successfully verified file: " + fileName);
            }
        }

        System.out.println("\nAll " + fileNames.size() + " files successfully verified.");
    }

    /**
     * Downloads a permit file by clicking on file name.
     * This function dynamically constructs the locator for the file name download link
     * It also includes a basic wait for the file to appear in the configured download directory.
     *
     * @param fileNameText The exact visible text of the file name on the UI (e.g., "For Discharges from Mineral Quarries, Borrow Pits, and Concrete and Asphalt Plants Notice of Intent (NOI) File").
     * @param expectedDownloadedFileName The expected name of the file to be downloaded in the file system
     * (e.g., "Permit_NOI.pdf"). Note: This might need to be exact or a pattern,
     * depending on the browser's downloaded file name.
     * @return Path to the downloaded file, or null if download fails/times out.
     */
    public Path downloadPermitFileMvp(String fileNameText, String expectedDownloadedFileName) {
        String downloadDirPath = DriverManagerPom.getDownloadDir();
        Path downloadedFilePath = Paths.get(downloadDirPath, expectedDownloadedFileName);
        File downloadedFile = downloadedFilePath.toFile();

        // Dynamically construct the By locator for the file name download link
        By filenameDownloadLink = By.xpath("//div[@class='permit-files-container']//ul//a[normalize-space(@title)='Download " + fileNameText + "']");


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

        // 2. Click on the file name download link
        try {
            WebElement downloadLink = wait.until(ExpectedConditions.elementToBeClickable(filenameDownloadLink));
            scrollToElementPom(downloadLink);
            Thread.sleep(2000);
            clickPom(downloadLink);
            //downloadLink.click();
            Thread.sleep(2000);
            System.out.println("Clicked download Link for file: " + fileNameText);
        } catch (Exception e) {
            System.err.println("Failed to click download icon for " + fileNameText + ". Error: " + e.getMessage());
            throw new RuntimeException("Could not initiate file download for: " + fileNameText, e);
        }

        // 3. Wait for the file to appear in the download directory
        // Use a longer wait for file downloads, as network and server speed can vary
        long downloadTimeoutSeconds = 20; // Adjust as needed
        WebDriverWait fileWait = new WebDriverWait(driver, Duration.ofSeconds(downloadTimeoutSeconds));

        try {
            fileWait.until(wd -> downloadedFile.exists() && downloadedFile.length() > 0);
            System.out.println("File downloaded successfully: " + downloadedFilePath.toAbsolutePath());
            return downloadedFilePath;
        } catch (TimeoutException e) {
            File dir = new File(downloadDirPath);
            String[] files = dir.list();
            System.err.println("TIMEOUT: Looking for: " + expectedDownloadedFileName);
            System.err.println("ACTUAL files in directory: " + (files != null ? Arrays.toString(files) : "Directory Empty"));
            System.err.println("Timeout waiting for file '" + expectedDownloadedFileName + "' to download to " + downloadDirPath + ". Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while waiting for file download: " + e.getMessage());
            return null;
        }
    }


    public void clickStartAppOnTopMvp() {
        clickVisibleElementPom(startApplicationButtonsOnTop);
    }


}