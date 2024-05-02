@defaultdriver @US4
Feature: User Story 4

  @US4_AC1 @US4_AC4
  Scenario: As the system owner, I want to provide an API that creates a working class hero with vouchers.
  AC1: POST request /api/v1/hero/vouchers
  AC4: Vouchers are created in table VOUCHERS

    Given I delete voucher record with natid 'natid-41'
    And I delete working class hero record with natid 'natid-41'
    When I add working hero with 'wchs-userstory4-ac1-1.json' file by calling '/api/v1/hero/vouchers' endpoint
    Then System should return '200' response code
    When I add working hero with 'wchs-userstory4-ac1-2.json' file by calling '/api/v1/hero/vouchers' endpoint
    Then System should return '400' response code
    And System should return 'errorMsg' as 'Working Class Hero of natid: natid-41 already exists!'
    And the working class heros table should be expected based on natid 'natid-41'
      | natid    | name         | gender | birth_date          | death_date | brownie_points | salary | tax_paid |
      | natid-41 | Voucher Test | MALE   | 2000-04-08T00:00:00 | null       | 1              | 10.0   | 1.0      |
    And the vouchers table should be expected based on natid 'nat-41'
      | VOUCHER 1 | TRAVEL |

  @US4_AC2
  Scenario Outline: As the system owner, I want to provide an API that creates a working class hero with vouchers. (Positive flow)
  AC2: Field validation are the same as User Story 1

    Given I delete voucher record with natid '<natid>'
    And I delete working class hero record with natid '<natid>'
    When I add working hero with '<fileName>' file by calling '/api/v1/hero/vouchers' endpoint
    Then System should return '200' response code
    And the working class heros table should be expected based on natid '<natid>'
      | natid   | name   | gender   | birth_date   | death_date   | brownie_points   | salary   | tax_paid   |
      | <natid> | <name> | <gender> | <birth_date> | <death_date> | <brownie_points> | <salary> | <tax_paid> |
    And the vouchers table should be expected based on natid '<natid>'
      | VOUCHER 1 | TRAVEL |

    Examples:
      | fileName                   | natid    | name                                                                                                 | gender | birth_date          | death_date      | brownie_points | salary  | tax_paid |
      ## BUG Hit 500 Internal Server Error when input death_date (UI&API)
      ##java.util.regex.PatternSyntaxException: Unmatched closing ')' near index 58
      ##^([0-9]{4}-[0-9]{2}-[0-9]{2})(T[0-9]{2}:[0-9]{2}:[0-9]{2})?)*$
      | wchs-userstory4-ac2-1.json | natid-0  | A                                                                                                    | MALE   | <today_date>        | <today+30_date> | 0              | 0.0     | 0.0      |
      | wchs-userstory4-ac2-2.json | natid-42 | Lorem ipsum dolor sit amete consectetuer adipiscing elite Aenean commodo ligula eget dolore Aenean m | FEMALE | 1961-04-08T00:00:00 | null            | null           | 10000.0 | 2000.0   |

  @US4_AC2 @US4_AC3 @US4_AC5
  Scenario Outline: As the system owner, I want to provide an API that creates a working class hero with vouchers. (Negative flow)
  AC2: Field validation are the same as User Story 1
  AC3: If vouchers is null or empty, the working class hero cannot be created
  AC5: If any validation fails, nothing is persisted.

    Given I delete voucher record with natid '<natid>'
    And I delete working class hero record with natid '<natid>'
    When I add working hero with '<fileName>' file by calling '/api/v1/hero/vouchers' endpoint
    Then System should return '400' response code
    And System should return 'errorMsg' as '<errorMsg>'
    And the working class heros table should be expected based on natid '<natid>'
      | null | null | null | null | null | null | null | null |
    And the vouchers table should be expected based on natid '<natid>'
      | null | null |

    Examples:
      | fileName                    | natid          | errorMsg                                      |
      ##BUG - able to create record, should return error based on US1
      | wchs-userstory4-ac2-3.json  | natid-10000000 | Invalid natid                                 |
      ##BUG - able to create record, should return error based on US1
      | wchs-userstory4-ac2-4.json  | natid-44       | Name must be between 1 and 100 characters     |
      | wchs-userstory4-ac2-5.json  | natid-45       | Invalid gender                                |
      | wchs-userstory4-ac2-6.json  | natid-46       | There are some issues with working class hero |
      | wchs-userstory4-ac2-7.json  | natid-47       | Salary must be greater than or equals to zero |
      ## description never mention taxPaid
      | wchs-userstory4-ac2-8.json  | natid-48       | must be greater than or equal to 0            |
      | wchs-userstory4-ac2-9.json  | natid-49       | vouchers cannot be null or empty              |
      | wchs-userstory4-ac2-10.json | natid-50       | Voucher Name cannot be blank                  |
      ## system never check on voucherType empty, will hit error when call by-person-and-type