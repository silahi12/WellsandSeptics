package pomFramework.testsPom;

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



    @Test(description = "Verify successful submit of the Wells Permit", dataProvider = "addressData")
    public void NavigateGreenFormFromDashboard(String partialSearch, String exactAddress, String county, String city, String zipCode) throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        SearchPermitsPage searchPermitsPage = new SearchPermitsPage();
        PermitInfoPage permitInfoPage = new PermitInfoPage();
        DashboardPage dashboardPage = new DashboardPage();
        WellSiteAddressPage wellSiteAddressPage = new WellSiteAddressPage();
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
