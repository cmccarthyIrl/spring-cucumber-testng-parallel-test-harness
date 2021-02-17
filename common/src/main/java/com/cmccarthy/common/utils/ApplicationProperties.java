package com.cmccarthy.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    @Value("${weather.url.value}")
    private String weatherAppUrl;
    @Value("${wikipedia.url.value}")
    private String wikipediaUrl;
    @Value("${browser}")
    private String browser;

    public String getWeatherAppUrl() {
        return weatherAppUrl;
    }

    public void setWeatherAppUrl(String weatherAppUrl) {
        this.weatherAppUrl = weatherAppUrl;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

    public void setWikipediaUrl(String wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }
}