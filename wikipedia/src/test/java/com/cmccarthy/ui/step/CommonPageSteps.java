package com.cmccarthy.ui.step;

import static org.testng.AssertJUnit.assertTrue;

import com.cmccarthy.common.utils.LogManager;
import com.cmccarthy.ui.page.WikipediaCommonPage;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonPageSteps extends AbstractStep {
  @Autowired
  private LogManager logManager;
  @Autowired
  private WikipediaCommonPage wikipediaCommonPage;

  @Then("The user should be on the Common page")
  public void theUserShouldBeOnTheCommonPage() {
    logManager.info("The Wikipedia Common page should be opened");
    assertTrue("Wikipedia Common page should be opened",
        isElementDisplayed(wikipediaCommonPage.getCentralLogo()));
  }
}
