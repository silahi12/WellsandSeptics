package pomFramework.testsPom;

import org.testng.annotations.DataProvider;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.DashboardPage;
import pomFramework.pagesPom.LoginPage;
import pomFramework.utilsPom.ConfigReader;
import pomFramework.utilsPom.ExcelUtils; // Import the new utility class

@Listeners(TestAllureListenerPom.class)
public class LoginTest extends BaseTestPom {

    private static final String BASE_URL = ConfigReader.getProperty("base.loginUrl");

    // --- 1. SET UP THE DATA PROVIDER ---
    @DataProvider(name = "excelLoginData")
    public Object[][] getLoginDataFromExcel() {
        String projectPath = System.getProperty("user.dir");
        String excelFilePath = projectPath + "/src/test/resources/testData/LoginData.xlsx";

        // Pass the file path and the exact name of the sheet
        return ExcelUtils.getExcelData(excelFilePath, "LoginScenarios");
    }

    // --- 2. LINK DATAPROVIDER TO THE TEST ---
    @Test(description = "Verify login with valid and invalid credentials", dataProvider = "excelLoginData")
    public void testLoginScenarios(String username, String password, String isValidStr, String expectedMessage) {

        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage = new DashboardPage();

        // Convert the string "TRUE"/"FALSE" from Excel into a Java boolean
        boolean isValidLogin = Boolean.parseBoolean(isValidStr);
        System.out.println("Executing Row -> User: [" + username + "] | Pass: [" + password + "] | Expect Valid: " + isValidLogin);
        // Execute Steps
        loginPage.navigateToPOM(BASE_URL);
        loginPage.login(username, password);

        // Conditional Assertions based on Excel data
        if (isValidLogin) {
            // Assertions for a VALID login
            String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
            Assert.assertTrue(dashboardPage.isUserOnDashboardUrl(), "Login was not successful! Current Url - " + currentUrl);
            System.out.println("Test Passed: Valid Login successful for user - " + username);
        } else {
            // Assertions for an INVALID login
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for invalid login.");
            Assert.assertEquals(loginPage.getErrorMessage(), expectedMessage, "Incorrect error message displayed.");
            System.out.println("Test Passed: Invalid Login correctly blocked with message '" + expectedMessage + "'");
        }
    }
}
