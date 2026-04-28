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

   // @Test(description = "Verify successful submit of the Wells Permit")
    public void HappyPathPermitSubmittedSuccessfully() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        SearchPermitsPage  searchPermitsPage = new SearchPermitsPage();
        PermitInfoPage permitInfoPage = new PermitInfoPage();
        DashboardPage dashboardPage = new DashboardPage();
        WellSiteAddressPage wellSiteAddressPage = new WellSiteAddressPage();
        OwnerInfoPage ownerInfoPage = new OwnerInfoPage();
        BasePage basePage = new BasePage();
        loginPage.navigateToPOM(BASE_URL);
        //loginPage.loginWithOtp(STANDARD_USERNAME, STANDARD_PASSWORD, STANDARD_APP_PASSWORD);
        loginPage.login(STANDARD_USERNAME, STANDARD_PASSWORD);
        // Assert that login was successful (e.g., check for URL change and element on next page)
        String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(dashboardPage.isUserOnDashboardUrl(), "Login was not successful! Current Url - " + currentUrl);
        //Assert.assertTrue(dashboardPage.isProfileBtnDisplayed(), "Login was not successful!");
        System.out.println("Test Successful Login Passed!");
        //System.out.println("Test Successful Login Passed!");
        Thread.sleep(10000);
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
        wellSiteAddressPage.addressSearchText("5 Ballymena");


    }



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



    @Test(description = "Verify successful submit of the Wells Permit By Well Driller", dataProvider = "addressData")
    public void NavigateGreenFormFromDashboard(String partialSearch, String exactAddress, String county, String city, String zipCode,String businessName, String phoneNumber, String emailAddress,String wellType, String pumpRate, String dailyQty, String depth, String diameter, String drillMethod, String sourceWater) throws InterruptedException {

        //ADD THIS STOPPER ---
        // If Excel feeds an empty or null row, skip the test immediately without failing it.
        if (partialSearch == null || partialSearch.trim().isEmpty()) {
            throw new org.testng.SkipException("Skipping test: Reached an empty row in the Excel sheet.");
        }
        // ------------------------


        LoginPage loginPage = new LoginPage();
        SearchPermitsPage searchPermitsPage = new SearchPermitsPage();
        PermitInfoPage permitInfoPage = new PermitInfoPage();
        DashboardPage dashboardPage = new DashboardPage();
        WellSiteAddressPage wellSiteAddressPage = new WellSiteAddressPage();
        OwnerInfoPage ownerInfoPage = new OwnerInfoPage();
        BasePage basePage = new BasePage();

        loginPage.navigateToPOM(BASE_URL);
        //loginPage.loginWithOtp(STANDARD_USERNAME, STANDARD_PASSWORD, STANDARD_APP_PASSWORD);
        loginPage.login(STANDARD_USERNAME, STANDARD_PASSWORD);

        // Assert that login was successful
        String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(dashboardPage.isUserOnDashboardUrl(), "Login was not successful! Current Url - " + currentUrl);
        System.out.println("Test Successful Login Passed!");

        Thread.sleep(10000);
        Assert.assertTrue(searchPermitsPage.isSearchPermitPageHeaderDisplayed(), "Incorrect search permits page header");
        System.out.println("User is on Search permits page");

        // Click on the MDE Online Portal
        basePage.clickMdeOnlinePortal();
        System.out.println("Clicked MDE Online Portal link");

        // Click on "Dashboard"
        basePage.clickDashboardMenu();

        // Final Assertion to ensure you are back home
        System.out.println("Successfully returned to Dashboard!");
        dashboardPage.clickNewApplication();


        // --- DATA DRIVEN ADDRESS SECTION ---

        // 1. Type in the partial search (Using Excel Variable)
        wellSiteAddressPage.addressSearchText(partialSearch);

        // 2. Get all the dropdown values
        List<String> allOptions = wellSiteAddressPage.getAllDropdownValues();

        // 3. Assert against the list (Using Excel Variable)
        Assert.assertTrue(allOptions.size() > 0, "The dropdown list was empty!");
        Assert.assertTrue(allOptions.contains(exactAddress), "Dropdown did not contain expected address: " + exactAddress);

        // 4. SELECT THE VALUE (Using Excel Variable)
        wellSiteAddressPage.selectAddressFromDropdown(exactAddress);
        System.out.println("Successfully selected the address from the dropdown: " + exactAddress);
        Thread.sleep(5000);

        // --- 2. SELECT COUNTY, CITY, AND ZIP CODE FROM EXCEL ---

        // Select County
        wellSiteAddressPage.selectCounty(county);
        Thread.sleep(2000); // Optional pause to let City populate

        // Select City
        wellSiteAddressPage.selectCity(city);
        Thread.sleep(2000); // Optional pause to let Zip Code populate

        // Clean the Zip Code (removes ".0" if Excel interpreted it as a decimal)
        String cleanZip = String.valueOf(zipCode).replace(".0", "");

        // Select Zip Code from the dropdown
        wellSiteAddressPage.selectZipCode(cleanZip);

        System.out.println("Successfully selected County, City, and Zip Code!");
        // 3. CLICK SAVE AND CONTINUE
        wellSiteAddressPage.clickSaveAndContinue();
        System.out.println("Successfully filled address details and clicked Save and Continue.");


        // --- 4. ASSERT NAVIGATION TO NEXT PAGE ---

        // Wait a few seconds to allow the browser to load the new page
        Thread.sleep(5000);

        // Get the new URL
        String nextUrl = DriverManagerPom.getDriverPom().getCurrentUrl();

        // Assert the URL contains "GreenFormOwnerInfo"
        Assert.assertTrue(nextUrl.contains("GreenFormOwnerInfo"),
                "Failed to navigate to the Owner Info page. Current URL is: " + nextUrl);
        Allure.step("Successfully verified navigation to the Owner Information page.");
        System.out.println("Successfully navigated to the Owner Information page!");

        // --- 5. FILL OUT OWNER INFORMATION PAGE ---


        ownerInfoPage.fillBusinessOwnerInfoAndSubmit("Test Drilling Company LLC", "5551234567", "test@example.com");

        // --- 6. ASSERT NAVIGATION TO NEXT PAGE ---
        Thread.sleep(5000); // Wait for next page to load
        String nextUrlAfterOwner = DriverManagerPom.getDriverPom().getCurrentUrl();

        // Assuming the next page is "Well Information"
        Assert.assertTrue(nextUrlAfterOwner.contains("WellInformation") || nextUrlAfterOwner.contains("WellInfo"),
                "Failed to navigate to the Well Information page. Current URL is: " + nextUrlAfterOwner);

        //Reporter.log("Navigation Assertion Passed! User is on the Well Information page.", true);


// --- 7. FILL OUT WELL INFORMATION PAGE ---

        // A. Set up the local file path for the upload safely using File.separator
        String projectPath = System.getProperty("user.dir");
        String siteMapPath = projectPath + java.io.File.separator + "src" + java.io.File.separator + "test" + java.io.File.separator + "resources" + java.io.File.separator + "testData" + java.io.File.separator + "dummySiteMap.pdf";

        // Safety Check: Verify the file actually exists before Selenium tries to use it
        java.io.File uploadFile = new java.io.File(siteMapPath);
        if(!uploadFile.exists()){
            System.out.println("CRITICAL ERROR: Cannot find the PDF at: " + siteMapPath);
            Assert.fail("Test failed because the dummySiteMap.pdf file is missing. Please create it.");
        }

        // B. Clean up the numbers from Excel (removes ".0" if Excel interpreted them as decimals)
        String cleanPump = String.valueOf(pumpRate).replace(".0", "");
        String cleanQty = String.valueOf(dailyQty).replace(".0", "");
        String cleanDepth = String.valueOf(depth).replace(".0", "");
        String cleanDiam = String.valueOf(diameter).replace(".0", "");

        // C. Call the wrapper method using the variables straight from the DataProvider
        WellInfoPage wellInfoPage = new WellInfoPage();
        wellInfoPage.fillWellInfoAndSubmit(
                wellType,
                cleanPump,
                cleanQty,
                cleanDepth,
                cleanDiam,
                drillMethod,
                sourceWater,
                siteMapPath
        );


        // --- 8. ASSERT NAVIGATION TO REVIEW PAGE ---

        Thread.sleep(5000); // Wait for the server to process the form and file
        String nextUrlAfterWellInfo = DriverManagerPom.getDriverPom().getCurrentUrl();

        // Based on the JS in the HTML, a successful submit redirects to "/Application/NewWellPermit/Review/"
        Assert.assertTrue(nextUrlAfterWellInfo.contains("Review"),
                "Failed to navigate to the Review page. Current URL is: " + nextUrlAfterWellInfo);

        System.out.println("Navigation Assertion Passed! User is on the Application Review page.");
        io.qameta.allure.Allure.step("Successfully verified navigation to the Review page.");

        Thread.sleep(50000);



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
