package com.cmccarthy.ui.page;

import com.cmccarthy.ui.annotations.PageObject;
import com.cmccarthy.ui.utils.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageObject
public class WikipediaHomePage extends AbstractPage {

  @FindBy(how = How.XPATH, using = "//a[contains(@href,'commons.wikimedia.org')]")
  private WebElement COMMONS_LOGO;

  @FindBy(how = How.CLASS_NAME, using = "central-featured-logo")
  private WebElement CENTRAL_LOGO;

  public WikipediaHomePage(DriverManager driverManager) {
    super(driverManager);
  }

  public void open(String url) {
    openAt(url);
  }

  public WebElement getCommonPage() {
    return COMMONS_LOGO;
  }
}

