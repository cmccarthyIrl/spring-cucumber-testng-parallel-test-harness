package com.cmccarthy.ui.step;

import com.cmccarthy.common.utils.ApplicationProperties;
import com.cmccarthy.ui.page.WikipediaHomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class WikipediaPageSteps {

    private final Logger log = LoggerFactory.getLogger(WikipediaPageSteps.class);

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private WikipediaHomePage wikipediaHomePage;

    @Given("^The user opened the Wikipedia Homepage$")
    public void userIsOpenMainPage() throws NoSuchFieldException {
        wikipediaHomePage.open(applicationProperties.getWikipediaUrl());
        log.info("The user navigated to the Wikipedia Homepage : " + applicationProperties.getWikipediaUrl());
        assertTrue("Wikipedia Homepage should be opened", wikipediaHomePage.isPageOpened());
    }

    @And("^The user clicked on the Common link$")
    public void theUserClickedOnTheCommonLink() {
        wikipediaHomePage.getCommonPage();
        log.info("The user clicked the Common link on the Homepage");
    }
}
