Feature: A space can be created
  Scenario: Private space can be created
    Given the user is logged in with name "shiggy"
    And the space should be private: "true"
    When the user wants to create a space
    Then the status code of create space is 201
    And the returned ID is legitimate
    And the space is private
    And the user has access


  Scenario: Shared space can be created
    Given the user is logged in with name "luigi"
    And the space should be private: "false"
    When the user wants to create a space
    Then the status code of create space is 201
    And the returned ID is legitimate
    And the space is shared
    And the user has access

  Scenario: Not authorized
    And the space should be private: "true"
    When the user wants to create a space
    Then the status code of create space is 401