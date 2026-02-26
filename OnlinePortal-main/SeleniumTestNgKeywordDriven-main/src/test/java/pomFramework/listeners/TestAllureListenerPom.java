package pomFramework.listeners;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import pomFramework.driverPom.DriverManagerPom;



public class TestAllureListenerPom implements ITestListener {

    private static String getTestMethodNamePom(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    // Text attachments for Allure

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLogPom(String message) {
        return message;
    }

    // Screenshot attachments for Allure
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPNGPom(WebDriver driver) {
        if (driver == null) {
            System.err.println("WebDriver instance is null, cannot capture screenshot.");
            return new byte[0]; // Return empty byte array if driver is null
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("I am in onStart method " + iTestContext.getName());
        iTestContext.setAttribute("WebDriver", DriverManagerPom.getDriverPom());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("I am in onFinish method " + iTestContext.getName());
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("I am in onTestStart method " + getTestMethodNamePom(iTestResult) + " start");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("I am in onTestSuccess method " + getTestMethodNamePom(iTestResult) + " succeed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("I am in onTestFailure method " + getTestMethodNamePom(iTestResult) + " failed");

        // Get WebDriver from DriverManager
        WebDriver driver = DriverManagerPom.getDriverPom();

        // Allure ScreenShotRobot and SaveTestLog
        if (driver != null) {
            System.out.println("Screenshot captured for test failure: " + getTestMethodNamePom(iTestResult));
            saveScreenshotPNGPom(driver);
        } else {
            System.err.println("WebDriver instance is null in onTestFailure, cannot capture screenshot.");
        }
        saveTextLogPom(getTestMethodNamePom(iTestResult) + " failed and screenshot taken!");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("I am in onTestSkipped method " + getTestMethodNamePom(iTestResult) + " skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("I am in onTestFailedButWithinSuccessPercentage method " + getTestMethodNamePom(iTestResult));
    }
}