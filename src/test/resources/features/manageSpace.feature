Feature: Space can be managed
  Scenario: User can kick other users
    Given the user has created an account with name "test1"
    And another user has an account with name "other1"
    And the other user has access
    When the user kicks all users
    Then the return code is 200
    And the other user has no access
    And the user still has access

  Scenario: Only creator can kick users
    Given the user has created an account with name "test2"
    And another user has an account with name "other2"
    And the other user has access
    When the other user kicks all users
    Then the return code is 406
    And the user still has access
    And the other user still has access

  Scenario: Users without access cannot kick
    Given the user has created an account with name "test3"
    And another user has an account with name "other3"
    When the other user kicks all users
    Then the return code is 403
    And the user still has access

  Scenario:


