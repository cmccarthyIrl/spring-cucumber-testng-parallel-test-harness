Feature: Open Weather App - Random Tests

  Scenario: Verify the user can retrieve the weather for Sydney
    Given The user has requested the weather for Sydney
    Then The weather for Sydney should be returned

  Scenario: Verify the user can retrieve the weather for Dublin
    Given The user has requested the weather for Dublin
    Then The weather for Dublin should be returned