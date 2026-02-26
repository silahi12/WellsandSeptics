package pomFramework.testsPom;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.*;
import pomFramework.utilsPom.ConfigReader;

import java.io.File;
import java.util.List;

@Listeners(TestAllureListenerPom.class)
public class Mvp_HappyPath_RefuseDisposal_IndustrialLandfill_New extends BaseTestPom{

    // Define constants for property keys for easy reference and to avoid typos
    private static final String BASE_URL = ConfigReader.getProperty("base.mvpLonginUrl");

    //User Info
    private static final String STANDARD_USERNAME = ConfigReader.getProperty("test.username"); // User should have a facility associated with all required fields
    private static final String STANDARD_PASSWORD = ConfigReader.getProperty("test.password");
    private static final String STANDARD_APP_PASSWORD = ConfigReader.getProperty("test.appPassword");
    private static final String STANDARD_APPLICANTNAME = ConfigReader.getProperty("test.applicantName");

    //Permit info
    private static final String PERMIT_TYPE = "Refuse Disposal Permit";
    private static final String PERMIT_DETAIL_TYPE = "Refuse Disposal Permit - Industrial Landfill";
    private static final String PERMIT_ACTION_TYPE = "New";

    //Files info
    //Download files
    private static final List<String> DOWNLOAD_FILES = List.of("Refuse Disposal Permit Application - LMA File");
    private static final List<String> DOWNLOAD_FILES_PDF_NAMES = List.of("Refuse Disposal Permit Application - LMA.pdf");
    //Upload Files
    private static final String REQUIRED_UPLOAD_FILE1 = "Refuse Disposal Permit Application - LMA";
    private static final String REQUIRED_UPLOAD_FILE2 = "Hydrogeological assessment of the site";
    private static final String REQUIRED_UPLOAD_FILE3 = "Conceptual facility plan";
    private static final String REQUIRED_UPLOAD_FILE4 = "Detailed engineering plan";
    private static final String REQUIRED_UPLOAD_FILE5 = "Engineering Specifications";
    private static final String REQUIRED_UPLOAD_FILE6 = "Operating manual for the proposed landfill";

    //Fees
    private static final String TOTAL_FEE = "NO FEE";

    @Test(description = "Happy path test for applying Refuse Disposal Permit - Industrial Landfill - New")
    public void mvpHappyPathRefuseDisposalIndustrialLandfillNew() throws InterruptedException {
        MvpLoginPage mvpLoginPage = new MvpLoginPage();
        MvpDashboardPage mvpDashboardPage = new MvpDashboardPage();
        MvpSearchPermitsPage mvpSearchPermitsPage = new MvpSearchPermitsPage();
        MvpPermitInfoPage mvpPermitInfoPage = new MvpPermitInfoPage();
        MvpUploadPage mvpUploadPage = new MvpUploadPage();
        MvpReviewAndSubmitPage mvpReviewAndSubmitPage = new MvpReviewAndSubmitPage();

        mvpLoginPage.navigateToPOM(BASE_URL);

        //Login
        //loginPage.loginWithOtp(STANDARD_USERNAME, STANDARD_PASSWORD, STANDARD_APP_PASSWORD);
        mvpLoginPage.mvpLogin(STANDARD_USERNAME, STANDARD_PASSWORD);
        // Assert that login was successful (e.g., check for URL change and element on next page)
        String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(mvpDashboardPage.isUserOnMvpDashboardUrl(), "Login was not successful! Current Url - " + currentUrl);
        System.out.println("Test Successful Login Passed!");

        //Click on Permits Tab
        mvpDashboardPage.clickMvpPermitsTab();
        //Assert user is on permit search page (e.g., check for URL change and element on next page)
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(mvpSearchPermitsPage.mvpSearchPermitsPageUrl), "User not on Search permits page. Current Url - " + currentUrl);
        Assert.assertTrue(mvpSearchPermitsPage.isMvpSearchPermitPageHeaderDisplayed(), "Incorrect search permits page header");
        System.out.println("User is on Search permits page");

        //Search for Permit type
        mvpSearchPermitsPage.mvpSearch(PERMIT_TYPE);
        mvpSearchPermitsPage.clickMvpLearnMoreButtonForPermitType(PERMIT_TYPE);
        //Assert User is on permit info page
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(mvpPermitInfoPage.mvpPermitInfoPageUrl), "User not on Permit info page. Current Url - " + currentUrl);
        Assert.assertEquals(mvpPermitInfoPage.getHeaderTextMvp().trim(), PERMIT_TYPE.trim(), "Incorrect header on Permit info page");

        //select Permit detail type and action type
        mvpPermitInfoPage.selectPermitDetailTypeMvp(PERMIT_DETAIL_TYPE);
        mvpPermitInfoPage.selectPermitActionTypeMvp(PERMIT_ACTION_TYPE);
        //Verify all expected download files are displayed
        mvpPermitInfoPage.waitForDownloadsectionToBeVisible();
        mvpPermitInfoPage.verifyFilesInDownloadSection(DOWNLOAD_FILES);
        //Verify files are downloadable without error
        int numberOfFiles = DOWNLOAD_FILES.size();
        for (int i = 0; i < numberOfFiles; i++) {
            Assert.assertNotEquals(mvpPermitInfoPage.downloadPermitFileMvp(DOWNLOAD_FILES.get(i), DOWNLOAD_FILES_PDF_NAMES.get(i)), null, "Unable to download or wrong file was downloaded for " + DOWNLOAD_FILES.get(i));
        }

        //Click on Start application button on top
        mvpPermitInfoPage.clickStartAppOnTopMvp();

        //Verify user is on Upload page
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(mvpUploadPage.mvpUploadPageUrl), "User not on Upload page. Current Url - " + currentUrl);
        //verify header on process overview page
        mvpUploadPage.verifyPermitTypeHeaderOnUploadPageMvp(PERMIT_TYPE);

        //Verify under Upload permit files section REQUIRED_UPLOAD_FILEis displayed
        Assert.assertTrue(mvpUploadPage.isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(REQUIRED_UPLOAD_FILE1), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE1 + " is not displayed");

        // Upload files for REQUIRED_UPLOAD_FILE1
        // Define the name of the file you want to upload from your test_files directory
        String localFileName1 = "19CM-NOI.pdf"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath1 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName1;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE1 , filePath1 );

        //Verify under Upload permit files section REQUIRED_UPLOAD_FILEis displayed
        Assert.assertTrue(mvpUploadPage.isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(REQUIRED_UPLOAD_FILE2), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE2 + " is not displayed");

        // Upload files for REQUIRED_UPLOAD_FILE1
        // Define the name of the file you want to upload from your test_files directory
        String localFileName2 = "file-sample_100kB.docx"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath2 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName2;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE2 , filePath2 );

        //Verify under Upload permit files section REQUIRED_UPLOAD_FILEis displayed
        Assert.assertTrue(mvpUploadPage.isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(REQUIRED_UPLOAD_FILE3), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE3 + " is not displayed");

        // Upload files for REQUIRED_UPLOAD_FILE1
        // Define the name of the file you want to upload from your test_files directory
        String localFileName3 = "Tidal_JPA_Form.pdf"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath3 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName3;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE3 , filePath3 );

        //Verify under Upload permit files section REQUIRED_UPLOAD_FILEis displayed
        Assert.assertTrue(mvpUploadPage.isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(REQUIRED_UPLOAD_FILE4), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE4 + " is not displayed");

        // Upload files for REQUIRED_UPLOAD_FILE1
        // Define the name of the file you want to upload from your test_files directory
        String localFileName4 = "excelTest.xlsx"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath4 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName4;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE4 , filePath4 );

        //Verify under Upload permit files section REQUIRED_UPLOAD_FILEis displayed
        Assert.assertTrue(mvpUploadPage.isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(REQUIRED_UPLOAD_FILE5), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE5 + " is not displayed");

        // Upload files for REQUIRED_UPLOAD_FILE1
        // Define the name of the file you want to upload from your test_files directory
        String localFileName5 = "sample3.txt"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath5 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName5;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE5 , filePath5 );

        //Verify under Upload permit files section REQUIRED_UPLOAD_FILEis displayed
        Assert.assertTrue(mvpUploadPage.isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(REQUIRED_UPLOAD_FILE6), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE6 + " is not displayed");

        // Upload files for REQUIRED_UPLOAD_FILE1
        // Define the name of the file you want to upload from your test_files directory
        String localFileName6 = "images.jpg"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath6 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName6;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE6 , filePath6 );

        mvpUploadPage.clickNextBtnOnUploadPageMvp();

        //Verify user is on review page
        mvpReviewAndSubmitPage.waitForReviewPageUrl();
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(mvpReviewAndSubmitPage.mvpReviewPageUrl), "User not on Review page. Current Url - " + currentUrl);

        //Verify User details
        Assert.assertTrue(mvpReviewAndSubmitPage.verifyApplicantNameMvp(STANDARD_APPLICANTNAME), "Applicant Name mismatch!");
        Assert.assertTrue(mvpReviewAndSubmitPage.verifyApplicantEmailMvp(STANDARD_USERNAME), "Applicant Email mismatch!");
        Assert.assertTrue(mvpReviewAndSubmitPage.verifyApplicationFeeMvp(TOTAL_FEE), "Application fee mismatch!");
        Assert.assertTrue(mvpReviewAndSubmitPage.userUploadedFileExistsMvp(localFileName1), "User upload file name mismatch!");

        mvpReviewAndSubmitPage.clickSubmitBtn();

        //Verify success msg
        mvpDashboardPage.verifyMvpPermitSubmissionSuccessMsgDisplayed();
        mvpDashboardPage.clickMvpLogout();

    }
}
