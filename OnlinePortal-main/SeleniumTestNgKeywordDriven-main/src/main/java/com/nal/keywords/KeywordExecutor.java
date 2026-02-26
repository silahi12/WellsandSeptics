package com.nal.keywords;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

import com.nal.utils.ExcelReader;
import com.nal.utils.Table;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.qameta.allure.Step;


public class KeywordExecutor {

	/**
	 * ThreadLocal instance to hold WebDriver instance for each thread.
	 */
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();
	private String parentWindowhandle;
     String  docId ;

	/**
	 * Executes the given keyword with the provided target and value.
	 * 
	 * @param keyword The keyword to execute.
	 * @param target  The target element or location.
	 * @param value   The value to input or verify.
	 */
	@Step("{keyword} | {target} | {value} | ")
	public void executeKeyword(String keyword, String target, String value) throws InterruptedException {
		switch (keyword) {
		case "OpenBrowser":
			try {
				Browser browser = Browser.valueOf(target.toLowerCase());
				openBrowser(browser);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Unsupported browser: " + value);
			}
			break;
		case "NavigateTo":
			navigateTo(target);
			break;
		case "WaitAndAssertCurrentUrlContains":
			waitAndAssertCurrentUrlContains(target);
			break;
		case "Click":
			click(target);
			break;
		case "ClickBasedOnValue":
			clickBasedOnValue(target, value);
			break;
		case "ScrollToElement":
			scrollToElement(target);
			break;
		case "Type":
			type(target, value);
			break;
		case "VerifyText":
			verifyText(target, value);
			break;
		case "SelectDropdownOptionByVisibleText":
			selectDropdownOptionByVisibleText(target,value);
			break;
		case "AssertElementIsSelected":
			assertElementIsSelected(target);
			break;
		case "AssertElementPresent":
			assertElementPresent(target);
			break;
		case "WaitAndAssertElementPresent":
			waitAndAssertElementPresent(target);
			break;
		case "Wait":
			wait(value);
			break;
		case "AssertAttributeContains":
			assertAttributeContains(target,value);
			break;
		case "SwitchToFrame":
			switchToFrame(target);
			break;
		case "SwitchToDefaultContent":
			switchToDefaultContent();
			break;
		case "SwitchToLatestTab":
			switchToLatestTab();
			break;
		case "NavigateBack":
			navigateBack();
			break;
		case "WaitForPageLoad":
			waitForPageLoad();
			break;
		case "VerifyTableCellValue":
			verifyTableCellValue(target,value);
			break;
		case "VerifyTableRowData":
			verifyTableRowData(target,value);
			break;
		// Add more cases for other keyword interpretations
		default:
			throw new IllegalArgumentException("Unsupported keyword: " + keyword);
		}
	}

	/**
	 * Verifies cell value of a particular column based on given key column name and key column value
	 *
	 * @param target table element Eg-"xpath=//table[@id='Appaddresstable']"
	 * @param value    KeyColumnName:KeyColumnValue|ColumnNameOfTheCellToBeVerified:ExpectedCellValue
	 */
	public void verifyTableCellValue(String target, String value){

		String keyColumnName = value.split("\\|")[0].split(":")[0];
		String keyColumnValue = value.split("\\|")[0].split(":")[1];
		String columnName = value.split("\\|")[1].split(":")[0];
		String expectedColumnValue = value.split("\\|")[1].split(":")[1];

		waitForElement(target);
		By locator = getBy(target);
		// Get table element
		WebElement table = getDriver().findElement(locator);

		Table tableClass = new Table();
		String actualValue = tableClass.getTableCellValue(table,keyColumnName,keyColumnValue,columnName);
		Assert.assertEquals(actualValue, expectedColumnValue, "The Cell value of the given column name " + columnName + " is " + actualValue + " but expected value is " + expectedColumnValue);
	}

	/**
	 * Verifies row data based on given key column name and key column value
	 *
	 * @param target table element Eg-"xpath=//table[@id='Appaddresstable']"
	 * @param value    KeyColumnName:KeyColumnValue|ColumnNameOfTheCellToBeVerified1:ExpectedCellValue1|ColumnNameOfTheCellToBeVerified2:ExpectedCellValue2|so on..
	 */
	public void verifyTableRowData(String target, String value) {

		String[] columnNamesValues = value.split("\\|");
		String keyColumnName = columnNamesValues[0].split(":")[0];
		String keyColumnValue = columnNamesValues[0].split(":")[1];

		waitForElement(target);
		By locator = getBy(target);
		// Get table element
		WebElement table = getDriver().findElement(locator);
		Table tableClass = new Table();

		//Start with 1 leaving the key column name and Value
		for(int i = 1; i < columnNamesValues.length ; i++){
			String columnName = value.split("\\|")[i].split(":")[0];
			String expectedColumnValue= "";
			try {
				expectedColumnValue = value.split("\\|")[i].split(":")[1];
			}catch (Exception e){
			e.printStackTrace();
			}
			String actualValue = tableClass.getTableCellValue(table,keyColumnName,keyColumnValue,columnName);
			Assert.assertEquals(actualValue, expectedColumnValue, "The Cell value of the given column name " + columnName + " is " + actualValue + " but expected value is " + expectedColumnValue);
		}
	}


	/**
	 * Executes the test steps for the given test case name.
	 *
	 * @param testCaseName The name of the test case.
	 * @param testSteps    The list of test steps to execute.
	 */
	public void executeTestStepsOld(String testCaseName, List<String> testSteps) throws InterruptedException {
		System.out.println("Executing test case: " + testCaseName);
		for (String step : testSteps) {
			String[] parts = step.split(",");
			String keyword = parts[0].trim();
			String target = "";
			if (parts.length == 2) {
				target = parts[1].trim();
				;
			}
			String value = "";
			if (parts.length == 3) {
				value = parts[2].trim();
			}
			executeKeyword(keyword, target, value);
		}
	}
	public void executeTestSteps(Map<String,String> stepMap) throws InterruptedException {
		System.out.println("Executing test case: " + stepMap.get("Description"));
		executeKeyword(stepMap.get("Keyword"), stepMap.get("Target"), stepMap.get("Value"));

	}

	public void executeTestCasesFromExcel(String excelFile) throws InterruptedException {
		ExcelReader excelReader = new ExcelReader();
		excelReader.executeTestCasesFromExcel(excelFile);
	}

	/**
	 * Asserts that the element specified by the target is present.
	 * 
	 * @param target The target element.
	 */
	private void assertElementPresent(String target) {
		By locator = getBy(target);
		boolean isElementPresent = isElementPresent(locator);
		Assert.assertTrue(isElementPresent, "Element is not present: " + target);
	}

	/**
	 * Checks if the element specified by the locator is present.
	 * 
	 * @param locator The locator of the element.
	 * @return True if the element is present, otherwise false.
	 */
	private boolean isElementPresent(By locator) {
		try {
			getDriver().findElement(locator);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Opens the browser specified by the browserName.
	 * 
	 * @param browser The browser to open.
	 */
	@Step("Open browser: {browser}")
	public void openBrowser(Browser browser) {
		WebDriver driver;
		System.out.println("==========browser : " + browser);
		switch (browser) {
		case chrome:
			ChromeOptions co = new ChromeOptions();
			co.addArguments("--window-size=1920,1080");
            co.addArguments("--no-sandbox");
			co.setAcceptInsecureCerts(true);
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
			driver = new ChromeDriver(co);
			break;
		case firefox:
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
			driver = new FirefoxDriver(fo);
			break;
		case edge:
			driver = new EdgeDriver();
			break;
		case ie:
			driver = new InternetExplorerDriver();
			break;
		case safari:
			driver = new SafariDriver();
			break;
		default:
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}
		tlDriver.set(driver);
	}

	/**
	 * Navigates to the specified URL.
	 * 
	 * @param url The URL to navigate to.
	 */
	@Step("Navigate to URL: {url}")
	private void navigateTo(String url) {
		getDriver().navigate().to(url);
	}
	/**
	 * Waits for the current URL to contain the expected substring.
	 *
	 * @param expectedUrlPart The expected URL substring.
	 * @return true if the URL contains the substring within the timeout, false otherwise.
	 */
	public boolean waitForUrlContains(String expectedUrlPart) {
		WebDriver driver = getDriver();

		// Create a WebDriverWait instance with a timeout (e.g., 10 seconds)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			return wait.until(ExpectedConditions.urlContains(expectedUrlPart));
		} catch (TimeoutException e) {
			System.err.println("Timeout waiting for URL to contain: " + expectedUrlPart);
			return false;
		}
	}

	/**
	 * Verifies if the current URL contains the expected substring and throws an AssertionError if it does not.
	 *
	 * @param expectedSubstring The expected substring.
	 */
	public void waitAndAssertCurrentUrlContains(String expectedSubstring) {
		waitForUrlContains(expectedSubstring);
		WebDriver driver = getDriver();
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains(expectedSubstring),"Verifying current url contains expected " + expectedSubstring);
	}

	/**
	 * Clicks on the element specified by the target.
	 * 
	 * @param target The target element to click.
	 */
	@Step("Click on element: {target}")
	private void click(String target) throws InterruptedException {
		waitForElement(target);
		waitForElementClickable(target);
		//Thread.sleep(1000);
		By locator = getBy(target);
		getDriver().findElement(locator).click();
	}

	/**
	 * Clicks on the element specified by the target.
	 *
	 * @param target The target element to click.
	 * @param value  The value to type.
	 */
	@Step("Click on element: {target} based on {value}")
	private void clickBasedOnValue(String target, String value) {
		target = target.replaceFirst("\\{}",value);
		By locator = getBy(target);
		waitForElementClickable(target);
		getDriver().findElement(locator).click();
	}

	private void scrollToElement(String target) {
		By locator = getBy(target);
		WebDriver driver = getDriver();
		WebElement element = getDriver().findElement(locator);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Scroll to the vertical center of the element
		js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element
		);

		// Option 2: Scroll to the top of the element
		// js.executeScript("arguments[0].scrollIntoView(true);", element);
		// OR
		// js.executeScript("arguments[0].scrollIntoView({block: 'start'});", element);

		// Option 3: Scroll to the bottom of the element
		// js.executeScript("arguments[0].scrollIntoView(false);", element);
		// OR
		// js.executeScript("arguments[0].scrollIntoView({block: 'end'});", element);

		// Optional: Add a small pause after scrolling to ensure it's fully in view
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	/**
	 * Selects a dropdown option by visible text.
	 *
	 * @param target The target element
	 * @param value The visible text of the option to select.
	 */
	public void selectDropdownOptionByVisibleText(String target, String value) {
		By locator = getBy(target);
		try {
			WebElement dropdown = getDriver().findElement(locator);
			Select select = new Select(dropdown);
			select.selectByVisibleText(value);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			System.err.println("Dropdown or option not found: " + value);
		} catch (Exception e){
			System.err.println("An error occurred: " + e.getMessage());
		}
	}
	/**
	 * Verifies if an element is selected (e.g., a checkbox or radio button).
	 *
	 * @param target The target element
	 * @return true if the element is selected, false otherwise.
	 */
	public boolean isElementSelected(String target) {
		By locator = getBy(target);
		try {
			WebElement element = getDriver().findElement(locator);
			return element.isSelected();
		} catch (org.openqa.selenium.NoSuchElementException e) {
			System.err.println("Element not found: " + locator);
			return false; // Element not found, so it cannot be selected.
		} catch (Exception e){
			System.err.println("An error occurred while verifying selection: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Method that checks if an element is selected and throws an assertion error if not.
	 * @param target The target element
	 */
	public void assertElementIsSelected(String target){
		if(!isElementSelected(target)){
			throw new AssertionError("Element is not selected: " + target);
		}
	}

	/**
	 * Validates if an element's attribute contains the expected value.
	 *
	 * @param target The target element
	 * @param attributeName The name of the attribute.
	 * @param expectedValue The expected value of the attribute.
	 * @return true if the attribute matches, false otherwise.
	 */
	public boolean validateAttributeContains(String target, String attributeName, String expectedValue) {
		By locator = getBy(target);
		try {
			WebElement element = getDriver().findElement(locator);
			String actualValue = element.getAttribute(attributeName);
			if (actualValue != null && actualValue.contains(expectedValue)) {
				return true;
			}
			return false;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			System.err.println("Element not found: " + locator);
			return false;
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
			return false;
		}
	}
	/**
	 * Asserts that an element's attribute contains the expected value.
	 *
	 * @param target The target element.
	 * @param value (attributeName, expectedValue separated by  | )
	 */
	public void assertAttributeContains(String target, String value) {
		String attributeName = value.split("\\|")[0];
		String expectedSubstring = value.split("\\|")[1];
		if (!waitForAttributeContains(target, attributeName, expectedSubstring)) {
			Assert.fail("Attribute '" + attributeName + "' does not match expected substring value " + expectedSubstring);
		}
	}
	/**
	 * Waits for an element's attribute to have the expected value.
	 *
	 * @param target The target element
	 * @param attributeName The name of the attribute.
	 * @param expectedSubstring The expected value of the attribute.
	 * @return true if the attribute has the expected value within the timeout, false otherwise.
	 */
	public boolean waitForAttributeContains(String target, String attributeName, String expectedSubstring) {
		waitForElement(target);
		By locator = getBy(target);
		WebDriver driver = getDriver();

		// Create a WebDriverWait instance with a timeout (e.g., 10 seconds)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			return wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					try {
						WebElement element = driver.findElement(locator);
						String actualValue = element.getAttribute(attributeName);
						return actualValue != null && actualValue.contains(expectedSubstring);
					} catch (org.openqa.selenium.NoSuchElementException e) {
						return false; // Element not found, attribute cannot match.
					}
				}
			});
		} catch (TimeoutException e) {
			System.err.println("Timeout waiting for attribute '" + attributeName + "' to contain '" + expectedSubstring + "': " + locator);
			return false;
		}
	}
	/**
	 * Types the specified value into the element specified by the target.
	 * 
	 * @param target The target element to type into.
	 * @param value  The value to type.
	 */
	@Step("Type '{value}' into element: {target}")
	private void type(String target, String value) {
		By locator = getBy(target);
		WebElement element = getDriver().findElement(locator);
		element.clear();
		element.sendKeys(value);
	}

	/**
	 * Verifies that the text in the element specified by the target matches the
	 * expectedText.
	 * 
	 * @param target       The target element containing the text to verify.
	 * @param expectedText The expected text.
	 */
	@Step("Verify text '{expectedText}' in element: {target}")
	private void verifyText(String target, String expectedText) {
		waitForElement(target);
		By locator = getBy(target);
		String actualText = getDriver().findElement(locator).getText();
		Assert.assertEquals(actualText, expectedText, "Text verification failed!");
	}

	/**
	 * Switches to the frame specified by the frameName.
	 * 
	 * @param frameName The name or ID of the frame to switch to.
	 */
	@Step("Switch to frame: {frameName}")
	private void switchToFrame(String frameName) {
		getDriver().switchTo().frame(frameName);
	}

	/**
	 * Switches to the latest tab.
	 */
	private void switchToLatestTab(){
		WebDriver driver = getDriver();
		parentWindowhandle = driver.getWindowHandle();
		// Get all window handles
		List<String> tabs = new ArrayList<>(driver.getWindowHandles());

		for(String eachTab : tabs){
			if(!eachTab.equals(parentWindowhandle)){
				driver.switchTo().window(eachTab);
			}
		}

	}

	private void closeChildTabAndSwitchToParentTab(){
		WebDriver driver = getDriver();
		//close current tab
		driver.close();
		//Switch to parent window tab
		driver.switchTo().window(parentWindowhandle);
	}

	private void navigateBack(){
		WebDriver driver = getDriver();
		driver.navigate().back();
	}

	/**
	 * Waits for the page to load completely.
	 */
	private void waitForPageLoad() {
		WebDriver driver = getDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Use Duration
		wait.until((ExpectedCondition<Boolean>) wd ->
				((org.openqa.selenium.JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
	}

	/**
	 * Switches to the default content.
	 */
	@Step("Switch to default content")
	private void switchToDefaultContent() {
		getDriver().switchTo().defaultContent();
	}

	/**
	 * Retrieves the By locator for the specified target.
	 * 
	 * @param target The target element.
	 * @return The By locator for the target.
	 */
	private By getBy(String target) {
		By locator;
		if (target.startsWith("id=")) {
			locator = By.id(target.substring(3));
		} else if (target.startsWith("name=")) {
			locator = By.name(target.substring(5));
		} else if (target.startsWith("class=")) {
			locator = By.className(target.substring(6));
		} else if (target.startsWith("xpath=")) {
			locator = By.xpath(target.substring(6));
		} else if (target.startsWith("css=")) {
			locator = By.cssSelector(target.substring(4));
		} else if (target.startsWith("linktext=")) {
			locator = By.linkText(target.substring(9));
		} else {
			throw new IllegalArgumentException("Unsupported locator format: " + target);
		}
		return locator;
	}

	/**
	 * Retrieves the WebDriver instance.
	 * 
	 * @return The WebDriver instance.
	 */
	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	/**
	 * Takes a screenshot of the current WebDriver instance and saves it to the
	 * specified file.
	 * 
	 * @param methodName The name of the method or test.
	 * @return The path to the saved screenshot file.
	 */
	public static String getScreenshot(String methodName) {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/screenshot/" + methodName + "_" + System.currentTimeMillis()
				+ ".png";
		File destination = new File(path);
		try {
			FileHandler.copy(srcFile, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	/**
	 * @param value  milliseconds to wait
	 */
	@Step("Waiting for milliseconds: {value}")
	public void wait(String value) throws InterruptedException {
		Thread.sleep(Integer.parseInt(value));
	}
	public void waitForElement(String target) {
		By locator = getBy(target);
		WebDriver driver = getDriver();

		// Create a WebDriverWait instance with a timeout (e.g., 10 seconds)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			// Wait for the element to be visible
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		} catch (org.openqa.selenium.TimeoutException e) {
			System.err.println("Element not found within the timeout.");
			// Handle the timeout exception (e.g., take a screenshot, fail the test)
			e.printStackTrace();
		}
	}

	public void waitForElementClickable(String target) {
		By locator = getBy(target);
		WebDriver driver = getDriver();

		// Create a WebDriverWait instance with a timeout (e.g., 10 seconds)
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			// Wait for the element to be visible
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));


		} catch (org.openqa.selenium.TimeoutException e) {
			System.err.println("Element not found within the timeout.");
			// Handle the timeout exception (e.g., take a screenshot, fail the test)
			e.printStackTrace();
		}
	}

	public void waitAndAssertElementPresent(String target) {
		waitForElement(target);
		assertElementPresent(target);
	}
}
