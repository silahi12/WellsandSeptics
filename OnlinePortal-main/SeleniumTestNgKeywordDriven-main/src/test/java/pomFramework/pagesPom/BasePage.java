package pomFramework.pagesPom;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pomFramework.driverPom.DriverManagerPom;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * BasePage class provides common functionalities and initializes Page Objects.
 * All other page classes should extend this class.
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManagerPom.getDriverPom(); // Get the WebDriver instance from ThreadLocal
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Initialize WebDriverWait
        PageFactory.initElements(driver, this); // Initialize WebElements declared in page classes
    }

    // Common functions like click, wait, sendKeys, etc., are kept here.

    /**
     * Clicks on a WebElement after waiting for it to be clickable.
     *
     * @param element The WebElement to click.
     */
    public void clickPom(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Scroll to and Clicks on a WebElement after waiting for it to be clickable.
     *
     * @param element The WebElement to click.
     */
    public void scrollToAndClickPom(WebElement element) {
        scrollToElementPom(element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }
    public void clickVisibleElementPom(List<WebElement> elements) {
        boolean clicked = false;

        for (WebElement element : elements) {
            try {
                // Check if element is displayed and has height/width
                if (element.isDisplayed() && element.getSize().getHeight() > 0) {
                    // Scroll to it to ensure it's in the viewport
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

                    // Wait briefly for clickability and click
                    wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                    clicked = true;
                    break; // Exit loop once the visible one is clicked
                }
            } catch (Exception e) {
                // If standard click fails (e.g. element intercepted), try JS click as a backup
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    clicked = true;
                    break;
                } catch (Exception jsEx) {
                    // Continue to next element in list if this one is truly not interactable
                    continue;
                }
            }
        }

        if (!clicked) {
            throw new RuntimeException("Could not find a visible 'element' to click.");
        }
    }
    /**
     * Sends text to a WebElement after waiting for it to be visible.
     *
     * @param element The WebElement to send keys to.
     * @param text The text to send.
     */
    public void sendKeysPom(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    }

    /**
     * Clears the existing text in a WebElement and then sends new text to it.
     *
     * @param element The WebElement input field to clear and send text to.
     * @param text The text to send to the element.
     */
    public void clearAndSendKeys(WebElement element, String text) {
        try {
            waitForVisibilityPom(element); // Ensure the element is visible
            element.clear(); // Clear any existing text
            element.sendKeys(text); // Send the new text
            System.out.println("Cleared text and sent '" + text + "' to element: " + element.toString());
        } catch (Exception e) {
            System.err.println("Failed to clear and send keys to element: " + element.toString() + ". Error: " + e.getMessage());
            throw new RuntimeException("Could not clear and send keys to element.", e);
        }
    }

    /**
     * Waits for a WebElement to be visible.
     *
     * @param element The WebElement to wait for.
     * @return The visible WebElement.
     */
    public WebElement waitForVisibilityPom(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Gets the text of a WebElement after waiting for it to be visible.
     *
     * @param element The WebElement to get text from.
     * @return The text of the WebElement.
     */
    public String getTextPom(WebElement element) {
        return waitForVisibilityPom(element).getText();
    }

    /**
     * Verifies if the actual text of a WebElement equals the expected text.
     *
     * @param element The WebElement whose text is to be verified.
     * @param expectedText The expected text.
     * @return true if the actual text equals the expected text, false otherwise.
     */
    public boolean verifyText(WebElement element, String expectedText) {
        try {
            waitForVisibilityPom(element); // Ensure the element is visible before getting its text
            String actualText = element.getText();
            boolean matches = actualText.equals(expectedText);
            if (!matches) {
                System.err.println("Text mismatch for element: " + element.toString() + ". Expected: '" + expectedText + "', Actual: '" + actualText + "'");
            }
            return matches;
        } catch (Exception e) {
            System.err.println("Failed to verify text for element: " + element.toString() + ". Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Asserts that the actual text of a WebElement equals the expected text.
     *
     * @param element The WebElement whose text is to be asserted.
     * @param expectedText The expected text.
     * @param elementName A descriptive name for the element for assertion messages.
     */
    public void assertTextEquals(WebElement element, String expectedText, String elementName) {
        String actualText = null;
        try {
            waitForVisibilityPom(element); // Ensure the element is visible before getting its text
            actualText = element.getText();
        } catch (Exception e) {
            System.err.println("Could not retrieve text for element '" + elementName + "'. Error: " + e.getMessage());
            Assert.fail("Failed to get text for element '" + elementName + "'. Expected: '" + expectedText + "', Actual: <not retrievable>");
        }
        Assert.assertEquals(actualText, expectedText,
                "Text mismatch for element '" + elementName + "'.");
    }

    /**
     * Navigates to a specified URL.
     *
     * @param url The URL to navigate to.
     */
    public void navigateToPOM(String url) {
        driver.get(url);
    }

    /**
     * Switches the WebDriver's focus to the latest (most recently opened) browser tab or window.
     * This is useful when actions in your test open a new tab/window.
     */
    public void switchToLatestTab() {
        try {
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);
            if (tabs.size() > 1) {
                // Switch to the last tab in the list (which is typically the newly opened one)
                driver.switchTo().window(tabs.get(tabs.size() - 1));
                System.out.println("Switched to the latest tab.");
            } else {
                System.out.println("Only one tab is open. No switch needed.");
            }
        } catch (Exception e) {
            System.err.println("Failed to switch to the latest tab. Error: " + e.getMessage());
            throw new RuntimeException("Could not switch to latest tab.", e);
        }
    }


    /**
     * Navigates back to the previous page in the browser's history.
     */
    public void navigateBackPom() {
        try {
            driver.navigate().back();
            System.out.println("Navigated back to the previous page.");
        } catch (Exception e) {
            System.err.println("Failed to navigate back. Error: " + e.getMessage());
            throw new RuntimeException("Could not navigate back.", e);
        }
    }

    /**
     * Waits for the page to completely load by checking the document.readyState.
     * This function uses JavascriptExecutor to check the page's readiness state.
     */
    public void waitForPageLoadPom() {
        try {
            // Cast WebDriver to JavascriptExecutor
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Wait until document.readyState is 'complete'
            wait.until((Function<WebDriver, Boolean>) wd ->
                    js.executeScript("return document.readyState").equals("complete"));

            System.out.println("Page has finished loading (document.readyState = 'complete').");
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for page to load completely. Current document.readyState: " +
                    ((JavascriptExecutor) driver).executeScript("return document.readyState"));
            throw new RuntimeException("Page did not load completely within the timeout.", e);
        } catch (Exception e) {
            System.err.println("An error occurred while waiting for page load. Error: " + e.getMessage());
            throw new RuntimeException("Error during page load wait.", e);
        }
    }

    /**
     * Checks if a WebElement is displayed on the page.
     * This will also implicitly wait for its visibility up to the default wait timeout.
     *
     * @param element The WebElement to check.
     * @return True if the element is visible and displayed, otherwise false.
     */
    public boolean isElementDisplayedPom(WebElement element) {
        try {
            return waitForVisibilityPom(element).isDisplayed();
        } catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    /**
     * Waits for a WebElement to become invisible or not present in the DOM.
     * This is useful for waiting for loading indicators or elements that disappear after an action.
     *
     * @param element The WebElement to wait for its disappearance.
     * @return true if the element disappears within the timeout, false otherwise.
     */
    public boolean waitForElementToDisappear(WebElement element) {
        try {
            // Using invisibilityOf(WebElement) to wait for the specific element to become invisible.
            // This also handles cases where the element is removed from the DOM.
            wait.until(ExpectedConditions.invisibilityOf(element));
            System.out.println("Element " + element.toString() + " has disappeared.");
            return true;
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for element " + element.toString() + " to disappear. Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An error occurred while waiting for element to disappear: " + e.getMessage());
            return false;
        }
    }

    /**
     * Asserts that a WebElement is displayed on the page.
     *
     * @param element The WebElement to assert.
     * @param elementName A descriptive name for the element for assertion messages.
     */
    public void assertElementDisplayedPom(WebElement element, String elementName) {
        boolean isDisplayed = isElementDisplayedPom(element);
        Assert.assertTrue(isDisplayed, "Element '" + elementName + "' is not displayed on the page.");
    }

    /**
     * Waits for the current URL to contain the expected substring within the configured timeout.
     *
     * @param expectedUrlPart The expected URL substring.
     * @return true if the URL contains the substring within the timeout, false otherwise.
     */
    public boolean waitForUrlContainsPom(String expectedUrlPart) {
        try {
            return wait.until(ExpectedConditions.urlContains(expectedUrlPart));
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for URL to contain: " + expectedUrlPart + ". Current URL: " + driver.getCurrentUrl());
            return false;
        }
    }

    /**
     * Verifies if the current URL contains the expected substring after waiting for it,
     * and throws an AssertionError if it does not.
     *
     * @param expectedSubstring The expected substring.
     */
    public void waitAndAssertCurrentUrlContainsPom(String expectedSubstring) {
        // First, wait for the URL to contain the substring
        boolean urlContains = waitForUrlContainsPom(expectedSubstring);

        // Then, assert the condition. This ensures the assertion message is clear
        // even if waitForUrlContains returned false due to a timeout.
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(urlContains, "Current URL '" + currentUrl + "' does not contain expected substring: '" + expectedSubstring + "' within the timeout.");
    }

    /**
     * Scrolls the browser window to make the specified WebElement visible in the viewport.
     * This uses JavascriptExecutor to perform the scroll action.
     *
     * @param element The WebElement to scroll to.
     */
    public void scrollToElementPom(WebElement element) {
        try {
            // Ensure the element is present in the DOM before attempting to scroll
            waitForVisibilityPom(element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            System.out.println("Scrolled to element: " + element.toString());
        } catch (Exception e) {
            System.err.println("Failed to scroll to element: " + element.toString() + ". Error: " + e.getMessage());
            // Optionally re-throw or handle based on framework's error policy
            throw new RuntimeException("Could not scroll to element: " + element.toString(), e);
        }
    }

    /**
     * Selects an option from a dropdown (select element) by its visible text.
     *
     * @param dropdownElement The WebElement representing the <select> dropdown.
     * @param optionText The visible text of the option to select.
     */
    public void selectDropdownOptionByVisibleTextPom(WebElement dropdownElement, String optionText) {
        try {
            // Ensure the dropdown element is visible and enabled before interacting
            wait.until(ExpectedConditions.elementToBeClickable(dropdownElement));
            Select select = new Select(dropdownElement);
            select.selectByVisibleText(optionText);
            System.out.println("Selected option '" + optionText + "' from dropdown: " + dropdownElement.toString());
        } catch (NoSuchElementException e) {
            System.err.println("Option with visible text '" + optionText + "' not found in dropdown: " + dropdownElement.toString() + ". Error: " + e.getMessage());
            throw new RuntimeException("Option not found in dropdown.", e);
        } catch (Exception e) {
            System.err.println("Failed to select option from dropdown: " + dropdownElement.toString() + ". Error: " + e.getMessage());
            throw new RuntimeException("Could not select dropdown option.", e);
        }
    }

    /**
     * Checks if a WebElement (e.g., checkbox, radio button, option in a multi-select) is currently selected.
     *
     * @param element The WebElement to check its selection status.
     * @return true if the element is selected, false otherwise.
     */
    public boolean isElementSelected(WebElement element) {
        try {
            waitForVisibilityPom(element); // Ensure the element is visible before checking its state
            return element.isSelected();
        } catch (Exception e) {
            System.err.println("Failed to check selection status for element: " + element.toString() + ". Error: " + e.getMessage());
            // Depending on desired behavior, you might re-throw or return false.
            // Returning false here means it's not selected or couldn't be checked.
            return false;
        }
    }

    /**
     * Asserts that a WebElement (e.g., checkbox, radio button, option) is currently selected.
     *
     * @param element The WebElement to assert its selection status.
     * @param elementName A descriptive name for the element for assertion messages.
     */
    public void assertElementIsSelected(WebElement element, String elementName) {
        boolean selected = isElementSelected(element);
        Assert.assertTrue(selected, "Element '" + elementName + "' is not selected.");
    }

    /**
     * Sets the state of a checkbox (checks or unchecks it).
     * The method will only click the checkbox if its current state does not match the desired state.
     *
     * @param checkboxElement The WebElement representing the checkbox.
     * @param check True to check the checkbox, false to uncheck it.
     */
    public void setCheckboxState(WebElement checkboxElement, boolean check) {
        try {
            waitForVisibilityPom(checkboxElement); // Ensure checkbox is visible
            if (checkboxElement.isSelected() != check) {
                // Only click if the current state is not the desired state
                wait.until(ExpectedConditions.elementToBeClickable(checkboxElement)).click();
                System.out.println("Checkbox " + (check ? "checked" : "unchecked") + ": " + checkboxElement.toString());
            } else {
                System.out.println("Checkbox is already " + (check ? "checked" : "unchecked") + ": " + checkboxElement.toString());
            }
        } catch (Exception e) {
            System.err.println("Failed to set checkbox state for element: " + checkboxElement.toString() + ". Error: " + e.getMessage());
            throw new RuntimeException("Could not set checkbox state.", e);
        }
    }

    /**
     * Validates if a specific attribute of a WebElement contains the expected text.
     *
     * @param element The WebElement to check.
     * @param attributeName The name of the attribute (e.g., "value", "class", "title").
     * @param expectedText The text expected to be contained within the attribute's value.
     * @return true if the attribute's value contains the expected text, false otherwise.
     */
    public boolean validateAttributeContains(WebElement element, String attributeName, String expectedText) {
        try {
            waitForVisibilityPom(element); // Ensure the element is visible before checking its attribute
            String attributeValue = element.getAttribute(attributeName);
            if (attributeValue != null) {
                return attributeValue.contains(expectedText);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to validate attribute '" + attributeName + "' for element: " + element.toString() + ". Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Asserts that a specific attribute of a WebElement contains the expected text.
     *
     * @param element The WebElement to check.
     * @param attributeName The name of the attribute (e.g., "value", "class", "title").
     * @param expectedText The text expected to be contained within the attribute's value.
     * @param elementName A descriptive name for the element for assertion messages.
     */
    public void assertAttributeContains(WebElement element, String attributeName, String expectedText, String elementName) {
        boolean contains = validateAttributeContains(element, attributeName, expectedText);
        String actualAttributeValue = null;
        try {
            actualAttributeValue = element.getAttribute(attributeName);
        } catch (Exception ignored) {
            // Element might become stale or disappear, use null if cannot retrieve
        }
        Assert.assertTrue(contains,
                "Attribute '" + attributeName + "' of element '" + elementName +
                        "' did not contain expected text. Expected to contain: '" + expectedText +
                        "', Actual value: '" + actualAttributeValue + "'");
    }

    /**
     * Waits for a specific attribute of a WebElement to contain the expected text within the configured timeout.
     *
     * @param element The WebElement to check.
     * @param attributeName The name of the attribute (e.g., "value", "class", "title").
     * @param expectedText The text expected to be contained within the attribute's value.
     * @return true if the attribute's value contains the expected text within the timeout, false otherwise.
     */
    public boolean waitForAttributeContains(WebElement element, String attributeName, String expectedText) {
        try {
            return wait.until(ExpectedConditions.attributeContains(element, attributeName, expectedText));
        } catch (TimeoutException e) {
            String actualAttributeValue = null;
            try {
                // Try to get the attribute value one last time for the error message
                actualAttributeValue = element.getAttribute(attributeName);
            } catch (Exception ignored) {
                // Element might be stale or not found if TimeoutException happened
                actualAttributeValue = " (attribute not found or element stale)";
            }
            System.err.println("Timeout waiting for attribute '" + attributeName + "' of element " + element.toString() +
                    " to contain: '" + expectedText + "'. Current value: '" + actualAttributeValue + "'");
            return false;
        } catch (Exception e) {
            System.err.println("An error occurred while waiting for attribute '" + attributeName + "' of element " + element.toString() +
                    " to contain: '" + expectedText + "'. Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Takes a screenshot of the current WebDriver instance and saves it to the
     * specified file.
     *
     * @param methodName The name of the method or test.
     * @return The path to the saved screenshot file.
     */
    public static String getScreenshotPom(String methodName) {
        File srcFile = ((TakesScreenshot) DriverManagerPom.getDriverPom()).getScreenshotAs(OutputType.FILE);
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
}
