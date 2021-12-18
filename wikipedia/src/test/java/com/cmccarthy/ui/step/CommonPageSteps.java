package com.cmccarthy.ui.step;

import com.cmccarthy.ui.page.WikipediaCommonPage;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class CommonPageSteps extends AbstractStep {

    private final Logger logger = LoggerFactory.getLogger(CommonPageSteps.class);
    @Autowired
    private WikipediaCommonPage wikipediaCommonPage;

    @Then("The user should be on the Common page")
    public void theUserShouldBeOnTheCommonPage() {
        logger.info("The Wikipedia Common page should be opened");
        assertTrue("Wikipedia Common page should be opened",
                isElementDisplayed(wikipediaCommonPage.getCentralLogo()));
    }
}
