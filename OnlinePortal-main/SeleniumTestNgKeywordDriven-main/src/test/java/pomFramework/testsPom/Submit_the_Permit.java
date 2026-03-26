package pomFramework.testsPom;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.DashboardPage;
import pomFramework.pagesPom.LoginPage;
import pomFramework.pagesPom.PermitInfoPage;
import pomFramework.pagesPom.SearchPermitsPage;
import pomFramework.utilsPom.ConfigReader;

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

    @Test(description = "Verify successful submit of the Wells Permit")
    public void HappyPathPermitSubmittedSuccessfully() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        SearchPermitsPage  searchPermitsPage = new SearchPermitsPage();
        PermitInfoPage permitInfoPage = new PermitInfoPage();
        DashboardPage dashboardPage = new DashboardPage();
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
    }



    //@Test(description = "Verify login with invalid credentials")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToPOM(BASE_URL);
        loginPage.login("invalid_user@gmail.com", "wrong_password");
        // Assert that an error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for invalid login.");
        Assert.assertEquals(loginPage.getErrorMessage(), "Email ID or password is incorrect.", "Incorrect error message displayed.");
        System.out.println("Test Invalid Login Passed!");
    }
}
