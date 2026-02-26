package com.nal.utils;

import com.nal.keywords.Browser;
import com.nal.keywords.KeywordExecutor;
import com.nal.listeners.TestAllureListener;
import org.testng.annotations.*;

@Listeners(TestAllureListener.class)
public abstract class BaseTest {

    protected KeywordExecutor keywordExecutor; // Make it protected so subclasses can access

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) { // Use @Optional to provide a default value
        try {
            Browser bBrowser = Browser.valueOf(browser.toLowerCase());
            keywordExecutor = new KeywordExecutor();
            keywordExecutor.openBrowser(bBrowser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (KeywordExecutor.getDriver() != null) {
            KeywordExecutor.getDriver().quit();
        }
    }
}