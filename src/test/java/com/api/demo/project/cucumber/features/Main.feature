Feature: Main.feature

  Scenario: Create, Get, Update and Delete a user
    Given I prepare the request payload "auth.json"
    When I create a user
    Then The response status code shall be 201 and response body should contain "Mike"

    When I change user name
    Then The response status code should be 200 and response should contain message "User updated successfully"

    When I get all users
    Then The get response status code shall be 200 and response body should contain "Matt"

    When I delete a user
    Then The delete response status code should be 200 and response should contain message "User deleted successfully"

