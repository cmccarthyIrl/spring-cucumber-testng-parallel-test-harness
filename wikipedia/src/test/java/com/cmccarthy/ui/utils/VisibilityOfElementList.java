package com.cmccarthy.ui.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;

public class VisibilityOfElementList implements ExpectedCondition<Boolean> {

    private final By locator;

    public VisibilityOfElementList(By locator) {
        this.locator = locator;
    }

    @Override
    public Boolean apply(WebDriver driver) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            if (elements.isEmpty()) {
                return false;
            }
            return elements.get(0).isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException | ElementNotVisibleException | NullPointerException e) {
            return false;
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
}