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
public class Mvp_HappyPath_New_GeneralDischargePermit_Discharges_Mineral_Mines_Quarries extends BaseTestPom{

    // Define constants for property keys for easy reference and to avoid typos
    private static final String BASE_URL = ConfigReader.getProperty("base.mvpLonginUrl");

    //User Info
    private static final String STANDARD_USERNAME = ConfigReader.getProperty("test.username"); // User should have a facility associated with all required fields
    private static final String STANDARD_PASSWORD = ConfigReader.getProperty("test.password");
    private static final String STANDARD_APP_PASSWORD = ConfigReader.getProperty("test.appPassword");
    private static final String STANDARD_APPLICANTNAME = ConfigReader.getProperty("test.applicantName");

    //Permit info
    private static final String PERMIT_TYPE = "General Discharge Permit for Discharges from Mineral Mines, Quarries, Borrow Pits, and Concrete and Asphalt Plants (MM) Notice of Intent (NOI)";
    private static final String PERMIT_DETAIL_TYPE = "General Discharge Permit for Discharges from Mineral Mines, Quarries, Borrow Pits, and Concrete and Asphalt Plants (MM) Notice of Intent (NOI)";
    //Using contains while choosing action type
    private static final String PERMIT_ACTION_TYPE = "New";

    //Files info
    //Download files
    private static final List<String> DOWNLOAD_FILES = List.of("For Discharges from Mineral Mines, Quarries,Borrow Pits, and Concrete and Asphalt Plants Notice of Intent (NOI) File",
            "Stormwater Pollution Prevention Plan (SWPPP) File",
            "Declaration of Intent (DOI) File",
            "Request for Cationic Chemical Additives Form  File");

    private static final List<String> DOWNLOAD_FILES_PDF_NAMES = List.of("Discharges from Mineral Mines, Quarries, Borrow Pits, and Concrete and Asphalt Plants - Wastewater - WSA.pdf",
            "Stormwater Pollution Prevention Plan - Wastewater - WSA.docx",
            "Declaration of Intent - 15MM.pdf",
            "Request for Use of Cationic Chemical Additives Form - Wastewater - WSA.pdf");

    //Upload Files
    private static final String REQUIRED_UPLOAD_FILE1 = "For Discharges from Mineral Mines, Quarries,Borrow Pits, and Concrete and Asphalt Plants Notice of Intent (NOI)";
    private static final String REQUIRED_UPLOAD_FILE2 = "Stormwater Pollution Prevention Plan (SWPPP)";
    private static final String REQUIRED_UPLOAD_FILE3 = "Declaration of Intent (DOI)";

    //Fees
    private static final String FEE_SELECTION = "Average Discharge Volume: 5,001—50,000 Gallons Per Day";
    private static final String TOTAL_FEE = "$600.00";

    @Test(description = "Happy path test for applying General Discharge Permit for Discharges from Mineral Mines, Quarries, Borrow Pits, and Concrete and Asphalt Plants (MM) Notice of Intent (NOI) - New")
    public void mvpHappyPathNewGeneralDischargePermitDischargesMineralMinesQuarries() throws InterruptedException {
        MvpLoginPage mvpLoginPage = new MvpLoginPage();
        MvpDashboardPage mvpDashboardPage = new MvpDashboardPage();
        MvpSearchPermitsPage mvpSearchPermitsPage = new MvpSearchPermitsPage();
        MvpPermitInfoPage mvpPermitInfoPage = new MvpPermitInfoPage();
        MvpProcessOverviewPage mvpProcessOverviewPage = new MvpProcessOverviewPage();
        MvpUploadPage mvpUploadPage = new MvpUploadPage();
        MvpFeePage mvpFeePage = new MvpFeePage();
        MvpReviewAndSubmitPage mvpReviewAndSubmitPage = new MvpReviewAndSubmitPage();
        GovolutionPagePaymentMethod govolutionPagePaymentMethod = new GovolutionPagePaymentMethod();
        GovolutionPagePaymentInfoPage govolutionPagePaymentInfoPage = new GovolutionPagePaymentInfoPage();
        GovolutionPaymentConfirmationPage govolutionPaymentConfirmationPage = new GovolutionPaymentConfirmationPage();
        GovolutionServiceFeePage govolutionServiceFeePage = new GovolutionServiceFeePage();

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

        //Verify user is on process overview page
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(mvpProcessOverviewPage.mvpProcessOverviewPageUrl), "User not on Process overview page. Current Url - " + currentUrl);

        //verify header on process overview page
        mvpProcessOverviewPage.verifyPermitTypeHeader(PERMIT_TYPE);
        mvpProcessOverviewPage.clickNextUploadBtn();

        //Verify user is on Upload page
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(mvpUploadPage.mvpUploadPageUrl), "User not on Upload page. Current Url - " + currentUrl);
        //verify header on process overview page
        mvpUploadPage.verifyPermitTypeHeaderOnUploadPageMvp(PERMIT_TYPE);

        //1.Verify under Upload permit files section REQUIRED_UPLOAD_FILE is displayed
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

        //2.Verify under Upload permit files section REQUIRED_UPLOAD_FILE is displayed
        Assert.assertTrue(mvpUploadPage.isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(REQUIRED_UPLOAD_FILE2), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE2 + " is not displayed");

        // Upload files for REQUIRED_UPLOAD_FILE2
        // Define the name of the file you want to upload from your test_files directory
        String localFileName2 = "Tidal_JPA_Form.pdf"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath2 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName2;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE2 , filePath2 );

        //3.Verify under Upload permit files section REQUIRED_UPLOAD_FILE is displayed
        Assert.assertTrue(mvpUploadPage.isRequiredFileNameDisplayedUnderUploadPermitFilesMvp(REQUIRED_UPLOAD_FILE3), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE3 + " is not displayed");

        // Upload files for REQUIRED_UPLOAD_FILE3
        // Define the name of the file you want to upload from your test_files directory
        String localFileName3 = "file-sample_100kB.docx"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath3 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName3;

        mvpUploadPage.uploadPermitFileMvp(REQUIRED_UPLOAD_FILE3 , filePath3 );

        mvpUploadPage.clickNextBtnOnUploadPageMvp();

        //Verify user is on Fee page
        mvpFeePage.waitForFeePageUrl();
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(mvpFeePage.mvpFeePageUrl), "User not on Fee page. Current Url - " + currentUrl);

        mvpFeePage.verifyStepIndicatorTextOnFeePageMvp();

        mvpFeePage.chooseFeeMvp(FEE_SELECTION);
        mvpFeePage.verifyApplicationTotalFee(TOTAL_FEE);
        mvpFeePage.clickNextBtnFeesSubTab();
        //Verify user is on review page
        mvpReviewAndSubmitPage.waitForReviewPageUrl();
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(mvpReviewAndSubmitPage.mvpReviewPageUrl), "User not on Review page. Current Url - " + currentUrl);

        //Verify User details
        Assert.assertTrue(mvpReviewAndSubmitPage.verifyApplicantNameMvp(STANDARD_APPLICANTNAME), "Applicant Name mismatch!");
        Assert.assertTrue(mvpReviewAndSubmitPage.verifyApplicantEmailMvp(STANDARD_USERNAME), "Applicant Email mismatch!");
        Thread.sleep(1000);
        Assert.assertTrue(mvpReviewAndSubmitPage.verifyApplicationFeeMvp(TOTAL_FEE), "Application fee mismatch!");
        Assert.assertTrue(mvpReviewAndSubmitPage.userUploadedFileExistsMvp(localFileName1), "User upload file name mismatch!");
        Assert.assertTrue(mvpReviewAndSubmitPage.userUploadedFileExistsMvp(localFileName2), "User upload file name mismatch!");
        Assert.assertTrue(mvpReviewAndSubmitPage.userUploadedFileExistsMvp(localFileName3), "User upload file name mismatch!");

        mvpReviewAndSubmitPage.clickSubmitBtn();

        //Govolution
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(govolutionPagePaymentMethod.isUserOnGovolutionPagePaymentMethodUrl(), "User not on Govolution page. Current Url - " + currentUrl);

        govolutionPagePaymentMethod.selectCreditCardPayment();
        govolutionPagePaymentMethod.clickMakePayment();

        Assert.assertTrue(govolutionPagePaymentInfoPage.isUserOnGovolutionPagePaymentInfoUrl(), "User not on Govolution payment info page. Current Url - " + currentUrl);

        govolutionPagePaymentInfoPage.enterPaymentInfoMandatoryFields("automation test", "5555555555554444","123", "01", "2027","line 1","21043",STANDARD_USERNAME);

        govolutionPagePaymentInfoPage.clickTermsAndConditions();
        govolutionPagePaymentInfoPage.clickImNotRobot();
        govolutionPagePaymentInfoPage.clickContinueBtn();

        Assert.assertTrue(govolutionPaymentConfirmationPage.isUserOnGovolutionPaymentConfirmUrl(), "User not on Govolution payment confirmation page. Current Url - " + currentUrl);
        govolutionPaymentConfirmationPage.clickConfirmBtn();

        Assert.assertTrue(govolutionServiceFeePage.isUserOnGovolutionServicefeeUrl(), "User not on Govolution Service fee page. Current Url - " + currentUrl);
        govolutionServiceFeePage.clickAcceptFeeBtn();


        //Verify success msg
        mvpDashboardPage.verifyMvpPermitSubmissionSuccessMsgDisplayed();
        mvpDashboardPage.clickMvpLogout();

    }
}
