package pomFramework.dataPom;

import pomFramework.utilsPom.ConfigReader;

public class FacilityDetails {

    private String facilityName;
    private String facilityType;
    private String facilityStartDate;

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

    private String xCord;
    private String yCord;
    private String ejsScore;


    /**
     * Constructor that populates facility details by reading from config.properties
     * using a specified prefix (e.g., "standard.facility").
     *
     * @param prefix The prefix for the facility properties in config.properties (e.g., "standard.facility").
     */
    public FacilityDetails(String prefix) {
        this.facilityName = ConfigReader.getProperty(prefix + ".name");
        this.facilityType = ConfigReader.getProperty(prefix + ".type");
        this.facilityStartDate = ConfigReader.getProperty(prefix + ".start_date");

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

        this.xCord = ConfigReader.getProperty(prefix + ".x_cord");
        this.yCord = ConfigReader.getProperty(prefix + ".y_cord");
        this.ejsScore = ConfigReader.getProperty(prefix + ".ej_score");
    }

    // --- Getters for all fields ---
    public String getFacilityName() {
        return facilityName;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public String getFacilityStartDate() {
        return facilityStartDate;
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
        return physicalAddressMunicipality;
    }

    public String getPhysicalAddressCounty() { // Check if the string is null OR if it's an empty string.
        return physicalAddressCounty;
    }

    public String getPhysicalAddressState() { // Check if the string is null OR if it's an empty string.
        return physicalAddressState;
    }

    public String getPhysicalAddressZipcode() {
        return physicalAddressZipcode;
    }

    public String getPhysicalAddressRegion() {// Check if the string is null OR if it's an empty string.
        return physicalAddressRegion;
    }

    public String getPhysicalAddressFieldOffice() { // Check if the string is null OR if it's an empty string.
        return physicalAddressFieldOffice;
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
        return mailingAddressMunicipality;
    }

    public String getMailingAddressCounty() {
        return mailingAddressCounty;
    }

    public String getMailingAddressState() {
        return mailingAddressState;
    }

    public String getMailingAddressZipcode() {
        return mailingAddressZipcode;
    }

    public String getMailingAddressRegion() {
        return mailingAddressRegion;
    }

    public String getMailingAddressFieldOffice() {
        return mailingAddressFieldOffice;
    }

    public String getXCord() {
        return xCord;
    }

    public String getYCord() {
        return yCord;
    }

    public String getEjsScore() {
        return ejsScore;
    }

}
