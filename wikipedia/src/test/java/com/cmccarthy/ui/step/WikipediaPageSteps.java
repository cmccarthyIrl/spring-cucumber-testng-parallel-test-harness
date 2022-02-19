package com.cmccarthy.ui.step;

import com.cmccarthy.common.utils.ApplicationProperties;
import com.cmccarthy.ui.page.WikipediaHomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class WikipediaPageSteps extends AbstractStep {

    private final Logger logger = LoggerFactory.getLogger(WikipediaPageSteps.class);


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
        logger.info("The user navigated to the Wikipedia Homepage : " + applicationProperties
                .getWikipediaUrl());
        assertTrue("Wikipedia Homepage should be opened",
                isElementDisplayed(wikipediaHomePage.getCommonPage()));
    }

    @And("The user clicked on the Common link")
    public void theUserClickedOnTheCommonLink() throws NoSuchFieldException {
        click(wikipediaHomePage.getCommonPage());
        logger.info("The user clicked the Common link on the Homepage");
    }
}
