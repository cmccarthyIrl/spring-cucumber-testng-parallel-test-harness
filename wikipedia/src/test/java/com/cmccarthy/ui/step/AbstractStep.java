package com.cmccarthy.ui.step;

import com.cmccarthy.ui.utils.DriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractStep {

  @Autowired
  private DriverHelper driverHelper;

  public void clickAction(WebElement element) throws NoSuchFieldException {
    driverHelper.clickAction(element);
  }

  public boolean isElementDisplayed(WebElement element) {
    return driverHelper.isElementDisplayed(element);
  }

  protected void click(WebElement element) throws NoSuchFieldException {
    driverHelper.click(element);
  }

  protected void click(By locator) throws NoSuchFieldException {
    driverHelper.click(locator);
  }

  protected void clickAction(By locator) throws NoSuchFieldException {
    driverHelper.clickAction(locator);
  }

  protected void sendKeys(WebElement element, String value) {
    driverHelper.sendKeys(element, value);
  }
}
