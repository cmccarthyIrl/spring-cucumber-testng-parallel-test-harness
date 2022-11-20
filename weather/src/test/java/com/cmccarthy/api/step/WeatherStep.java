package com.cmccarthy.api.step;

import com.cmccarthy.api.config.WeatherAbstractTestDefinition;
import com.cmccarthy.api.model.response.LocationWeatherRootResponse;
import com.cmccarthy.api.service.WeatherService;
import com.cmccarthy.common.service.StepDefinitionDataManager;
import com.cmccarthy.common.utils.LogManager;
import com.google.gson.Gson;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

public class WeatherStep extends WeatherAbstractTestDefinition {
    @Autowired
    private LogManager logManager;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private StepDefinitionDataManager stepDefinitionDataManager;

    @Then("^The weather for (.*) should be returned$")
    public void theWeatherForDublinShouldBeReturned(String location) {
        final SoftAssertions softAssertions = new SoftAssertions();
        final LocationWeatherRootResponse locationWeatherRootResponse = new Gson().fromJson(((Response) stepDefinitionDataManager.getStoredObjectMap().get("class")).getBody().asString(), LocationWeatherRootResponse.class);
        logManager.info("Verifying the Response location : " + locationWeatherRootResponse.getName() + ", is equal to the expected location : " + location);
        softAssertions.assertThat(locationWeatherRootResponse.getName()).as("Expected the weather forecast to be for : " + location).withFailMessage("But it was for : " + locationWeatherRootResponse.getName()).isEqualToIgnoringCase(location);
        softAssertions.assertAll();
    }

    @Given("^The user has requested the weather for (.*)$")
    public void theUserHasRequestedTheWeatherForDublin(String location) {
        logManager.info("The user makes an request for the weather in : " + location);
        weatherService.getWeatherForLocation(location);
    }
}