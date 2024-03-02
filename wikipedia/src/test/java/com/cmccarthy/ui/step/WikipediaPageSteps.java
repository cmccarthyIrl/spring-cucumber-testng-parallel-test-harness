package com.cmccarthy.ui.step;

import static org.testng.AssertJUnit.assertTrue;

import com.cmccarthy.common.utils.ApplicationProperties;
import com.cmccarthy.common.utils.LogManager;
import com.cmccarthy.ui.page.WikipediaHomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

public class WikipediaPageSteps extends AbstractStep {
  @Autowired
  private LogManager logManager;
  private final ApplicationProperties applicationProperties;
  private final WikipediaHomePage wikipediaHomePage;

  public WikipediaPageSteps(ApplicationProperties applicationProperties,
                            WikipediaHomePage wikipediaHomePage) {
    this.applicationProperties = applicationProperties;
    this.wikipediaHomePage = wikipediaHomePage;
  }

  @Given("The user opened the Wikipedia Homepage")
  public void userIsOpenMainPage() throws NoSuchFieldException {
    wikipediaHomePage.open(applicationProperties.getWikipediaUrl());
    logManager.info("The user navigated to the Wikipedia Homepage : " + applicationProperties
        .getWikipediaUrl());
    assertTrue("Wikipedia Homepage should be opened",
        isElementDisplayed(wikipediaHomePage.getCommonPage()));
  }

  @And("The user clicked on the Common link")
  public void theUserClickedOnTheCommonLink() throws NoSuchFieldException {
    click(wikipediaHomePage.getCommonPage());
    logManager.info("The user clicked the Common link on the Homepage");
  }
}
