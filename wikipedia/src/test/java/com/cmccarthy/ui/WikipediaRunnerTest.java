package com.cmccarthy.ui;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.springframework.stereotype.Component;

@CucumberOptions(
        features = {
                "src/test/resources/feature"
        },
        plugin = {
                "pretty",
                "json:target/cucumber/report.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        })
@Component
@Deprecated
public class WikipediaRunnerTest extends AbstractTestNGCucumberTests {

    //use the parallel class runner instead - more efficient
    //if you want to run your tests sequentially implement in the hooks class : Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
}


