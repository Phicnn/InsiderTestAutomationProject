package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class JobsPage extends BasePage{
    By seeALLjops = By.xpath("//*[contains(text(),'See all QA jobs')]");

    @FindBy(xpath = "//span[contains(@id,'location')]/following-sibling::span[@class=\"select2-selection__arrow\"]")
    private WebElement locationFilter;

    String[] getFilterValue = {"//li[contains(@id,'","')]"};

    @FindBy(xpath = "//span[contains(@id,'department')]/following-sibling::span[@class=\"select2-selection__arrow\"]")
    private WebElement departmentFilter;

    @FindBy(xpath = "//a[normalize-space()='View Role']")
    private List<WebElement> viewRoleButtons;

    public JobsPage(WebDriver driver) {
        super(driver);
    }

    public void seeAllQAJops()
    {
        setWait(seeALLjops).click();
    }

    public void filterJobs(String location, String department) {
        setTimer(4000);
        setWait(locationFilter).click();
        setWait(By.xpath(getFilterValue[0]+location+getFilterValue[1])).click();

        setWait(departmentFilter).click();
        actions.moveToElement(driver.findElement(By.xpath(getFilterValue[0]+department+getFilterValue[1]))).click().build().perform();
        //setWait(By.xpath(getFilterValue[0]+department+getFilterValue[1])).click();
    }

    public boolean validateJobListings() {
        try {
            List<WebElement> jobRows = driver.findElements(By.cssSelector(".jobs-list-item"));

            boolean validPositions = jobRows.stream()
                    .allMatch(row -> {
                        String position = row.findElement(By.cssSelector(".position")).getText();
                        String department = row.findElement(By.cssSelector(".department")).getText();
                        String location = row.findElement(By.cssSelector(".location")).getText();

                        return position.contains("Quality Assurance") &&
                                department.contains("Quality Assurance") &&
                                location.contains("Istanbul, Turkey");
                    });

            return validPositions;
        } catch (Exception e) {
            takeScreenshot("job_validation_failed");
            return false;
        }
    }

    public boolean clickViewRoleAndCheckRedirect() {
        try {
            String currentUrl = driver.getCurrentUrl();
            actions.moveToElement(driver.findElement(By.xpath("//div[@class=\"jobs-pagination\"]"))).perform();
            //actions.moveToElement(viewRoleButtons.get(0)).perform();
            viewRoleButtons.get(0).click();

            //wait.until(ExpectedConditions.urlContains("lever.co"));
            setTimer(6000);
            ArrayList<String> switchTabs= new ArrayList<> (driver.getWindowHandles());
            driver.switchTo().window(switchTabs.get(1));
            //String currentUrlnew = driver.getCurrentUrl();
            //return !currentUrlnew.equals(currentUrl);
            return setWait(By.xpath("(//a[contains(text(),'Apply for this job')])[1]")).isDisplayed();
        } catch (Exception e) {
            takeScreenshot("view_role_redirect_failed");
            return false;
        }
    }
}
