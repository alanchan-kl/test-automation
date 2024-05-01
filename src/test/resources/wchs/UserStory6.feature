@defaultdriver @US6
Feature: User Story 6

  @US6_AC1
  Scenario: As the system owner, I want to provide an API that gives insight into the number of vouchers each customer has for each voucher category

    Given I delete all voucher records
    And I delete all working class hero records
    When I add working hero with 'wchs-userstory4-ac1-1.json' file by calling '/api/v1/hero/vouchers' endpoint
    Then System should return '200' response code
    And I send an external service request with '/api/v1/voucher/by-person-and-type' endpoint to get owe money status
    Then System should return '200' response code
    ## need refactor to check db based on records
    And System should return '\{\"data\"\:\[\{\"name\"\:\"Voucher Test\",\"voucherType\"\:\"TRAVEL\",\"count\"\:1\}\],\"timestamp\"\:\".*\"\}' response body
