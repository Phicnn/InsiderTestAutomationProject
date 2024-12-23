package pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
        this.actions = new Actions(driver);
    }

    protected void takeScreenshot(String fileName) {
        try {
            File screenshotDir = new File(ConfigReader.getScreenshotDirectory());
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(screenshotDir, fileName + ".png"));
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }
    protected WebElement setWait(By key)
    {
         return wait.until(ExpectedConditions.elementToBeClickable(key));
    }
    protected WebElement setWait(WebElement key)
    {
        return wait.until(ExpectedConditions.elementToBeClickable(key));
    }
    protected void setTimer(int timer)
    {
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
