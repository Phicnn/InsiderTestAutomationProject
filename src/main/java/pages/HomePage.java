package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {
    @FindBy(xpath = "//a[contains(text(),'Company')]")
    private WebElement companyMenu;

    @FindBy(xpath = "//a[contains(text(),'Careers')]")
    private WebElement careersLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToCareers() {
        setWait(companyMenu).click();
        setWait(careersLink).click();
    }

    public boolean isHomePageOpened() {
        return driver.getCurrentUrl().equals("https://useinsider.com/");
    }
}
