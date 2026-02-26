package pomFramework.driverPom;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * DriverManager class manages WebDriver instances using ThreadLocal.
 * This ensures that each thread gets its own WebDriver instance,
 * which is crucial for parallel test execution with TestNG.
 */
public class DriverManagerPom {

    // ThreadLocal<WebDriver> is declared here.
    // It holds a separate WebDriver instance for each thread accessing it.
    private static final ThreadLocal<WebDriver> driverPom = new ThreadLocal<>();
    private static final String DOWNLOAD_DIR = System.getProperty("user.dir") + File.separator + "downloads"; // Define download directory

    /**
     * Initializes a new WebDriver instance based on the specified browser.
     * This method is called at the beginning of each test method or suite.
     *
     * @param browser The name of the browser (e.g., "chrome", "firefox", "edge").
     */
    public static void initDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            // Set system property for ChromeDriver if not already set by WebDriverManager or similar
            // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe");
            ChromeOptions co = new ChromeOptions();

            Map<String, Object> prefs = new HashMap<>();
            //added below line to fix the download fail on jenkins
            prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
            prefs.put("download.default_directory", DOWNLOAD_DIR);
            prefs.put("download.prompt_for_download", false); // Do not ask where to save the file
            prefs.put("plugins.always_open_pdf_externally", true); // Handle PDFs directly rather than opening in browser
            co.setExperimentalOption("prefs", prefs);

            co.addArguments("--window-size=1920,1080");
            co.addArguments("--no-sandbox");
            co.setAcceptInsecureCerts(true);
            //co.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            //co.addArguments("--disable-blink-features=AutomationControlled");
//            co.addArguments("--headless");
//            co.addArguments("--disable-gpu");
//            co.addArguments("--disable-crash-reporter");
//            co.addArguments("--disable-extensions");
//            co.addArguments("--disable-in-process-stack-traces");
//            co.addArguments("--disable-logging");
//            co.addArguments("--disable-dev-shm-usage");
//            co.addArguments("--log-level=3");
//            co.addArguments("--output=/dev/null");
//            co.addArguments("ignore-certificate-errors");
            driverPom.set(new ChromeDriver(co));
        } else if (browser.equalsIgnoreCase("firefox")) {
            // System.setProperty("webdriver.gecko.driver", "path/to/geckodriver.exe");
            FirefoxOptions fo = new FirefoxOptions();
            fo.addArguments("--window-size=1920,1080");
            fo.addArguments("--no-sandbox");
//            fo.addArguments("--headless");
//            fo.addArguments("--disable-gpu");
//            fo.addArguments("--disable-crash-reporter");
//            fo.addArguments("--disable-extensions");
//            fo.addArguments("--disable-in-process-stack-traces");
//            fo.addArguments("--disable-logging");
//            fo.addArguments("--disable-dev-shm-usage");
//            fo.addArguments("--log-level=3");
//            fo.addArguments("--output=/dev/null");
//            fo.addArguments("ignore-certificate-errors");
            driverPom.set(new FirefoxDriver(fo));
        } else if (browser.equalsIgnoreCase("edge")) {
            // System.setProperty("webdriver.edge.driver", "path/to/msedgedriver.exe");
            driverPom.set(new EdgeDriver());
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser + ". Please use 'chrome', 'firefox', or 'edge'.");
        }
        // Maximize the browser window
        getDriverPom().manage().window().maximize();

    }


    /**
     * Returns the WebDriver instance associated with the current thread.
     *
     * @return The WebDriver instance.
     * named as getDriverPom because of another get driver in keywordexecutor class
     */
    public static WebDriver getDriverPom() {
        return driverPom.get();
    }

    /**
     * Quits the WebDriver instance associated with the current thread and removes it from ThreadLocal.
     * This method is called at the end of each test method or suite.
     */
    public static void quitDriver() {
        if (driverPom.get() != null) {
            driverPom.get().quit();
            driverPom.remove(); // Remove the WebDriver instance from ThreadLocal
        }
    }

    /**
     * Returns the configured download directory path.
     * @return The absolute path to the download directory.
     */
    public static String getDownloadDir() {
        return DOWNLOAD_DIR;
    }

}


