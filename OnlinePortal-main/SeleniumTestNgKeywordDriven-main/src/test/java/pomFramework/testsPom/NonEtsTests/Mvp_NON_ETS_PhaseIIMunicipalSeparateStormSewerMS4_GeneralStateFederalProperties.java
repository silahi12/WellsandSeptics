package pomFramework.testsPom.NonEtsTests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pomFramework.driverPom.DriverManagerPom;
import pomFramework.listeners.TestAllureListenerPom;
import pomFramework.pagesPom.BasePage;
import pomFramework.pagesPom.MvpSearchPermitsPage;
import pomFramework.testsPom.BaseTestPom;
import pomFramework.utilsPom.ConfigReader;

import java.time.Duration;

@Listeners(TestAllureListenerPom.class)
public class Mvp_NON_ETS_PhaseIIMunicipalSeparateStormSewerMS4_GeneralStateFederalProperties extends BaseTestPom {

    // Define constants for property keys for easy reference and to avoid typos
    private static final String SEARCH_PERMITS_URL = ConfigReader.getProperty("base.mvpSearchPermitsUrl");


    //Permit info
    private static final String PERMIT_TYPE = "Phase II Municipal Separate Storm Sewer (MS4) Permit (General, State and Federal Properties)";

    //URL
    private static final String REDIRECT_URL = "mde.maryland.gov/programs/water/StormwaterManagementProgram/Pages/NPDES_MS4_New.aspx";

    @Test(description = "Non ETS redirect url test for applying Phase II Municipal Separate Storm Sewer (MS4) Permit (General, State and Federal Properties)")
    public void mvpNonETSPhaseIIMunicipalSeparateStormSewerMS4_GeneralStateFederalProperties() {
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

        WebDriverWait wait = new WebDriverWait(DriverManagerPom.getDriverPom(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // 4. Switch to the new tab
        for (String windowHandle : DriverManagerPom.getDriverPom().getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                DriverManagerPom.getDriverPom().switchTo().window(windowHandle);
                break;
            }
        }
        basePage.waitForUrlContainsPom(REDIRECT_URL);

        //Assert User is on expected redirect page
        String currentUrl = DriverManagerPom.getDriverPom().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(REDIRECT_URL), "User not on expected redirect url- "+ REDIRECT_URL +". Current Url - " + currentUrl);


    }
}
