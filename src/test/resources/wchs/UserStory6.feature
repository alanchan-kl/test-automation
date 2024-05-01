@defaultdriver @US6
Feature: User Story 6

  @US6_AC1
  Scenario: As the system owner, I want to provide an API that gives insight into the number of vouchers each customer has for each voucher category

    Given I send an external service request with '/api/v1/voucher/by-person-and-type' endpoint to get owe money status
    Then System should return '200' response code
    ## need check db based on records
    And System should return '\{\"data\"\:\[\{\"name\"\:\"Voucher Test\"\,\"voucherType\"\:\"TRAVEL\"\,\"count\"\:1\},\{\"name\"\:\"Lorem ipsum dolor sit amete consectetuer adipiscing elite Aenean commodo ligula eget dolore Aenean mT\"\,\"voucherType\"\:\"TRAVEL\"\,\"count\":1\},\{\"name\":\"Lorem ipsum dolor sit amete consectetuer adipiscing elite Aenean commodo ligula eget dolore Aenean m\",\"voucherType\"\:\"TRAVEL\"\,\"count\"\:1\},\{\"name\"\:\"Voucher Test\",\"voucherType\"\:\"TRAVEL\",\"count\"\:1\}\],\"timestamp\"\:\".*\"\}' response body
