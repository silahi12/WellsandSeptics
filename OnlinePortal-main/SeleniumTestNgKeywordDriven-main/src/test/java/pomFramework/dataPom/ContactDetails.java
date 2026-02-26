package pomFramework.dataPom;

import pomFramework.utilsPom.ConfigReader;

public class ContactDetails {

    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
    private String contactUserRelationship;
    private String contactPhoneNumber;

    private String physicalAddressLine1;
    private String physicalAddressLine2;
    private String physicalAddressLine3;
    private String physicalAddressMunicipality;
    private String physicalAddressCounty;
    private String physicalAddressState;
    private String physicalAddressZipcode;
    private String physicalAddressRegion;
    private String physicalAddressFieldOffice;

    private String mailingAddressLine1;
    private String mailingAddressLine2;
    private String mailingAddressLine3;
    private String mailingAddressMunicipality;
    private String mailingAddressCounty;
    private String mailingAddressState;
    private String mailingAddressZipcode;
    private String mailingAddressRegion;
    private String mailingAddressFieldOffice;



    /**
     * Constructor that populates facility details by reading from config.properties
     * using a specified prefix (e.g., "standard.facility").
     *
     * @param prefix The prefix for the facility properties in config.properties (e.g., "standard.facility").
     */
    public ContactDetails(String prefix) {
        this.contactFirstName = ConfigReader.getProperty(prefix + ".first_name");
        this.contactLastName = ConfigReader.getProperty(prefix + ".last_name");
        this.contactEmail = ConfigReader.getProperty(prefix + ".email");
        this.contactUserRelationship = ConfigReader.getProperty(prefix + ".user_relationship");
        this.contactPhoneNumber = ConfigReader.getProperty(prefix + ".phone_number");

        this.physicalAddressLine1 = ConfigReader.getProperty(prefix + ".physical.address.line1");
        this.physicalAddressLine2 = ConfigReader.getProperty(prefix + ".physical.address.line2");
        this.physicalAddressLine3 = ConfigReader.getProperty(prefix + ".physical.address.line3");
        this.physicalAddressMunicipality = ConfigReader.getProperty(prefix + ".physical.address.municipality");
        this.physicalAddressCounty = ConfigReader.getProperty(prefix + ".physical.address.county");
        this.physicalAddressState = ConfigReader.getProperty(prefix + ".physical.address.state");
        this.physicalAddressZipcode = ConfigReader.getProperty(prefix + ".physical.address.zipcode");
        this.physicalAddressRegion = ConfigReader.getProperty(prefix + ".physical.address.region");
        this.physicalAddressFieldOffice = ConfigReader.getProperty(prefix + ".physical.address.field_office");

        this.mailingAddressLine1 = ConfigReader.getProperty(prefix + ".mailing.address.line1");
        this.mailingAddressLine2 = ConfigReader.getProperty(prefix + ".mailing.address.line2");
        this.mailingAddressLine3 = ConfigReader.getProperty(prefix + ".mailing.address.line3");
        this.mailingAddressMunicipality = ConfigReader.getProperty(prefix + ".mailing.address.municipality");
        this.mailingAddressCounty = ConfigReader.getProperty(prefix + ".mailing.address.county");
        this.mailingAddressState = ConfigReader.getProperty(prefix + ".mailing.address.state");
        this.mailingAddressZipcode = ConfigReader.getProperty(prefix + ".mailing.address.zipcode");
        this.mailingAddressRegion = ConfigReader.getProperty(prefix + ".mailing.address.region");
        this.mailingAddressFieldOffice = ConfigReader.getProperty(prefix + ".mailing.address.field_office");

    }

    // --- Getters for all fields ---
    public String getContactFirstName() {
        return contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public String getContactUserRelationship() {
        return contactUserRelationship;
    }

    public String getPhysicalAddressLine1() {
        return physicalAddressLine1;
    }

    public String getPhysicalAddressLine2() {
        return physicalAddressLine2;
    }

    public String getPhysicalAddressLine3() {
        return physicalAddressLine3;
    }

    public String getPhysicalAddressMunicipality() { // Check if the string is null OR if it's an empty string.
        if (physicalAddressMunicipality == null || physicalAddressMunicipality.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return physicalAddressMunicipality; // Otherwise, return the actual municipality value
        }
    }

    public String getPhysicalAddressCounty() { // Check if the string is null OR if it's an empty string.
        if (physicalAddressCounty == null || physicalAddressCounty.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return physicalAddressCounty; // Otherwise, return the actual municipality value
        }
    }

    public String getPhysicalAddressState() { // Check if the string is null OR if it's an empty string.
        if (physicalAddressState == null || physicalAddressState.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return physicalAddressState; // Otherwise, return the actual municipality value
        }
    }

    public String getPhysicalAddressZipcode() {
        return physicalAddressZipcode;
    }

    public String getPhysicalAddressRegion() {// Check if the string is null OR if it's an empty string.
        if (physicalAddressRegion == null || physicalAddressRegion.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return physicalAddressRegion; // Otherwise, return the actual municipality value
        }
    }

    public String getPhysicalAddressFieldOffice() { // Check if the string is null OR if it's an empty string.
        if (physicalAddressFieldOffice == null || physicalAddressFieldOffice.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return physicalAddressFieldOffice; // Otherwise, return the actual municipality value
        }
    }

    public String getMailingAddressLine1() {
        return mailingAddressLine1;
    }

    public String getMailingAddressLine2() {
        return mailingAddressLine2;
    }

    public String getMailingAddressLine3() {
        return mailingAddressLine3;
    }

    public String getMailingAddressMunicipality() {
        // Check if the string is null OR if it's an empty string.
        if (mailingAddressMunicipality == null || mailingAddressMunicipality.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return mailingAddressMunicipality; // Otherwise, return the actual municipality value
        }
    }

    public String getMailingAddressCounty() {
        // Check if the string is null OR if it's an empty string.
        if (mailingAddressCounty == null || mailingAddressCounty.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return mailingAddressCounty; // Otherwise, return the actual municipality value
        }
    }

    public String getMailingAddressState() {
        // Check if the string is null OR if it's an empty string.
        if (mailingAddressState == null || mailingAddressState.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return mailingAddressState; // Otherwise, return the actual municipality value
        }
    }

    public String getMailingAddressZipcode() {
        return mailingAddressZipcode;
    }

    public String getMailingAddressRegion() {
        // Check if the string is null OR if it's an empty string.
        if (mailingAddressRegion == null || mailingAddressRegion.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return mailingAddressRegion; // Otherwise, return the actual municipality value
        }
    }

    public String getMailingAddressFieldOffice() {
        // Check if the string is null OR if it's an empty string.
        if (mailingAddressFieldOffice == null || mailingAddressFieldOffice.isEmpty()) {
            return "Select"; // Return "Select" if the municipality is empty or null
        } else {
            return mailingAddressFieldOffice; // Otherwise, return the actual municipality value
        }
    }



}
