Feature: A space can be deleted
  Scenario: Successful deletion
    Given the user is logged in properly
    And the user created the space
    When the user wants to delete the space
    Then the response is 200
    And the user has no access
    And there is no file in that space
    And all pending uploads are deleted
    And the refFile was deleted
    And the space was deleted


  Scenario: Space does not exist
    Given the user is logged in properly
    When the user wants to delete the space
    Then the response is 403


  Scenario: User has no access
    Given the user is logged in properly
    And another user created the space
    When the user wants to delete the space
    Then the response is 403


  Scenario: User is not the creator
    Given the user is logged in properly
    And another user created the space
    And the user has access to the space
    When the user wants to delete the space
    Then the response is 403

  Scenario: User is not properly logged in
    Given another user created the space
    When the user wants to delete the space
    Then the response is 401