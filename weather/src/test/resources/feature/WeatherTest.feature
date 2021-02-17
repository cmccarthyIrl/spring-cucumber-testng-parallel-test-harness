@OpenWeather
Feature: Open Weather App - Random Tests

  Scenario Outline: Verify the user can retrieve the weather from Open Weather
    Given The user has requested the weather for <location>
    Then The weather for <location> should be returned

    Examples:
      | location |
      | Dublin   |
      | Paris    |
      | Sydney   |