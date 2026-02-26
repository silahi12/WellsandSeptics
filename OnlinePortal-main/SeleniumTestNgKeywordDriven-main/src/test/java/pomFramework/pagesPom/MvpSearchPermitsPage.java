package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class MvpSearchPermitsPage extends BasePage {

    public String mvpSearchPermitsPageUrl = "Application/SearchPermitTypes";

    // WebElements on the Search permits page, identified using @FindBy annotations.
    @FindBy(xpath = "//h1[text()='Search for your permit']")
    private WebElement searchPermitPageHeader;

    @FindBy(xpath = "//input[@name='searchTerm']")
    private WebElement searchBarField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchBtn;


    public MvpSearchPermitsPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /**
     * -----functions----
     **/
    public boolean isMvpSearchPermitPageHeaderDisplayed() {
        return isElementDisplayedPom(searchPermitPageHeader);
    }

    public void mvpSearch(String searchTerm) {
        sendKeysPom(searchBarField, searchTerm);
        clickPom(searchBtn);
    }

    /**
     * Clicks the "Learn more" button for a specific permit type on the Permit Search page.
     * The method constructs the XPath dynamically using the provided permit type.
     *
     * @param permitType The visible text of the permit type (e.g., "General Discharge Permit").
     * @throws RuntimeException if the details button is not found or cannot be clicked.
     */
    public void clickMvpLearnMoreButtonForPermitType(String permitType) {
        String currentPermitType = permitType;
        boolean clicked = false;

        try {
            // Attempt 1: Try with the literal input provided
            clicked = attemptClick(currentPermitType);

            // Attempt 2: If first try failed and it contains an ampersand, try with &amp;
            if (!clicked && permitType.contains("&")) {
                currentPermitType = permitType.replace("&", "&amp;");
                System.out.println("Literal '&' failed. Retrying with: " + currentPermitType);
                clicked = attemptClick(currentPermitType);
            }

            if (!clicked) {
                throw new Exception("Element not found with literal or escaped ampersand.");
            }

        } catch (Exception e) {
            System.err.println("Failed to click 'Learn more' button for Permit Type: '" + permitType + "'. Error: " + e.getMessage());
            throw new RuntimeException("Could not click learn more button for permit type: " + permitType, e);
        }
    }

    /**
     * Helper method to encapsulate the wait and click logic.
     * Returns true if clicked successfully, false if the element was not found.
     */
    private boolean attemptClick(String permitText) {

        try {
            // Using normalize-space to handle potential newline/spacing issues seen in DevTools
            By locator = By.xpath("//a[contains(@aria-label, 'Learn more about') and contains(normalize-space(@aria-label), '" + permitText + "')]");
            WebElement learnMoreButton = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            // 2. Scroll into view using JS to ensure it's centered
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", learnMoreButton);

            // 3. Wait until it's actually clickable (not obscured)
            wait.until(ExpectedConditions.elementToBeClickable(learnMoreButton));
            //scrollToElementPom(learnMoreButton);
            //learnMoreButton.click();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", learnMoreButton);
            System.out.println("Successfully clicked button for: " + permitText);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *checks the download folder for expected filename
     * @param expectedDownloadedFileName The expected name of the file to be downloaded in the file system
     * (e.g., "Permit_NOI.pdf"). Note: This might need to be exact or a pattern,
     * depending on the browser's downloaded file name.
     * @return Path to the downloaded file, or null if download fails/times out.
     */
    public Path downloadPermitFileGeneralMvp(String expectedDownloadedFileName) {
        String downloadDirPath = DriverManagerPom.getDownloadDir();
        Path downloadedFilePath = Paths.get(downloadDirPath, expectedDownloadedFileName);
        File downloadedFile = downloadedFilePath.toFile();

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

        // 2. Wait for the file to appear in the download directory
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

}