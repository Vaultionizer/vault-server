Feature: The user can be deleted

  Scenario: The user was deleted successfully
    Given the user created an account with name "ruffy"
    When the user requests to delete the user
    Then the status code delete user is 200
    And the user's spaces are deleted
    And the user has no access to any spaces

  Scenario: The user is not authorized
    When the user requests to delete the user
    Then the status code delete user is 401
