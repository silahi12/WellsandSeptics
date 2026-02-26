package pomFramework.testsPom;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.MvpDashboardPage;
import pomFramework.pagesPom.MvpLoginPage;
import pomFramework.utilsPom.ConfigReader;

@Listeners(TestAllureListenerPom.class)
public class MvpLoginTest extends BaseTestPom {

    // Define constants for property keys for easy reference and to avoid typos
    private static final String BASE_URL = ConfigReader.getProperty("base.mvpLonginUrl");
    private static final String STANDARD_USERNAME = ConfigReader.getProperty("test.username");
    private static final String STANDARD_PASSWORD = ConfigReader.getProperty("test.password");
    private static final String STANDARD_APP_PASSWORD = ConfigReader.getProperty("test.appPassword");


    @Test(description = "Verify successful login on MVP with valid credentials")
    public void testSuccessfulLogin() {
        MvpLoginPage mvpLoginPage = new MvpLoginPage();
        MvpDashboardPage mvpDashboardPage = new MvpDashboardPage();
        mvpLoginPage.navigateToPOM(BASE_URL);
        //loginPage.loginWithOtp(STANDARD_USERNAME, STANDARD_PASSWORD, STANDARD_APP_PASSWORD);
        mvpLoginPage.mvpLogin(STANDARD_USERNAME, STANDARD_PASSWORD);
        // Assert that login was successful (e.g., check for URL change and element on next page)
        String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(mvpDashboardPage.isUserOnMvpDashboardUrl(), "Login was not successful! Current Url - " + currentUrl);
        System.out.println("Test Successful Login Passed!");
        mvpDashboardPage.clickMvpLogout();
    }

    @Test(description = "Verify login with invalid credentials")
    public void testInvalidLogin() {
        MvpLoginPage mvpLoginPage = new MvpLoginPage();
        mvpLoginPage.navigateToPOM(BASE_URL);
        mvpLoginPage.mvpLogin("invalid1_user@gmail.com", "wrong_password");
        // Assert that an error message is displayed
        Assert.assertTrue(mvpLoginPage.isMVPErrorMessageDisplayed(), "Error message was not displayed for invalid login.");
        Assert.assertEquals(mvpLoginPage.getMvpErrorMessage(), "Email ID or password is incorrect.", "Incorrect error message displayed.");
        System.out.println("Test Invalid Login Passed!");
    }
}