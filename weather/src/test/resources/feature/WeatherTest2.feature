@OpenWeather
Feature: Open Weather App - Random Tests 2

  Scenario: Verify the user can retrieve the weather from Open Weather
    Given The user has requested the weather for Dublin
    Then The weather for Dublin should be returned
