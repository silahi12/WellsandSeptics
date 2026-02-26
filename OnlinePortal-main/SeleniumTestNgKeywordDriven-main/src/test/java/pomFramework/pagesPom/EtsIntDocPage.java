package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class EtsIntDocPage extends BasePage {

    public String etsIntDocPageUrl = "/Application/Edit?INT_DOC_ID=";


    // WebElements, identified using @FindBy annotations.
    @FindBy(xpath = "//h2[text()='CROMERR Copy of Record']")
    private WebElement cromerrCopyOfRecordHeader;

    @FindBy(id = "FileDocumentManagement")
    private WebElement fileDocMangTab;


    public EtsIntDocPage() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    public void waitForIntDocPage(){
        waitForUrlContainsPom(etsIntDocPageUrl);
        waitForPageLoadPom();
        waitForVisibilityPom(cromerrCopyOfRecordHeader);
    }

    public void clickFileDocMngTab(){
        waitForVisibilityPom(fileDocMangTab);
        clickPom(fileDocMangTab);
        waitForPageLoadPom();
        waitForAttributeContains(fileDocMangTab, "class", "active");
    }

    //File tab functions
    public void verifyFileNameInTable(String fileName) throws InterruptedException {
        Thread.sleep(2000);
        //verify each filename exists
        String dynamicXpath = "//td[contains(@id,'FILE_DESC') and normalize-space(text())='" + fileName +"']";
        By fileLocator = By.xpath(dynamicXpath);
        try {
            WebElement element = driver.findElement(fileLocator);
            waitForVisibilityPom(element);
            System.out.println("Successfully verified file: " + fileName);

        } catch (TimeoutException e) {
            String errorMessage = String.format("Error: The expected file '%s' was NOT found in the files table within the timeout.", fileName);
            throw new AssertionError(errorMessage);
        }
//            // We use findElements() because it returns a list of size 0 if not found,
//            // instead of throwing an exception, which allows us to handle the failure gracefully.
//            List<WebElement> elementsFound = driver.findElements(fileLocator);
//            if (elementsFound.isEmpty()) {
//                // If the list is empty (size 0), the file element was not found.
//                String errorMessage = String.format("Error: The expected file '%s' was NOT found in the files table.", fileName);
//
//                // Throw an error to fail the test immediately
//                throw new AssertionError(errorMessage);
//
//            } else {
//                // Element found (list size > 0)
//                System.out.println("Successfully verified file: " + fileName);
//            }

    }




}
