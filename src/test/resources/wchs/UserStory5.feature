@defaultdriver
Feature: User Story 5

  @US5_AC1 @US5_AC2 @US5_AC3 @US5_AC4
  Scenario: As the system owner, I want to have an API that hits an external service’s API to find out if a working class hero owes money
  AC1: GET request /api/v1/hero/owe-money with query parameter natid=<number>
  AC2: The natid value should only accept numeric values.
  ## not sure how to produce status NIL
  AC3: The system should receive a response with the payload format like:
  {
  "data": "natid-<number>",
  "status": "OWE"
  }
  AC4: The system should respond in the following format
  {
  "message": {
  "data": "natid-<number>",
  "status": "OWE"
  }

    Given I send an external service request with '/api/v1/hero/owe-money?natid=1' endpoint to get owe money status
    Then System should return '200' response code
    And System should return '\{\"message\"\:\{\"data\"\:\"natid-1\",\"status\"\:\"OWE\"\},\"timestamp\"\:\".*\"\}' response body

  @US5_AC2
  Scenario Outline: As the system owner, I want to have an API that hits an external service’s API to find out if a working class hero owes money - (Negative flow)
  AC2: The natid value should only accept numeric values.

    Given I send an external service request with '/api/v1/hero/owe-money?natid=<natid>' endpoint to get owe money status
    Then System should return '<responseCode>' response code

    Examples:
      | natid  | responseCode |
      | &TEST& | 500          |
      | TEST   | 500          |
      | -1     | 500          |
      | 0.1    | 400          |