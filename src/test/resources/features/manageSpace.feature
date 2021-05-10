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

  Scenario: User can make space private
    Given the user has created an account with name "test4"
    And the user creates a space that is "shared"
    And another user has an account with name "other4"
    And the other user has access
    When the user sets the space "private"
    Then the return code is 202
    And the user still has access
    And the space is configured as "private"
    And the other user has no access

  Scenario: User can make space private
    Given the user has created an account with name "test5"
    And the user creates a space that is "private"
    And another user has an account with name "other5"
    And the other user has access
    When the user sets the space "shared"
    Then the return code is 202
    And the user still has access
    And the space is configured as "shared"
    And the other user has no access


  Scenario: Only creator can configure
    Given the user has created an account with name "test6"
    And another user has an account with name "other6"
    And the other user has access
    When the other user configures space
    Then the return code is 406
    And the user still has access
    And the other user still has access


  Scenario: Configuration needs access
    Given the user has created an account with name "test7"
    And another user has an account with name "other7"
    When the other user configures space
    Then the return code is 403
    And the user still has access
    And the other user has no access

  Scenario Outline: User can make space <shared_state> <username> <other_user>
    Given the user has created an account with name "<username>"
    And the user creates a space that is "<shared_state>"
    And another user has an account with name "<other_user>"
    And the other user has access
    When the user sets the space "<shared_state>"
    Then the return code is 202
    And the user still has access
    And the space is configured as "<shared_state>"
  Examples:
    | shared_state   | username | other_user |
    | private        | test8    | other8     |
    | shared         | test9    | other9     |

  Scenario:


