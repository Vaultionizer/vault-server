Feature: A file can be uploaded

  Scenario: The file can be uploaded successfully
    Given the user has an account with name "goku"
    When the user requests to upload 10 files
    Then the status code of upload is 202
    And the saveIndex is 0
    When the user requests to upload 10 files
    Then the status code of upload is 202
    And the saveIndex is 10

  Scenario: Space id sub zero is bad request
    Given the user has an account with name "gohan"
    And the spaceID is -1
    When the user requests to upload 1 files
    Then the status code of upload is 400


  Scenario: Amount files zero or less is bad request
    Given the user has an account with name "krillin"
    When the user requests to upload 0 files
    Then the status code of upload is 400

  Scenario: The user has no access to the space
    Given the user has an account with name "chaozu"
    And the space is not accessible
    When the user requests to upload 10 files
    Then the status code of upload is 403

  Scenario: The user is not logged in
    When the user requests to upload 10 files
    Then the status code of upload is 401

  Scenario: the reffile does not exist
    Given the user has an account with name "piccolo"
    And the reffile does not exist
    When the user requests to upload 10 files
    Then the status code of upload is 404

