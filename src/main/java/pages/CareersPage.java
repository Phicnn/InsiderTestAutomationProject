package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.ConfigReader;

public class CareersPage extends BasePage {
    @FindBy(xpath = "//h3[contains(text(),'Our Locations')]")
    private WebElement locationsBlock;

    @FindBy(xpath = "//div[contains(@class,'career-load-more')]/following-sibling::a[contains(text(),'See all teams')]")
    private WebElement teamsBlock;

    @FindBy(xpath = "//*[contains(text(),'Life at Insider')]")
    private WebElement lifeAtInsiderBlock;


    public CareersPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToQAPage() {
        driver.navigate().to(ConfigReader.getCarrierQuality());
    }

    public boolean areBlocksDisplayed() {
        try {

            return locationsBlock.isDisplayed() &&
                    teamsBlock.isDisplayed() &&
                    lifeAtInsiderBlock.isDisplayed();
        } catch (Exception e) {
            takeScreenshot("blocks_not_displayed");
            return false;
        }
    }
}
