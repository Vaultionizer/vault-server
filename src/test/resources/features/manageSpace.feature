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
      | shared_state | username | other_user |
      | private      | test8    | other8     |
      | shared       | test9    | other9     |


  Scenario: User can get config
    Given the user has created an account with name "test10"
    When the user queries the config
    Then the return code is 200
    And the config is correct.

  Scenario: User can only get config with access
    Given the user has created an account with name "test11"
    And another user has an account with name "other11"
    When the other user queries the config
    Then the return code is 403

  Scenario Outline: User can configure space
    Given the user has created an account with name "<username>"
    When the user configures the space to write access "<writeAccess>" and invite "<inviteAllowed>"
    Then the return code is 202
    And the config has write access "<writeAccess>" and invite "<inviteAllowed>"
    Examples:
      | username | writeAccess | inviteAllowed |
      | test12   | false       | false         |
      | test13   | false       | true          |
      | test14   | true        | false         |
      | test15   | true        | true          |

  Scenario: User without access cannot configure
    Given the user has created an account with name "test16"
    And another user has an account with name "other16"
    When the other user configures the space
    Then the return code is 403

  Scenario: User without access cannot configure
    Given the user has created an account with name "test17"
    And another user has an account with name "other17"
    And the other user has access
    When the other user configures the space
    Then the return code is 406

  Scenario: User can change auth key
    Given the user has created an account with name "test18"
    When the user changes the auth key to "PizzaPasta"
    Then the return code is 200
    And the auth key is "PizzaPasta"


  Scenario: Users without access cannot change auth key
    Given the user has created an account with name "test19"
    And another user has an account with name "other19"
    When the other user changes the auth key
    Then the return code is 403
    And the auth key remains unchanged


  Scenario: Only creator can change auth key
    Given the user has created an account with name "test20"
    And another user has an account with name "other20"
    And the other user has access
    When the other user changes the auth key
    Then the return code is 406
    And the auth key remains unchanged