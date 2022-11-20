package com.cmccarthy.ui.utils.expectedConditions;

import com.cmccarthy.common.utils.Constants;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

public class ClickabilityOfElementByLocator implements ExpectedCondition<WebElement> {

    private final By locator;

    public ClickabilityOfElementByLocator(By locator) {
        this.locator = locator;
    }

    @Override
    public WebElement apply(WebDriver webDriver) {

        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(Constants.timeoutShort))
                .pollingEvery(Duration.ofMillis(Constants.pollingShort))
                .ignoring(java.util.NoSuchElementException.class,
                        StaleElementReferenceException.class);

        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (StaleElementReferenceException | NoSuchElementException | ElementNotVisibleException e) {
            return webDriver.findElement(locator);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

}
