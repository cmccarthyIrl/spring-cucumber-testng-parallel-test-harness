package com.cmccarthy.api.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {WeatherContextConfiguration.class})
@SpringBootTest
public class WeatherAbstractTestDefinition {
}