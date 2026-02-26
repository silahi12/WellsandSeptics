package pomFramework.pagesPom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pomFramework.dataPom.ContactDetails;

import java.util.List;

public class ContactInformationForm extends BasePage{

    // WebElements on the ContactInformationForm, identified using @FindBy annotations.
    @FindBy(xpath = "//div[@class='modal fade show' and @id='contactWindow']")
    private WebElement addContactWindow;

    @FindBy(id="ContactFirstName")
    private WebElement firstnameField;

    @FindBy(id="ContactLastName")
    private WebElement lastnameField;

    @FindBy(id="ContactEmail")
    private WebElement emailField;

    @FindBy(id="ContactIsOwner")
    private WebElement ownerCheckbox;

    @FindBy(id="ContactIsOperator")
    private WebElement operatorCheckbox;

    @FindBy(id="ContactIsResponsible")
    private WebElement responsiblePartyCheckbox;

    @FindBy(id="ContactPhone")
    private WebElement phoneNumberField;

    @FindBy(id = "ContactPhysicalLine1")
    private WebElement physicalAddressLine1;

    @FindBy(id = "ContactPhysicalLine2")
    private WebElement physicalAddressLine2;

    @FindBy(id = "ContactPhysicalLine3")
    private WebElement physicalAddressLine3;

    @FindBy(xpath = "//select[@id='ContactPhysicalMunicipality']")
    private WebElement physicalAddressMunicipalityDropdown;

    @FindBy(xpath = "//select[@id='ContactPhysicalCounty']")
    private WebElement physicalAddressCountyDropdown;

    @FindBy(xpath = "//select[@id='ContactPhysicalState']")
    private WebElement physicalAddressStateDropdown;

    @FindBy(id = "ContactPhysicalZip")
    private WebElement physicalAddressZipcode;

    @FindBy(xpath = "//select[@id='ContactPhysicalRegion']")
    private WebElement physicalAddressRegionDropdown;

    @FindBy(xpath = "//select[@id='ContactPhysicalFieldOffice']")
    private WebElement physicalAddressFieldOfcDropdown;

    @FindBy(id = "ContactMailingLine1")
    private WebElement mailingAddressLine1;

    @FindBy(id = "ContactMailingLine2")
    private WebElement mailingAddressLine2;

    @FindBy(id = "ContactMailingLine3")
    private WebElement mailingAddressLine3;

    @FindBy(xpath = "//select[@id='ContactMailingMunicipality']")
    private WebElement mailingAddressMunicipalityDropdown;

    @FindBy(xpath = "//select[@id='ContactMailingCounty']")
    private WebElement mailingAddressCountyDropdown;

    @FindBy(xpath = "//select[@id='ContactMailingState']")
    private WebElement mailingAddressStateDropdown;

    @FindBy(id = "ContactMailingZip")
    private WebElement mailingAddressZipcode;

    @FindBy(xpath = "//select[@id='ContactMailingRegion']")
    private WebElement mailingAddressRegionDropdown;

    @FindBy(xpath = "//select[@id='ContactMailingFieldOffice']")
    private WebElement mailingAddressFieldOfcDropdown;

    @FindBy(id = "btnSaveContact")
    private WebElement saveBtn;

    public ContactInformationForm() {
        super(); // Call the constructor of BasePage to initialize driver and PageFactory
    }

    /** ---functions-- **/

    public boolean isContactInfoFormDisplayed(){
        return isElementDisplayedPom(addContactWindow);
    }

    public void addContact(String prefix) throws InterruptedException {
        ContactDetails newContactDetails = new ContactDetails(prefix);
        //ToDo- Enable below steps when Contacts work fine
//        clearAndSendKeys(firstnameField,newContactDetails.getContactFirstName());
//        clearAndSendKeys(lastnameField,newContactDetails.getContactLastName());
        clearAndSendKeys(emailField,newContactDetails.getContactEmail());
        String[] relationships = newContactDetails.getContactUserRelationship().split(",");
        for (String relationship : relationships) {
            if(relationship.toLowerCase().trim().equals("owner")){
                setCheckboxState(ownerCheckbox, true);
            }else if(relationship.toLowerCase().trim().equals( "operator")){
                setCheckboxState(operatorCheckbox, true);
            } else {setCheckboxState(responsiblePartyCheckbox, true);}
        }
        clearAndSendKeys(phoneNumberField, newContactDetails.getContactPhoneNumber());
        //fill out physical address
        clearAndSendKeys(physicalAddressLine1, newContactDetails.getPhysicalAddressLine1());
        clearAndSendKeys(physicalAddressLine2, newContactDetails.getPhysicalAddressLine2());
        clearAndSendKeys(physicalAddressLine3, newContactDetails.getPhysicalAddressLine3());
        selectDropdownOptionByVisibleTextPom(physicalAddressMunicipalityDropdown, newContactDetails.getPhysicalAddressMunicipality());
        selectDropdownOptionByVisibleTextPom(physicalAddressCountyDropdown, newContactDetails.getPhysicalAddressCounty());
        selectDropdownOptionByVisibleTextPom(physicalAddressStateDropdown,newContactDetails.getPhysicalAddressState());
        clearAndSendKeys(physicalAddressZipcode, newContactDetails.getPhysicalAddressZipcode());
        selectDropdownOptionByVisibleTextPom(physicalAddressRegionDropdown,newContactDetails.getPhysicalAddressRegion());
        selectDropdownOptionByVisibleTextPom(physicalAddressFieldOfcDropdown,newContactDetails.getPhysicalAddressFieldOffice());

        //fill out mailing address
        clearAndSendKeys(mailingAddressLine1, newContactDetails.getMailingAddressLine1());
        clearAndSendKeys(mailingAddressLine2, newContactDetails.getMailingAddressLine2());
        clearAndSendKeys(mailingAddressLine3, newContactDetails.getMailingAddressLine3());
        selectDropdownOptionByVisibleTextPom(mailingAddressMunicipalityDropdown, newContactDetails.getMailingAddressMunicipality());
        selectDropdownOptionByVisibleTextPom(mailingAddressCountyDropdown, newContactDetails.getMailingAddressCounty());
        selectDropdownOptionByVisibleTextPom(mailingAddressStateDropdown,newContactDetails.getMailingAddressState());
        clearAndSendKeys(mailingAddressZipcode, newContactDetails.getMailingAddressZipcode());
        selectDropdownOptionByVisibleTextPom(mailingAddressRegionDropdown,newContactDetails.getMailingAddressRegion());
        selectDropdownOptionByVisibleTextPom(mailingAddressFieldOfcDropdown,newContactDetails.getMailingAddressFieldOffice());

        scrollToElementPom(saveBtn);
        Thread.sleep(2000);
        clickPom(saveBtn);
        Thread.sleep(2000);
        waitForElementToDisappear(addContactWindow);
    }
}
