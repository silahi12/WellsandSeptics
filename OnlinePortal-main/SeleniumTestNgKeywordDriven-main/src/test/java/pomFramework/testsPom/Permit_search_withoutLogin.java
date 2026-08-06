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
public class Permit_search_withoutLogin extends BaseTestPom {

    // Define constants for property keys for easy reference and to avoid typos
    private static final String BASE_URL = ConfigReader.getProperty("base.loginUrl");
    private static final String STANDARD_USERNAME = ConfigReader.getProperty("test.username");
    private static final String STANDARD_PASSWORD = ConfigReader.getProperty("test.password");
    private static final String STANDARD_APP_PASSWORD = ConfigReader.getProperty("test.appPassword");


    //Permit info
    private static final String PERMIT_TYPE = "Well Permit Applications";
    private static final String PERMIT_DETAIL_TYPE = "Well Permit Applications";
    private static final String PERMIT_ACTION_TYPE = "New (GREEN)";

    @Test(description = "Verify that user is navigated to the Login Page without entering the creds")
    public void SearchPermit_WithoutLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        SearchPermitsPage searchPermitsPage = new SearchPermitsPage();
        PermitInfoPage permitInfoPage = new PermitInfoPage();
        DashboardPage dashboardPage = new DashboardPage();

        // 1. Navigate to the base URL (Do NOT log in)
        loginPage.navigateToPOM(BASE_URL);
        System.out.println("Navigated to Base URL as a guest user.");

        // 2. Navigate to Permits Tab
        dashboardPage.clickPermitTab();

        // 3. Search for Permit type
        searchPermitsPage.search(PERMIT_TYPE);
        searchPermitsPage.clickDetailsButtonForPermitType(PERMIT_TYPE);

        // 4. Assert User is on permit info page
        String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(permitInfoPage.permitInfoPageUrl), "User not on Permit info page. Current Url - " + currentUrl);
        Assert.assertEquals(permitInfoPage.getHeaderText().trim(), PERMIT_TYPE.trim(), "Incorrect header on Permit info page");

        // 5. Select Permit detail type and action type
        permitInfoPage.selectPermitDetailType(PERMIT_DETAIL_TYPE);
        permitInfoPage.selectPermitActionType(PERMIT_ACTION_TYPE);

        // 6. Click Apply Now btn
        permitInfoPage.clickApplyNowBtnDisplayed();

        // Wait for redirect to occur
        Thread.sleep(3000);

        // 7. ASSERTION: Verify redirection to Login Page
        String redirectedUrl = DriverManagerPom.getDriverPom().getCurrentUrl();

        // Check if the URL contains a login routing keyword (adjust "login" to match your actual routing path if needed)
        boolean isRedirectedToLogin = redirectedUrl.toLowerCase().contains("login") || redirectedUrl.equalsIgnoreCase(BASE_URL);

        Assert.assertTrue(isRedirectedToLogin, "Security Bypass Bug! User was NOT redirected to the login page. Current URL: " + redirectedUrl);
        System.out.println("Successfully verified: Unauthenticated user was redirected to the Login page.");
        Thread.sleep(6000);


    }








}
