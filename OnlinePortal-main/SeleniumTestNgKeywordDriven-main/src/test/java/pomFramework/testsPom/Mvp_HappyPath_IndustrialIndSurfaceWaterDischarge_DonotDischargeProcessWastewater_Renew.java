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
public class Mvp_HappyPath_IndustrialIndSurfaceWaterDischarge_DonotDischargeProcessWastewater_Renew extends BaseTestPom{

    // Define constants for property keys for easy reference and to avoid typos
    private static final String BASE_URL = ConfigReader.getProperty("base.mvpLonginUrl");

    //User Info
    private static final String STANDARD_USERNAME = ConfigReader.getProperty("test.username"); // User should have a facility associated with all required fields
    private static final String STANDARD_PASSWORD = ConfigReader.getProperty("test.password");
    private static final String STANDARD_APP_PASSWORD = ConfigReader.getProperty("test.appPassword");
    private static final String STANDARD_APPLICANTNAME = ConfigReader.getProperty("test.applicantName");

    //Permit info
    private static final String PERMIT_TYPE = "Industrial Individual Surface Water Discharge Permit";
    private static final String PERMIT_DETAIL_TYPE = "For Industrial Surface Water Discharge Permit for Industrial Sources that Do Not Discharge Process Wastewater";
    private static final String PERMIT_ACTION_TYPE = "Renew";

    //Files info
    //Download files
    private static final List<String> DOWNLOAD_FILES = List.of("Form 1 General Information File",
            "Form 2E File",
            "Worker Compensation Form File");
    private static final List<String> DOWNLOAD_FILES_PDF_NAMES = List.of("Individual NPDES Permit - Application Form 1 - General Information - Wastewater - WSA.pdf",
            "Individual NPDES Permit - Application Form 2E - Manufacturing, Commercial, Mining, and Silvicultural Facilities Discharge Only Nonprocess Wastewater - WSA.pdf",
            "Certification of Worker's Compensation Insurance Coverage - Wastewater - WSA.pdf");
    //Upload Files
    private static final String REQUIRED_UPLOAD_FILE1 = "Form 1 General Information";
    private static final String REQUIRED_UPLOAD_FILE2 = "Form 2E";
    private static final String REQUIRED_UPLOAD_FILE3 = "Worker Compensation Form";
    //Fees
    private static final String TOTAL_FEE = "To Be Determined";

    @Test(description = "Happy path test for applying Industrial Individual Surface Water Discharge Permit - For Industrial Surface Water Discharge Permit for Industrial Sources that Do Not Discharge Process Wastewater - Renew")
    public void mvpHappyPathIndustrialIndSurfaceWaterDischargeDonotDischargeProcessWastewaterRenew() throws InterruptedException {
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
        mvpPermitInfoPage.selectPermitActionTypeContainsMvp(PERMIT_ACTION_TYPE);
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

        // Upload files for REQUIRED_UPLOAD_FILE2
        // Define the name of the file you want to upload from your test_files directory
        String localFileName2 = "file-sample_100kB.docx"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath2 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName2;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE2 , filePath2 );

        // Upload files for REQUIRED_UPLOAD_FILE1
        // Define the name of the file you want to upload from your test_files directory
        String localFileName3 = "Tidal_JPA_Form.pdf"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath3 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName3;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE3 , filePath3 );

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

