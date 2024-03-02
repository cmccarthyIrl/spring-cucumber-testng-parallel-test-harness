package com.cmccarthy.ui.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class DriverHelper {

//    @Bean
//    public DriverHelper driverHelper(){
//        return new DriverHelper();
//    }

  private final Logger logger = LoggerFactory.getLogger(DriverHelper.class);
  private DriverManager driverManager;
  @Autowired
  private DriverWait driverWait;

  /**
   * Send Keys to the specified element, clears the element first
   */
  public void sendKeys(WebElement element, String value) {
    if (value != null) {
      if (value.length() > 0) {
        clear(element);
        element.sendKeys(value);
      } else {
        clear(element);
      }
    }
  }

  /**
   * Clicks on an element by WebElement
   */
  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
  public void click(WebElement element) throws NoSuchFieldException {
    try {
      driverWait.waitForElementToLoad(element);
      element.click();
    } catch (StaleElementReferenceException e) {
      logger.warn("Could not click on the element");
      throw new RetryException("Could not click on the element : " + element);
    }
  }

  /**
   * Clicks on an element by Locator
   */
  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
  public void click(By locator) throws NoSuchFieldException {
    try {
      driverWait.waitForElementToLoad(locator);
      driverManager.getDriver().findElement(locator).click();
    } catch (StaleElementReferenceException e) {
      logger.warn("Could not click on the element");
      throw new RetryException("Could not click on the element");
    }
  }

  /**
   * Clicks on an element by Locator
   */
  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
  public void rightClick(By locator) throws NoSuchFieldException {
    driverWait.waitForElementToLoad(locator);
    final WebElement element = driverManager.getDriver().findElement(locator);
    try {
      final Actions builder = new Actions(driverManager.getDriver());
      builder.moveToElement(element).contextClick(element);
      builder.perform();
    } catch (Exception ser) {
      logger.warn("Could not click on the element : " + element);
      throw new RetryException("Could not click on the element : " + element);
    }
  }

  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
  public void scrollElementIntoView(WebElement element) {
    try {
      driverManager.getJSExecutor().executeScript("arguments[0].scrollIntoView(true);", element);
    } catch (Exception ignored) {
      logger.warn("Could not click on the element : " + element);
      throw new RetryException("Could not click on the element : " + element);
    }
  }

  /**
   * Clicks on an element by WebElement
   */
  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
  public void rightClick(WebElement element) throws NoSuchFieldException {
    driverWait.waitForElementToLoad(element);

    try {
      final Actions builder = new Actions(driverManager.getDriver());
      builder.moveToElement(element).contextClick(element);
      builder.perform();
    } catch (Exception ser) {
      logger.warn("Could not click on the element : " + element);
      throw new RetryException("Could not click on the element : " + element);
    }
  }

  /**
   * Clicks on an element using Actions
   */

  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
  public void clickAction(WebElement element) throws NoSuchFieldException {
    driverWait.waitForElementToLoad(element);
    try {
      final Actions builder = new Actions(driverManager.getDriver());
      builder.moveToElement(element).click(element);
      builder.perform();
    } catch (Exception ser) {
      logger.warn("Could not click on the element");
      throw new RetryException("Could not click on the element : " + element);
    }
  }

  /**
   * Clicks on an element using Actions
   */
  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
  public void clickAction(By locator) throws NoSuchFieldException {
    driverWait.waitForElementToLoad(locator);

    final WebElement element = driverManager.getDriver().findElement(locator);
    try {
      final Actions builder = new Actions(driverManager.getDriver());
      builder.moveToElement(element).click(element);
      builder.perform();
    } catch (Exception ser) {
      logger.warn("Could not click on the element");
      throw new RetryException("Could not click on the element : " + element);
    }
  }

  /**
   * Checks if the specified element is displayed
   */
  public boolean isElementDisplayed(WebElement element) {
    boolean present = false;
    try {
      present = element.isDisplayed();
    } catch (Exception ignored) {
    }
    return present;
  }

  /**
   * Clear text from a field
   */
  private void clear(WebElement element) {
    try {
      driverManager.getJSExecutor().executeScript("arguments[0].value='';", element);
    } catch (ElementNotInteractableException ignored) {
    }
  }

}
