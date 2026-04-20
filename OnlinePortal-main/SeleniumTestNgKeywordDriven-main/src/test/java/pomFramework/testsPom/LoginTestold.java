package pomFramework.testsPom;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.DashboardPage;
import pomFramework.pagesPom.LoginPage;
import pomFramework.utilsPom.ConfigReader;

@Listeners(TestAllureListenerPom.class)
public class LoginTestold extends BaseTestPom {

    // Define constants for property keys for easy reference and to avoid typos
    private static final String BASE_URL = ConfigReader.getProperty("base.loginUrl");
    private static final String STANDARD_USERNAME = ConfigReader.getProperty("test.username");
    private static final String STANDARD_PASSWORD = ConfigReader.getProperty("test.password");
    private static final String STANDARD_APP_PASSWORD = ConfigReader.getProperty("test.appPassword");


    @Test(description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage();
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
