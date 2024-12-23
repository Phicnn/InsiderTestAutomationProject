package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.CareersPage;
import pages.JobsPage;
import utils.ConfigReader;
import utils.Driver;


public class InsiderTestAutomation {

    private WebDriver driver;

    @BeforeMethod
    @Parameters("browser")
    public void setUp(String browser) {
        this.driver = Driver.getDriver("");
    }

    @Test
    public void testInsiderCareerJourney() {
        // Use base URL from config
        driver.get(ConfigReader.getBaseUrl());
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageOpened(), "Home page is not opened");

        // Navigate to Careers page
        homePage.navigateToCareers();
        CareersPage careersPage = new CareersPage(driver);
        Assert.assertTrue(careersPage.areBlocksDisplayed(), "Career page blocks are not displayed");

        // Navigate to QA Jobs page
        careersPage.navigateToQAPage();
        JobsPage jobsPage = new JobsPage(driver);

        // Filter jobs using config parameters
        jobsPage.seeAllQAJops();
        jobsPage.filterJobs(
                ConfigReader.getJobLocation(),
                ConfigReader.getJobDepartment()
        );

        // Validate job listings
        Assert.assertTrue(jobsPage.validateJobListings(), "Job listings do not match criteria");

        // Check View Role button redirect
        Assert.assertTrue(jobsPage.clickViewRoleAndCheckRedirect(), "Redirect to Lever Application form failed");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            //driver.quit();
        }
    }
}
