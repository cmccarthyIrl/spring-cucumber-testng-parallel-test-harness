package com.cmccarthy.common.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApplicationProperties {

    @Value("${weather.url.value}")
    private String weatherAppUrl;
    @Value("${wikipedia.url.value}")
    private String wikipediaUrl;
    @Value("${browser}")
    private String browser;
    @Value("${gridUrl}")
    private String gridUrl;

    public void setWeatherAppUrl(String weatherAppUrl) {
        this.weatherAppUrl = weatherAppUrl;
    }

    public void setWikipediaUrl(String wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

}