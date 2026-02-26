package pomFramework.testsPom;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.dataPom.FacilityDetails;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.*;
import pomFramework.utilsPom.ConfigReader;

import java.io.File;

@Listeners(TestAllureListenerPom.class)
public class HappyPathTest_GeneralDischarge_MineralMines_Renew extends BaseTestPom {

    // Define constants for property keys for easy reference and to avoid typos
    private static final String BASE_URL = ConfigReader.getProperty("base.loginUrl");

    //Permit info
    private static final String PERMIT_TYPE = "General Discharge Permit";
    private static final String PERMIT_DETAIL_TYPE = "For Discharges from Mineral Mines, Quarries, Borrow Pits, and Concrete and Asphalt Plants Notice of Intent (NOI)";
    private static final String PERMIT_ACTION_TYPE = "Renew";

    //User Info
    private static final String STANDARD_USERNAME = ConfigReader.getProperty("test.username"); // User should have a facility associated with all required fields
    private static final String STANDARD_PASSWORD = ConfigReader.getProperty("test.password");
    private static final String STANDARD_APP_PASSWORD = ConfigReader.getProperty("test.appPassword");

    //Facility Info
    private static final String FACILITY_UNDER_TEST = "test.facility";

    //Contact Info
    private static final String CONTACT_UNDER_TEST = "test.contact1";

    //Files info
    private static final String REQUIRED_DOWNLOAD_FILE1 = "For Discharges from Mineral Quarries,Borrow Pits, and Concrete and Asphalt Plants Notice of Intent (NOI) File";
    private static final String REQUIRED_DOWNLOAD_FILE1_PDF_NAME = "15MM-NOI.pdf";
    private static final String REQUIRED_UPLOAD_FILE1 = "For Discharges from Mineral Quarries,Borrow Pits, and Concrete and Asphalt Plants Notice of Intent (NOI) File";
    private static final String REQUIRED_UPLOAD_FILE2 = "Stormwater Pollution Prevention Plan (SWPPP) File";

    //Fee info
    private static final String FEE_SELECTION ="No Fee";
    private static final String FEE_AMOUNT = "$0.00";

    @Test(description = "Happy path test for applying General Discharge Permit - For Discharges from Mineral Mines, Quarries, Borrow Pits, and Concrete and Asphalt Plants Notice of Intent (NOI) - Renew")
    public void happyPathGeneralDischargePermit() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage = new DashboardPage();
        SearchPermitsPage searchPermitsPage = new SearchPermitsPage();
        PermitInfoPage permitInfoPage = new PermitInfoPage();
        StandardPermitInfoTab standardPermitInfoTab = new StandardPermitInfoTab();
        ContactInformationForm contactInformationForm = new ContactInformationForm();
        ProgramSpecificInfoTab programSpecificInfoTab = new ProgramSpecificInfoTab();
        ReviewAndSubmitTab reviewAndSubmitTab = new ReviewAndSubmitTab();

        loginPage.navigateToPOM(BASE_URL);
        //Login
        loginPage.loginWithOtp(STANDARD_USERNAME, STANDARD_PASSWORD, STANDARD_APP_PASSWORD);
        // Assert that login was successful (e.g., check for URL change and element on next page)
        String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(dashboardPage.isUserOnDashboardUrl(), "Login was not successful! Current Url - " + currentUrl);
        Assert.assertTrue(dashboardPage.isProfileBtnDisplayed(), "Login was not successful!");
        System.out.println("Test Successful Login Passed!");

        //Click on Permits Tab
        dashboardPage.clickPermitTab();
        //Assert user is on permit search page (e.g., check for URL change and element on next page)
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(searchPermitsPage.searchPermitsPageUrl), "User not on Search permits page. Current Url - " + currentUrl);
        Assert.assertTrue(searchPermitsPage.isSearchPermitPageHeaderDisplayed(), "Incorrect search permits page header");
        System.out.println("User is on Search permits page");

        //Search for Permit type
        searchPermitsPage.search(PERMIT_TYPE);
        searchPermitsPage.clickDetailsButtonForPermitType(PERMIT_TYPE);
        //Assert User is on permit info page
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(permitInfoPage.permitInfoPageUrl), "User not on Permit info page. Current Url - " + currentUrl);
        Assert.assertEquals(permitInfoPage.getHeaderText().trim(), PERMIT_TYPE.trim(), "Incorrect header on Permit info page");

        //select Permit detail type and action type
        permitInfoPage.selectPermitDetailType(PERMIT_DETAIL_TYPE);
        permitInfoPage.selectPermitActionType(PERMIT_ACTION_TYPE);
        //Scroll to and click Apply now btn
        permitInfoPage.clickApplyNowBtnDisplayed();

        //Assert User is on standard permit info page
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(standardPermitInfoTab.isUserOnNewPermitUrl(), "User not on New permit application page. Current Url - " + currentUrl);
        //As user already has a facility associated, assert user lands on Stand permit Info page
        Assert.assertTrue(standardPermitInfoTab.isStandardPermitTabActive(), "User is not on Standard permit info tab ");
        BasePage basePage = new BasePage();
        basePage.waitForPageLoadPom();

        //Verify Associated facility info is auto populated
        FacilityDetails expectedFacilityDetails = new FacilityDetails(FACILITY_UNDER_TEST);

        standardPermitInfoTab.verifyFacilityDetails(FACILITY_UNDER_TEST);
        standardPermitInfoTab.verifyFacilityPhysicalAddress(FACILITY_UNDER_TEST);
        standardPermitInfoTab.verifyFacilityMailingAddress(FACILITY_UNDER_TEST);
        standardPermitInfoTab.verifyFacilityLocationInfo(FACILITY_UNDER_TEST);


        //Todo -Implementing Workaround for contact info
        //Delete any auto populated contacts
//        standardPermitInfoTab.deleteAllContacts();
        //Add one contact with all three required roles
        //standardPermitInfoTab.clickAddContactBtn();
//        standardPermitInfoTab.clickEditContactbtn();
//        Assert.assertTrue(contactInformationForm.isContactInfoFormDisplayed(), "Contact Info form is not displayed after clicking on add contact btn ");
//        contactInformationForm.addContact(CONTACT_UNDER_TEST);

        //Todo- Add and verify added contact on standard permit info page

        standardPermitInfoTab.clickNextBtn();
        Assert.assertTrue(programSpecificInfoTab.isProgramSpecificTabActive(), "User is not on Program specific info tab");
        //Assert user is on Files sub tab
        Assert.assertTrue(programSpecificInfoTab.isFilesSubTabActive(), "User is not on Files sub tab under Program specific info tab");

        //Verify under download permit files section REQUIRED_DOWNLOAD_FILE1 is displayed
        Assert.assertTrue(programSpecificInfoTab.isFileNameDisplayedUnderDownloadPermitFiles(REQUIRED_DOWNLOAD_FILE1), "Required download permit file - " + REQUIRED_DOWNLOAD_FILE1 + " is not displayed");
        //Verify under Upload permit files section REQUIRED_UPLOAD_FILE1, REQUIRED_UPLOAD_FILE2 are displayed
        Assert.assertTrue(programSpecificInfoTab.isFileNameDisplayedUnderUploadPermitFiles(REQUIRED_UPLOAD_FILE1), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE1 + " is not displayed");
        Assert.assertTrue(programSpecificInfoTab.isFileNameDisplayedUnderUploadPermitFiles(REQUIRED_UPLOAD_FILE2), "Required Upload permit file - " + REQUIRED_UPLOAD_FILE2 + " is not displayed");

        //Download the file and verify no error is shown
        Assert.assertNotEquals(programSpecificInfoTab.downloadPermitFile(REQUIRED_DOWNLOAD_FILE1,REQUIRED_DOWNLOAD_FILE1_PDF_NAME), null, "Unable to download " + REQUIRED_DOWNLOAD_FILE1);
        Assert.assertTrue(programSpecificInfoTab.isFilesSubTabActive(), "User is not on Files sub tab under Program specific info tab after downloading file");

        // Upload files for REQUIRED_UPLOAD_FILE1, REQUIRED_UPLOAD_FILE2
        // Define the name of the file you want to upload from your test_files directory
        String localFileName1 = "19CM-NOI.pdf"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath1 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName1;

        // Optional: Define a different name to appear in the "File Name" field in the modal
        String userFileName1 = "Uploaded NOI by Test Automation";

        programSpecificInfoTab.uploadPermitFile(REQUIRED_UPLOAD_FILE1,filePath1,userFileName1);

        // Define the name of the file you want to upload from your test_files directory
        String localFileName2 = "Tidal_JPA_Form.pdf"; // This file should be in src/test/resources/resourcesPOM/testFiles/
        // Construct the absolute path to your test file
        String filePath2 = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "resourcesPOM" +
                File.separator + "testFiles" + File.separator + localFileName2;

        // Optional: Define a different name to appear in the "File Name" field in the modal
        String userFileName2 = "Uploaded SWPPP by Test Automation";

        programSpecificInfoTab.uploadPermitFile(REQUIRED_UPLOAD_FILE2,filePath2,userFileName2);

        //Verify file was uploaded
        Assert.assertTrue(programSpecificInfoTab.userUploadFileNameExists(userFileName1),"User uploaded file name- " + userFileName1 + " does not exists");
        Assert.assertTrue(programSpecificInfoTab.userUploadFileNameExists(userFileName2),"User uploaded file name- " + userFileName2 + " does not exists");
        //waiting for toast msg to disappear
        programSpecificInfoTab.waitUntilSuccessUploadMsgDisappears();

        programSpecificInfoTab.clickNextBtnFilesSubTab();
        Assert.assertTrue(programSpecificInfoTab.isProgramSpecificTabActive(), "User is not on Program specific info tab after clicking on Next on Files sub tab");
        Assert.assertTrue(programSpecificInfoTab.isFeeSubTabActive(), "User is not on Fees sub tab under Program specific info tab after downloading file");
        //Choose fee and verify the Application total fee
        programSpecificInfoTab.chooseFee(FEE_SELECTION);
        Assert.assertTrue(programSpecificInfoTab.verifyApplicationTotalFee(FEE_AMOUNT), "Incorrect application total fee");
        programSpecificInfoTab.clickNextBtnFeesSubTab();

        //Assert User is on Review and Submit tab
        Assert.assertTrue(reviewAndSubmitTab.isReviewAndSubmitTabActive(), "User is not on Review and Submit tab");

        //Verify Facility info
        reviewAndSubmitTab.verifyFacilityDetails(FACILITY_UNDER_TEST);

        //Verify Facility Physical address
        reviewAndSubmitTab.verifyFacilityPhysicalAddress(FACILITY_UNDER_TEST);

        //verify Facility mailing Address
        reviewAndSubmitTab.verifyFacilityMailingAddress(FACILITY_UNDER_TEST);

        //Verify Location Information
        reviewAndSubmitTab.verifyFacilityLocationInfo(FACILITY_UNDER_TEST);

        //ToDo - Verify Contact information

        //Verify Permit documents
        Assert.assertTrue(reviewAndSubmitTab.userUploadFilesExists(localFileName1),"User uploaded file - " + localFileName1 + " does not exists");
        Assert.assertTrue(reviewAndSubmitTab.userUploadFilesExists(localFileName2),"User uploaded file - " + localFileName2 + " does not exists");

        //Todo - Actions on Electronic Signature Section
        reviewAndSubmitTab.VerifyESignature(STANDARD_USERNAME, STANDARD_APP_PASSWORD);
        //Click Submit
        reviewAndSubmitTab.clickSubmitBtn();

        //verify application Success msg shown
        dashboardPage.verifyPermitSubmissionSuccessMsgDisplayed();

        //Verify user is on dashboard and do logout
        currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(dashboardPage.isUserOnDashboardUrl(), "User not on Dashboard " + currentUrl);
        Assert.assertTrue(dashboardPage.isMyApplicationsHeaderDisplayed(), "My Applications Header not displayed on dashboard");
        dashboardPage.clickLogout();

        //Verify User is on Login page
        Assert.assertTrue(loginPage.isUserOnLoginUrl(), "Login was not successful! Current Url - " + currentUrl);


    }

}
