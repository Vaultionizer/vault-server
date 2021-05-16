Feature: One can create a user

  Scenario: Key too short creation
    Given the username is "mario"
    And the key is "luigiIdiotta!"
    And the refFile is long enough
    When the client wants to register
    Then the status code of register is 400

  Scenario: No reffile
    Given the username is "mario"
    And the key is long enough
    When the client wants to register
    Then the status code of register is 400

  Scenario: Too short username
    Given the username is "mario"
    And the key is long enough
    When the client wants to register
    Then the status code of register is 400


  Scenario: Empty reffile
    Given the username is "mario"
    And the key is long enough
    And the refFile is ""
    When the client wants to register
    Then the status code of register is 400

  Scenario: Successful creation
    Given the username is "mario"
    And the key is long enough
    And the refFile is long enough
    When the client wants to register
    Then the status code of register is 201
    And the user has a userID
    And the user has a sessionKey
    And the user has a websocketToken

  Scenario: Conflicting username
    Given the username is "mario"
    And the key is long enough
    And the refFile is long enough
    And the client wants to register
    When the client wants to register
    Then the status code of register is 409
