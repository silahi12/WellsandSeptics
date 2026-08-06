package pomFramework.testsPom;

import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.*;
import pomFramework.utilsPom.ConfigReader;
import pomFramework.utilsPom.ExcelUtils;

import java.util.List;

@Listeners(TestAllureListenerPom.class)
public class Submit_the_Permit extends BaseTestPom {

    // Define constants for property keys for easy reference and to avoid typos
    private static final String BASE_URL = ConfigReader.getProperty("base.loginUrl");
    private static final String STANDARD_USERNAME = ConfigReader.getProperty("test.username");
    private static final String STANDARD_PASSWORD = ConfigReader.getProperty("test.password");
    private static final String STANDARD_APP_PASSWORD = ConfigReader.getProperty("test.appPassword");


    //Permit info
    private static final String PERMIT_TYPE = "Well Permit Applications";
    private static final String PERMIT_DETAIL_TYPE = "Well Permit Applications";
    private static final String PERMIT_ACTION_TYPE = "New (GREEN)";


    //@Test(priority=1,description = "Verify login with invalid credentials")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToPOM(BASE_URL);
        loginPage.login("invalid_user@gmail.com", "wrong_password");
        // Assert that an error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for invalid login.");
        Assert.assertEquals(loginPage.getErrorMessage(), "Email ID or password is incorrect.", "Incorrect error message displayed.");
        System.out.println("Test Invalid Login Passed!");
    }


    @Test(description = "Verify successful submit of the Wells Permit By Well Driller for OWNER IS BUSINESS", dataProvider = "addressData")
    public void Submit_NWA_FromDashboard(
            String searchMethod, String propertyNumber, String partialSearch, String exactAddress,
            String isCorrectionNeeded, String correctionNotes, String isOwnerCorrectionNeeded, String ownercorrectionNotes,
            String phoneNumber, String emailAddress,
            String wellType, String pumpRate, String dailyQty, String depth,
            String diameter, String drillMethod, String sourceWater, String signatureName, String ownerEmail, String ownerPhone) throws InterruptedException {

        // --- SAFETY CHECK ---
        // If Excel feeds an empty or null row, skip the test immediately without failing it.
        if ((partialSearch == null || partialSearch.trim().isEmpty()) && (propertyNumber == null || propertyNumber.trim().isEmpty())) {
            throw new org.testng.SkipException("Skipping test: Reached an empty row in the Excel sheet.");
        }

        // --- INITIALIZE PAGES ---
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage = new DashboardPage();
        WellSiteAddressPage wellSiteAddressPage = new WellSiteAddressPage();
        OwnerInfoPage ownerInfoPage = new OwnerInfoPage();
        WellInfoPage wellInfoPage = new WellInfoPage();
        ReviewPage reviewPage = new ReviewPage();

        // --- LOGIN & NAVIGATION ---
        loginPage.navigateToPOM(BASE_URL);
        loginPage.login(STANDARD_USERNAME, STANDARD_PASSWORD);

        Assert.assertTrue(dashboardPage.isUserOnDashboardUrl(), "Login was not successful! Current Url - " + DriverManagerPom.getDriverPom().getCurrentUrl());
        System.out.println("Test Successful Login Passed!");

        Thread.sleep(10000);
        dashboardPage.clickNewApplication();
        Thread.sleep(5000);

        // --- 1. WELL SITE ADDRESS PAGE (UPDATED FLOW) ---

        // Determine Search Method based on Excel Data
        if (propertyNumber != null && !propertyNumber.trim().isEmpty()) {

            System.out.println("Property Number data found in Excel. Initiating Property Search for: " + propertyNumber);

            // 1. Click the radio button label to ensure the form triggers the input to enable
            // (Even if it is selected by default, clicking the label ensures the USWDS JS is active)
            wellSiteAddressPage.selectPropertySearchRadio();

            // 2. Enter the property number and click search
            // (Assumes your POM method handles the waiting and the search button click)
            wellSiteAddressPage.searchByPropertyNumber(propertyNumber);

            System.out.println("Successfully searched by Property Number: " + propertyNumber);


            // 1. Verify the message is physically displayed on the screen
            Assert.assertTrue(wellSiteAddressPage.isSuccessMessageDisplayed(),
                    "The property search success message was NOT displayed!");

// 2. Verify the text exactly matches the expected USWDS output
            String expectedMessage = "The correct address was successfully found. Please scroll down to save";
            Assert.assertEquals(wellSiteAddressPage.getSuccessMessageText(), expectedMessage,
                    "The success message text did not match the expected value!");


        } else {
            // Assume you created a POM method that clicks the 'rbMethodAddress' radio label
            wellSiteAddressPage.selectAddressSearchRadio();

            // 1. Type in the partial search
            wellSiteAddressPage.addressSearchText(partialSearch);

            // 2. Get all the dropdown values & Assert
            List<String> allOptions = wellSiteAddressPage.getAllDropdownValues();
            Assert.assertTrue(allOptions.size() > 0, "The dropdown list was empty!");
            Assert.assertTrue(allOptions.contains(exactAddress), "Dropdown did not contain expected address: " + exactAddress);

            // 3. Select the value
            wellSiteAddressPage.selectAddressFromDropdown(exactAddress);
            System.out.println("Successfully selected the address from the dropdown: " + exactAddress);
        }

        Thread.sleep(3000); // Wait for auto-fill and success banner

        // Handle Address Correction if specified in Excel
        if (isCorrectionNeeded != null && isCorrectionNeeded.equalsIgnoreCase("Yes")) {
            wellSiteAddressPage.clickAddressCorrectionCheckbox();
            wellSiteAddressPage.enterCorrectionNotes(correctionNotes);
            System.out.println("Address correction notes entered.");
        }

        // Click Save and Continue
        wellSiteAddressPage.clickSaveAndContinue();
        System.out.println("Successfully filled address details and clicked Save and Continue.");

        // 1. Initialize the Owner Information Page
        OwnerInfoPage ownerPage = new OwnerInfoPage();

        // 2. Log the action (optional, but helpful for debugging)
        System.out.println("Populating Owner Info with Email: " + ownerEmail + " and Phone: " + ownerPhone);

        // 3. Call the consolidated method to fill the fields and click Save
        ownerPage.fillOwnerContactInfoAndSubmit(ownerEmail, ownerPhone);

        if (isOwnerCorrectionNeeded != null && isOwnerCorrectionNeeded.equalsIgnoreCase("Yes")) {
            ownerInfoPage.clickOwnerAddressCorrectionCheckbox();
            ownerInfoPage.enterOwnerAddressCorrectionNotes(ownercorrectionNotes);
            System.out.println("Address correction notes entered.");
        }

        // --- 4. ASSERT NAVIGATION TO WELL INFO PAGE ---
        Thread.sleep(5000);
        String nextUrlAfterOwner = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(nextUrlAfterOwner.contains("WellInformation") || nextUrlAfterOwner.contains("WellInfo"), "Failed to navigate to the Well Information page. Current URL is: " + nextUrlAfterOwner);

        // --- 5. FILL OUT WELL INFORMATION PAGE ---
        String projectPath = System.getProperty("user.dir");
        String siteMapPath = projectPath + java.io.File.separator + "src" + java.io.File.separator + "test" + java.io.File.separator + "resources" + java.io.File.separator + "testData" + java.io.File.separator + "dummySiteMap.pdf";

        java.io.File uploadFile = new java.io.File(siteMapPath);
        if (!uploadFile.exists()) {
            Assert.fail("Test failed because the dummySiteMap.pdf file is missing at: " + siteMapPath);
        }

        // Clean up numeric strings from Excel
        String cleanPump = String.valueOf(pumpRate).replace(".0", "");
        String cleanQty = String.valueOf(dailyQty).replace(".0", "");
        String cleanDepth = String.valueOf(depth).replace(".0", "");
        String cleanDiam = String.valueOf(diameter).replace(".0", "");

        wellInfoPage.fillWellInfoAndSubmit(
                wellType, cleanPump, cleanQty, cleanDepth, cleanDiam, drillMethod, sourceWater, siteMapPath
        );

        // --- 6. ASSERT NAVIGATION TO REVIEW PAGE ---
        Thread.sleep(5000);
        String nextUrlAfterWellInfo = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(nextUrlAfterWellInfo.contains("Review"), "Failed to navigate to the Review page. Current URL is: " + nextUrlAfterWellInfo);
        io.qameta.allure.Allure.step("Successfully verified navigation to the Review page.");


        // --- 7. FILL OUT REVIEW PAGE & SIGN ---
        reviewPage.signAndSubmit(signatureName);

        // --- 8. ASSERT FINAL SUCCESSFUL SUBMISSION (PAYMENT PAGE) ---
        Thread.sleep(5000);
        String finalUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(finalUrl.contains("Payment"), "Submission failed! Did not navigate to the Payment page. Current URL is: " + finalUrl);
        System.out.println("SUCCESS! Permit successfully submitted. User is on the Payment page.");

        SubmissionSuccessPage successPage = new SubmissionSuccessPage();

        //  Run the hard assertions
        successPage.verifySuccessfulSubmission();

        // Extract the ID so you can see it in your IntelliJ console and Extent/Allure reports
        String newApplicationId = successPage.getApplicationId();


    }

    @Test(description = "Verify validation messages on Well Site Address page when submitting blank form")
    public void Verify_WellSiteAddress_Validations() throws InterruptedException {

        // --- INITIALIZE PAGES ---
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage = new DashboardPage();
        WellSiteAddressPage wellSiteAddressPage = new WellSiteAddressPage();

        // --- LOGIN & NAVIGATION ---
        loginPage.navigateToPOM(BASE_URL);
        loginPage.login(STANDARD_USERNAME, STANDARD_PASSWORD);

        Assert.assertTrue(dashboardPage.isUserOnDashboardUrl(), "Login was not successful!");
        System.out.println("Login Passed for Validation Test!");

        // Wait for Dashboard to load and click New Application
        Thread.sleep(5000);
        dashboardPage.clickNewApplication();
        Thread.sleep(5000); // Wait for the Well Site Address page to render

        // --- TRIGGER VALIDATIONS ---
        System.out.println("Clicking Save and Continue without entering data...");
        wellSiteAddressPage.clickSaveAndContinue();

        // --- ASSERT ERRORS ---
        wellSiteAddressPage.verifyBlankSubmissionErrors();
    }

    @Test(description = "Verify validation messages on Owner Information page when submitting blank form")
    public void Verify_OwnerInfo_Validations() throws InterruptedException {

        // --- INITIALIZE PAGES ---
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage = new DashboardPage();
        WellSiteAddressPage wellSiteAddressPage = new WellSiteAddressPage();
        OwnerInfoPage ownerInfoPage = new OwnerInfoPage();

        // --- LOGIN & NAVIGATION ---
        loginPage.navigateToPOM(BASE_URL);
        loginPage.login(STANDARD_USERNAME, STANDARD_PASSWORD);
        Assert.assertTrue(dashboardPage.isUserOnDashboardUrl(), "Login was not successful!");

        // Go to New Application
        Thread.sleep(5000);
        dashboardPage.clickNewApplication();
        Thread.sleep(5000);

        // --- 1. BYPASS WELL SITE ADDRESS PAGE ---
        // Provide valid dummy data to get past step 1.
        // Adjust these methods based on what works best for your environment.
        wellSiteAddressPage.selectPropertySearchRadio();
        wellSiteAddressPage.searchByPropertyNumber("1101000063"); // Use a valid dummy Property Number
        wellSiteAddressPage.clickSearchButton();
        Thread.sleep(3000); // Wait for auto-fill
        wellSiteAddressPage.clickSaveAndContinue();

        // Ensure we successfully landed on the Owner Info page
        Thread.sleep(3000);
        Assert.assertTrue(DriverManagerPom.getDriverPom().getCurrentUrl().contains("OwnerInfo"), "Did not reach Owner Information page.");

        // --- 2. TRIGGER OWNER INFO VALIDATIONS ---
        System.out.println("Clicking Save and Continue on Owner Info without entering data...");
        // Call the click method directly without passing email/phone data
        ownerInfoPage.clickSaveAndContinue();

        // --- 3. ASSERT ERRORS ---
        ownerInfoPage.verifyBlankSubmissionErrors();
    }


    // --- 1. SET UP THE DATA PROVIDER FOR ADDRESSES ---
    @DataProvider(name = "addressData")
    public Object[][] getAddressDataFromExcel() {
        String projectPath = System.getProperty("user.dir");

        // 1. Point to your EXISTING LoginData.xlsx file
        String excelFilePath = projectPath + "/src/test/resources/testData/LoginData.xlsx";

        // 2. Tell the utility to pull from your NEW "Addresses" sheet
        return ExcelUtils.getExcelData(excelFilePath, "WellSiteAddress");
    }
}
