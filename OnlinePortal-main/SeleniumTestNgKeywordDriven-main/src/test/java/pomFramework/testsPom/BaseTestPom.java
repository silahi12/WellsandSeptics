package pomFramework.testsPom;

import org.testng.annotations.*;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;

@Listeners(TestAllureListenerPom.class)
public class BaseTestPom {
    /**
     * Browser setup method.
     * This method runs before each test method.
     * It initializes the WebDriver using DriverManager based on the 'browser' parameter.
     *
     * @param browser The browser to use for the test (default to "chrome" if not specified).
     */
    @BeforeMethod
    @Parameters("browser") // Parameter to specify the browser from testng.xml
    public void setupPom(@Optional("chrome") String browser) {
        System.out.println("Setting up WebDriver for browser: " + browser);
        DriverManagerPom.initDriver(browser); // Initialize WebDriver for the current thread
    }

    /**
     * Browser teardownPom method.
     * This method runs after each test method.
     * It quits the WebDriver and removes it from ThreadLocal.
     */
    @AfterMethod
    public void teardownPom() {
        System.out.println("Quitting WebDriver.");
        DriverManagerPom.quitDriver(); // Quit WebDriver for the current thread
    }
}
