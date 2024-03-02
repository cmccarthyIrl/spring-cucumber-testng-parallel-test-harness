package com.cmccarthy.ui.utils.expectedConditions;

import com.cmccarthy.common.utils.Constants;
import java.time.Duration;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class ClickabilityOfElement implements ExpectedCondition<WebElement> {

  private final WebElement element;

  public ClickabilityOfElement(WebElement element) {
    this.element = element;
  }

  @Override
  public WebElement apply(WebDriver webDriver) {

    final Wait<WebDriver> wait = new FluentWait<>(webDriver)
        .withTimeout(Duration.ofSeconds(Constants.timeoutShort))
        .pollingEvery(Duration.ofMillis(Constants.pollingShort))
        .ignoring(java.util.NoSuchElementException.class,
            StaleElementReferenceException.class);
    try {
      return wait.until(ExpectedConditions.elementToBeClickable(element));
    } catch (StaleElementReferenceException | NoSuchElementException exception) {
      return element;
    } catch (Throwable t) {
      throw new Error(t);
    }
  }
}
