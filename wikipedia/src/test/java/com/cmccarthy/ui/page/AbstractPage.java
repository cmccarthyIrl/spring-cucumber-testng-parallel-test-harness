package com.cmccarthy.ui.page;

import com.cmccarthy.ui.utils.DriverManager;
import com.cmccarthy.ui.utils.VisibilityOfElement;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractPage {

    private final Logger log = LoggerFactory.getLogger(AbstractPage.class);
    @Autowired
    private DriverManager driverManager;


    public AbstractPage(DriverManager driverManager) {
        PageFactory.initElements(driverManager.getDriver(), this);
    }

    protected void openAt(String url) {
        driverManager.getDriver().get(url);
    }

    public WebDriver getDriver() {
        return driverManager.getDriver();
    }

    protected boolean waitForElementPresent(WebElement element) throws NoSuchFieldException {
        waitForPageLoaded();
        getElementLocator(element);
        boolean result = true;
        try {
            driverManager.getDriverWait().until(new VisibilityOfElement(element));
        } catch (TimeoutException e) {
            result = false;
        } catch (Exception t) {
            throw new NoSuchFieldException(t.getLocalizedMessage());
        }
        return result;
    }

    protected boolean checkForElementPresent(WebElement element) {
        waitForPageLoaded();
        getElementLocator(element);
        boolean result = true;
        try {
            driverManager.getDriverWait().until(new VisibilityOfElement(element));
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    private void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
        try {
            Thread.sleep(800);
            driverManager.getDriverWait().until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }

    public void switchToFrame(WebElement element) {
        driverManager.getDriver().switchTo().frame(element);
    }

    public void switchToMainContent() {
        driverManager.getDriver().switchTo().parentFrame();
        driverManager.getDriver().switchTo().defaultContent();
    }

    private void getElementLocator(WebElement element) {
        try {
            log.debug("Waiting for element to be present by locator :" + element.toString().split(">")[1].replace("]", ""));
        } catch (IndexOutOfBoundsException ex) {
            log.debug("Waiting for element to be present by locator :" + element.toString().split("'")[1].replace("]", ""));
        }
    }
}
