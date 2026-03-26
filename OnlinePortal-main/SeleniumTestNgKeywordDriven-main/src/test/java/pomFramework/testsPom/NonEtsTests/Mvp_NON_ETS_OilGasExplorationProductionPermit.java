package pomFramework.testsPom.NonEtsTests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.BasePage;
import pomFramework.pagesPom.MvpSearchPermitsPage;
import pomFramework.testsPom.BaseTestPom;
import pomFramework.utilsPom.ConfigReader;

@Listeners(TestAllureListenerPom.class)
public class Mvp_NON_ETS_OilGasExplorationProductionPermit extends BaseTestPom {

    // Define constants for property keys for easy reference and to avoid typos
    private static final String SEARCH_PERMITS_URL = ConfigReader.getProperty("base.mvpSearchPermitsUrl");


    //Permit info
    private static final String PERMIT_TYPE = "Oil and Gas Exploration and Production Permit";

    //URL
    private static final String DOWNLOAD_FILENAME = "Application for Gas Exploration and Production.pdf";

    @Test(description = "Non ETS redirect url test for applying Oil and Gas Exploration and Production Permit")
    public void mvpNonETSOilGasExplorationProductionPermit() {
        MvpSearchPermitsPage mvpSearchPermitsPage = new MvpSearchPermitsPage();
        BasePage basePage = new BasePage();

        mvpSearchPermitsPage.navigateToPOM(SEARCH_PERMITS_URL);

        Assert.assertTrue(mvpSearchPermitsPage.isMvpSearchPermitPageHeaderDisplayed(), "Incorrect search permits page header");
        System.out.println("User is on Search permits page");

        // 1. Store the ID of the current window
        String originalWindow = DriverManagerPom.getDriverPom().getWindowHandle();

        //Search for Permit type
        mvpSearchPermitsPage.mvpSearch(PERMIT_TYPE);
        mvpSearchPermitsPage.clickMvpLearnMoreButtonForPermitType(PERMIT_TYPE);

        Assert.assertNotEquals(mvpSearchPermitsPage.downloadPermitFileGeneralMvp(DOWNLOAD_FILENAME), null, "Unable to download or wrong file was downloaded for " + PERMIT_TYPE);



    }
}