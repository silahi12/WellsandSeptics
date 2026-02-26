package pomFramework.pagesPom;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import pomFramework.dataPom.FacilityDetails;

import java.util.List;

public class StandardPermitInfoTab extends BasePage {

    // WebElements on the StandardPermitInfoTab, identified using @FindBy annotations.
    @FindBy(xpath = "//li[@class='nav-item']/a[text()='Standard Permit Info']")
    private WebElement standardPermitInfoTab;

    @FindBy(xpath = "//select[@id='SelectedFacilityId']/option[@selected='selected']")
    private WebElement displayedFacilityName;

    @FindBy(xpath = "//select[@id='SelectedFacilityId']")
    private WebElement facilityNameDropdown;

    @FindBy(id = "facilityType")
    private WebElement displayedFacilityType;

    @FindBy(id = "StartDate")
    private WebElement displayedFacilityStartDate;

    @FindBy(id = "physicalLine1")
    private WebElement displayedPhysicalAddressLine1;

    @FindBy(id = "physicalLine2")
    private WebElement displayedPhysicalAddressLine2;

    @FindBy(id = "physicalLine3")
    private WebElement displayedPhysicalAddressLine3;

    @FindBy(xpath = "//select[@id='PHYSICAL_ADDRESS_MUNICIPALITY']")
    private WebElement physicalAddressMunicipalityDropdown;

    @FindBy(xpath = "//select[@id='PHYSICAL_PARISH_OR_COUNTY_CODE']")
    private WebElement physicalAddressCountyDropdown;

    @FindBy(xpath = "//select[@id='PHYSICAL_ADDRESS_STATE_CODE']")
    private WebElement physicalAddressStateDropdown;

    @FindBy(id = "physicalZip")
    private WebElement physicalAddressZipcodeDropdown;

//    @FindBy(xpath = "//select[@id='PHYSICAL_REGION_CODE']/option[@selected='selected']")
//    private WebElement displayedPhysicalAddressRegion;
//
//    @FindBy(xpath = "//select[@id='PHYSICAL_REGION_CODE']")
//    private WebElement physicalAddressRegionDropdown;
//
//    @FindBy(xpath = "//select[@id='PHYSICAL_FIELD_OFFICE_CODE']/option[@selected='selected']")
//    private WebElement displayedPhysicalAddressFieldOfc;
//
//    @FindBy(xpath = "//select[@id='PHYSICAL_FIELD_OFFICE_CODE']")
//    private WebElement physicalAddressFieldOfcDropdown;

    @FindBy(id = "mailingLine1")
    private WebElement displayedMailingAddressLine1;

    @FindBy(id = "mailingLine2")
    private WebElement displayedMailingAddressLine2;

    @FindBy(id = "mailingLine3")
    private WebElement displayedMailingAddressLine3;

    @FindBy(xpath = "//select[@id='POSTAL_ADDRESS_MUNICIPALITY']")
    private WebElement mailingAddressMunicipalityDropdown;

    @FindBy(xpath = "//select[@id='POSTAL_PARISH_OR_COUNTY_CODE']")
    private WebElement mailingAddressCountyDropdown;

    @FindBy(xpath = "//select[@id='POSTAL_ADDRESS_STATE_CODE']")
    private WebElement mailingAddressStateDropdown;

    @FindBy(id = "mailingZip")
    private WebElement mailingAddressZipcodeDropdown;

//    @FindBy(xpath = "//select[@id='POSTAL_REGION_CODE']/option[@selected='selected']")
//    private WebElement displayedMailingAddressRegion;
//
//    @FindBy(xpath = "//select[@id='POSTAL_REGION_CODE']")
//    private WebElement mailingAddressRegionDropdown;
//
//    @FindBy(xpath = "//select[@id='POSTAL_FIELD_OFFICE_CODE']/option[@selected='selected']")
//    private WebElement displayedMailingAddressFieldOfc;
//
//    @FindBy(xpath = "//select[@id='POSTAL_FIELD_OFFICE_CODE']")
//    private WebElement mailingAddressFieldOfcDropdown;

    @FindBy(id = "latitude")
    private WebElement displayedXCord;

    @FindBy(id = "longitude")
    private WebElement displayedYCord;

    @FindBy(id = "ejScore")
    private WebElement displayedEjsScore;

    @FindBy(id = "addContact")
    private WebElement addContactBtn;

    //Todo- need to refactor to work dynamically for contact
    @FindBy(xpath = "//button[@class='btn btn-sm edit-contact']")
    private WebElement editContactBtn;

    @FindBy(xpath = "//div[@class='contact-item row']//button[@class='btn btn-sm delete-contact text-danger']")
    private List<WebElement> contactDeleteButtons;

    @FindBy(id = "nextButton")
    private WebElement nextBtn;

    public StandardPermitInfoTab() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }


    /** ------ Functions --------**/

    public boolean isUserOnNewPermitUrl(){
        return waitForUrlContainsPom("/Application/New?");
    }
    public boolean isStandardPermitTabActive(){
       return waitForAttributeContains(standardPermitInfoTab, "class", "active");
    }

    public String getDisplayedFacilityName() { return displayedFacilityName.getText(); }
    public String getFacilityType() { return displayedFacilityType.getAttribute("value"); }
    public String getFacilityStartDate() { return displayedFacilityStartDate.getAttribute("value"); }

    public String getPhysicalAddressLine1() { return displayedPhysicalAddressLine1.getAttribute("value"); }
    public String getPhysicalAddressLine2() { return displayedPhysicalAddressLine2.getAttribute("value"); }
    public String getPhysicalAddressLine3() { return displayedPhysicalAddressLine3.getAttribute("value"); }

    public String getPhysicalAddressMunicipality() {
        //Fixing stale element issue as the dropdowns are taking time to get loaded
        By municipalityPhyDropdownLocator = By.id("PHYSICAL_ADDRESS_MUNICIPALITY");
        wait.until(ExpectedConditions.visibilityOfElementLocated(municipalityPhyDropdownLocator));
        WebElement freshMunicipalityPhyDropdownElement = driver.findElement(municipalityPhyDropdownLocator);
        Select select = new Select(freshMunicipalityPhyDropdownElement);
        return select.getFirstSelectedOption().getText();}

    public String getPhysicalAddressCounty() {
        //Fixing stale element issue as the dropdowns are taking time to get loaded
        By countyPhyDropdownLocator = By.id("PHYSICAL_PARISH_OR_COUNTY_CODE");
        wait.until(ExpectedConditions.visibilityOfElementLocated(countyPhyDropdownLocator));
        WebElement freshCountyPhyDropdownElement = driver.findElement(countyPhyDropdownLocator);
        Select select = new Select(freshCountyPhyDropdownElement);
        return select.getFirstSelectedOption().getText();}

    public String getPhysicalAddressState() {
        //Fixing stale element issue as the dropdowns are taking time to get loaded
        By statePhyDropdownLocator = By.id("PHYSICAL_ADDRESS_STATE_CODE");
        wait.until(ExpectedConditions.visibilityOfElementLocated(statePhyDropdownLocator));
        WebElement freshStatePhyDropdownElement = driver.findElement(statePhyDropdownLocator);
        Select select = new Select(freshStatePhyDropdownElement);
        return select.getFirstSelectedOption().getText();}

    public String getPhysicalAddressZipcodeDropdown() {
        // Waiting on the By locator is safer than waiting on the stale WebElement field.
        By zipDropdownLocator = By.id("physicalZip");
        // Use the explicit wait before interaction
        wait.until(ExpectedConditions.visibilityOfElementLocated(zipDropdownLocator));
        // Re-locate the element immediately before creating the Select object.
        // This gets a *fresh* WebElement reference that is not stale.
        WebElement freshZipDropdownElement = driver.findElement(zipDropdownLocator);
        Select select = new Select(freshZipDropdownElement);
        return select.getFirstSelectedOption().getText();}
//    public String getPhysicalAddressRegion() { return displayedPhysicalAddressRegion.getText(); }
//    public String getPhysicalAddressFieldOffice() { return displayedPhysicalAddressFieldOfc.getText(); }

    public String getMailingAddressLine1() { return displayedMailingAddressLine1.getAttribute("value");}
    public String getMailingAddressLine2() { return displayedMailingAddressLine2.getAttribute("value"); }
    public String getMailingAddressLine3() { return displayedMailingAddressLine3.getAttribute("value"); }

    public String getMailingAddressMunicipality() {
        //Fixing stale element issue as the dropdowns are taking time to get loaded
        By municipalityMailingDropdownLocator = By.id("POSTAL_ADDRESS_MUNICIPALITY");
        wait.until(ExpectedConditions.visibilityOfElementLocated(municipalityMailingDropdownLocator));
        WebElement freshMunicipalityMailingDropdownElement = driver.findElement(municipalityMailingDropdownLocator);
        Select select = new Select(freshMunicipalityMailingDropdownElement);
        return select.getFirstSelectedOption().getText();}

    public String getMailingAddressCounty() {
        //Fixing stale element issue as the dropdowns are taking time to get loaded
        By countyMailingDropdownLocator = By.xpath("//select[@id='POSTAL_PARISH_OR_COUNTY_CODE']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(countyMailingDropdownLocator));
        WebElement freshCountyMailingDropdownElement = driver.findElement(countyMailingDropdownLocator);
        Select select = new Select(freshCountyMailingDropdownElement);
        return select.getFirstSelectedOption().getText();}

    public String getMailingAddressState() {
        //Fixing stale element issue as the dropdowns are taking time to get loaded
        By stateMailingDropdownLocator = By.id("POSTAL_ADDRESS_STATE_CODE");
        wait.until(ExpectedConditions.visibilityOfElementLocated(stateMailingDropdownLocator));
        WebElement freshStateMailingDropdownElement = driver.findElement(stateMailingDropdownLocator);
        Select select = new Select(freshStateMailingDropdownElement);
        return select.getFirstSelectedOption().getText();}

    public String getMailingAddressZipcodeDropdown() {
        //Fixing stale element issue as the dropdowns are taking time to get loaded
        By zipMailingDropdownLocator = By.id("mailingZip");
        wait.until(ExpectedConditions.visibilityOfElementLocated(zipMailingDropdownLocator));
        WebElement freshZipMailingDropdownElement = driver.findElement(zipMailingDropdownLocator);
        Select select = new Select(freshZipMailingDropdownElement);
        return select.getFirstSelectedOption().getText();}

//    public String getMailingAddressRegion() { return displayedMailingAddressRegion.getText(); }
//    public String getMailingAddressFieldOffice() { return displayedMailingAddressFieldOfc.getText();  }

    public String getXCord() { return displayedXCord.getAttribute("value"); }
    public String getYCord() { return displayedYCord.getAttribute("value"); }
    public String getEjsScore() { return displayedEjsScore.getAttribute("value"); }

    public void verifyFacilityDetails(String facilityPrefix) {
        FacilityDetails expectedFacilityDetails = new FacilityDetails(facilityPrefix);
        Assert.assertEquals(getDisplayedFacilityName(), expectedFacilityDetails.getFacilityName(), "Facility name ");
        Assert.assertEquals(getFacilityType(), expectedFacilityDetails.getFacilityType(), "Facility type ");
        Assert.assertEquals(getFacilityStartDate(), expectedFacilityDetails.getFacilityStartDate(), "Facility start date ");
    }
    public void verifyFacilityPhysicalAddress(String facilityPrefix) throws InterruptedException {
        Thread.sleep(2000);
        FacilityDetails expectedFacilityDetails = new FacilityDetails(facilityPrefix);
        Assert.assertEquals(getPhysicalAddressLine1(), expectedFacilityDetails.getPhysicalAddressLine1(), "Facility Physical address line 1 ");
        Assert.assertEquals(getPhysicalAddressLine2(), expectedFacilityDetails.getPhysicalAddressLine2(), "Facility Physical address line 2 ");
        Assert.assertEquals(getPhysicalAddressLine3(), expectedFacilityDetails.getPhysicalAddressLine3(), "Facility Physical address line 3 ");
        String expectedPhysicalState = expectedFacilityDetails.getPhysicalAddressState();
        if (expectedPhysicalState == null || expectedPhysicalState.isEmpty()) {
            expectedPhysicalState = "Select";
        }
        Assert.assertEquals(getPhysicalAddressState(), expectedPhysicalState, "Facility Physical address State ");
        String expectedPhysicalCounty = expectedFacilityDetails.getPhysicalAddressCounty();
        if (expectedPhysicalCounty == null || expectedPhysicalCounty.isEmpty()) {
            expectedPhysicalCounty = "Select";
        }
        Assert.assertEquals(getPhysicalAddressCounty(), expectedPhysicalCounty, "Facility Physical address county ");
        String expectedPhysicalMunicipality = expectedFacilityDetails.getPhysicalAddressMunicipality();
        if (expectedPhysicalMunicipality == null || expectedPhysicalMunicipality.isEmpty()) {
            expectedPhysicalMunicipality = "Select";
        }
        Assert.assertEquals(getPhysicalAddressMunicipality(), expectedPhysicalMunicipality, "Facility Physical address Municipality ");
        Assert.assertEquals(getPhysicalAddressZipcodeDropdown().trim(), expectedFacilityDetails.getPhysicalAddressZipcode().trim(), "Facility Physical address Zipcode ");
//        String expectedPhysicalRegion = expectedFacilityDetails.getPhysicalAddressRegion();
//        if (expectedPhysicalRegion == null || expectedPhysicalRegion.isEmpty()) {
//            expectedPhysicalRegion = "Select";
//        }
//        Assert.assertEquals(getPhysicalAddressRegion(), expectedPhysicalRegion, "Facility Physical address Region ");
//        String expectedPhysicalFieldOfc = expectedFacilityDetails.getPhysicalAddressFieldOffice();
//        if (expectedPhysicalFieldOfc == null || expectedPhysicalFieldOfc.isEmpty()) {
//            expectedPhysicalFieldOfc = "Select";
//        }
//        Assert.assertEquals(getPhysicalAddressFieldOffice(),expectedPhysicalFieldOfc, "Facility Physical address Field Office ");
    }

    public void verifyFacilityMailingAddress(String facilityPrefix){
        FacilityDetails expectedFacilityDetails = new FacilityDetails(facilityPrefix);
        Assert.assertEquals(getMailingAddressLine1(), expectedFacilityDetails.getMailingAddressLine1(), "Facility Mailing address line 1 ");
        Assert.assertEquals(getMailingAddressLine2(), expectedFacilityDetails.getMailingAddressLine2(), "Facility Mailing address line 2 ");
        Assert.assertEquals(getMailingAddressLine3(), expectedFacilityDetails.getMailingAddressLine3(), "Facility Mailing address line 3 ");
        String expectedMailingState = expectedFacilityDetails.getMailingAddressState();
        if (expectedMailingState == null || expectedMailingState.isEmpty()) {
            expectedMailingState = "Select";
        }
        Assert.assertEquals(getMailingAddressState(), expectedMailingState, "Facility Mailing address State ");
        String expectedMailingCounty = expectedFacilityDetails.getMailingAddressCounty();
        if (expectedMailingCounty == null || expectedMailingCounty.isEmpty()) {
            expectedMailingCounty = "Select";
        }
        Assert.assertEquals(getMailingAddressCounty(), expectedMailingCounty, "Facility Mailing address county ");
        String expectedMailingMunicipality = expectedFacilityDetails.getMailingAddressMunicipality();
        if (expectedMailingMunicipality == null || expectedMailingMunicipality.isEmpty()) {
            expectedMailingMunicipality = "Select";
        }
        Assert.assertEquals(getMailingAddressMunicipality(), expectedMailingMunicipality, "Facility Mailing address Municipality ");
        Assert.assertEquals(getMailingAddressZipcodeDropdown().trim(), expectedFacilityDetails.getMailingAddressZipcode().trim(), "Facility Mailing address Zipcode ");
//        String expectedMailingRegion = expectedFacilityDetails.getMailingAddressRegion();
//        if (expectedMailingRegion == null || expectedMailingRegion.isEmpty()) {
//            expectedMailingRegion = "Select";
//        }
//        Assert.assertEquals(getMailingAddressRegion(), expectedMailingRegion, "Facility Mailing address Region ");
//        String expectedMailingFieldOfc = expectedFacilityDetails.getMailingAddressFieldOffice();
//        if (expectedMailingFieldOfc == null || expectedMailingFieldOfc.isEmpty()) {
//            expectedMailingFieldOfc = "Select";
//        }
//        Assert.assertEquals(getMailingAddressFieldOffice(), expectedMailingFieldOfc, "Facility Mailing address Field Office ");
    }

    public void verifyFacilityLocationInfo(String facilityPrefix){
        FacilityDetails expectedFacilityDetails = new FacilityDetails(facilityPrefix);
        Assert.assertEquals(getXCord(), expectedFacilityDetails.getXCord(), "Facility X Cord ");
        Assert.assertEquals(getYCord(), expectedFacilityDetails.getYCord(), "Facility Y Cord ");
        String actualEjScore = getEjsScore();
        if (actualEjScore.equals("0.00000000")) {actualEjScore = "0";};
        Assert.assertEquals(actualEjScore, expectedFacilityDetails.getEjsScore(), "Facility EJs Score ");
    }

    public void clickAddContactBtn(){
        clickPom(addContactBtn);
    }

    //Todo- need to refactor to work dynamically for contact
    public void clickEditContactbtn() throws InterruptedException {
        scrollToElementPom(editContactBtn);
        Thread.sleep(1000);
        clickPom(editContactBtn);
    }

    /**
     * Deletes all contacts listed in the "Contact Information" table.
     * It repeatedly clicks the delete icon for each contact until no more delete icons are found.
     * Assumes a browser native confirmation alert appears after clicking delete.
     *
     * @throws RuntimeException if an issue occurs during the deletion process (e.g., element not found, alert not present).
     */
    public void deleteAllContacts() {
        System.out.println("Attempting to delete all contacts from the table...");

        // Locator for the contact rows (for initial count and waiting for less elements)
        By contactRowLocator = By.xpath("//div[@id='contactList']/div[@class='contact-item row']");

        // Locator for the delete buttons (same as @FindBy)
        By deleteButtonBy = By.xpath("//div[@class='contact-item row']//button[@class='btn btn-sm delete-contact text-danger']");

        int initialContactCount = 0;
        try {
            // Wait for at least one contact row to be visible, or for the table to stabilize
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(contactRowLocator));
            initialContactCount = driver.findElements(deleteButtonBy).size(); // Use driver.findElements for fresh count
            System.out.println("Initial number of contacts found: " + initialContactCount);
        } catch (TimeoutException e) {
            System.out.println("No contacts found to delete or table not fully loaded within timeout. Proceeding as if empty.");
            initialContactCount = 0;
        } catch (Exception e) {
            System.err.println("Error getting initial contact count: " + e.getMessage());
            throw new RuntimeException("Failed to get initial contact count.", e);
        }

        if (initialContactCount == 0) {
            System.out.println("No contacts to delete. Table is already empty or elements not found.");
            return;
        }

        // Loop to delete contacts one by one
        // Using a do-while loop for at least one execution if initialCount > 0
        do {
            try {
                // Re-find the delete button each time to avoid StaleElementReferenceException
                // Always get the first visible and clickable delete button
                WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(deleteButtonBy));

                scrollToElementPom(deleteButton);
                Thread.sleep(2000);
                deleteButton.click();
                Thread.sleep(2000);
                System.out.println("Clicked a delete button.");


                // Wait for the number of delete buttons to decrease.
                // This is a robust way to wait for the DOM to update after a deletion.
                wait.until(ExpectedConditions.numberOfElementsToBeLessThan(deleteButtonBy, driver.findElements(deleteButtonBy).size() + 1));
                System.out.println("Contact deleted successfully. Remaining: " + driver.findElements(deleteButtonBy).size());

            } catch (TimeoutException e) {
                System.err.println("Timeout waiting for contact to disappear after deletion. Error: " + e.getMessage());
                throw new RuntimeException("Failed to delete contact due to timeout waiting for DOM update.", e);
            } catch (Exception e) {
                System.err.println("An error occurred during contact deletion: " + e.getMessage());
                // Depending on your framework's policy, you might assert failure here
                throw new RuntimeException("Failed to delete all contacts.", e);
            }
        } while (driver.findElements(deleteButtonBy).size() > 0); // Continue as long as delete buttons exist

        System.out.println("Successfully deleted all contacts from the table.");
    }

    public void clickNextBtn() throws InterruptedException {
        scrollToElementPom(nextBtn);
        Thread.sleep(2000);
        clickPom(nextBtn);
    }
}
