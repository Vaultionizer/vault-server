Feature: A file can be downloaded
  Scenario: The file can be downloaded successfully
    Given the user has successfully created an account with username "cell"
    And the file with saveIndex 420 was uploaded
    When the user requests to download the file with saveIndex 420
    Then the status code of download is 200

  Scenario: User is not authorized
    When the user requests to download the file with saveIndex 420
    Then the status code of download is 401


  Scenario: User has no access to the space
    Given the user has successfully created an account with username "yamcha"
    And the space id is set inappropriately
    And the file with saveIndex 420 was uploaded
    When the user requests to download the file with saveIndex 420
    Then the status code of download is 403


  Scenario: The file was not uploaded
    Given the user has successfully created an account with username "frieza"
    When the user requests to download the file with saveIndex 420
    Then the status code of download is 404


  Scenario: The file is currently modified or uploaded
    Given the user has successfully created an account with username "greatSayaman"
    And the file with saveIndex 420 was uploaded
    And the file with saveIndex 420 is being modified
    When the user requests to download the file with saveIndex 420
    Then the status code of download is 423