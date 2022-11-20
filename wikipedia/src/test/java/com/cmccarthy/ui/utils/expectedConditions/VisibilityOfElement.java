package com.cmccarthy.ui.utils.expectedConditions;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class VisibilityOfElement implements ExpectedCondition<Boolean> {

    private final WebElement element;

    public VisibilityOfElement(WebElement element) {
        this.element = element;
    }

    @Override
    public Boolean apply(WebDriver d) {
        try {
            return element.isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException | ElementNotVisibleException e) {
            return false;
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
}
