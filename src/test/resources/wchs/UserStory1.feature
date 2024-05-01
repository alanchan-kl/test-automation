@defaultdriver
Feature: User Story 1

  @US1_AC1 @US1_AC3 @US1_AC4
  Scenario: Application allows the creation of a single working class hero via API call.
  AC1: POST request /api/v1/hero
  AC3: If the natid already exists in the database, return error status code 400. No changes
  should be made to the existing record.
  AC4: Verify record is created in database table WORKING_CLASS_HEROES

    Given I delete working class hero record with natid 'natid-12'
    When I add working hero with 'wchs-userstory1-ac1-1.json' file by calling '/api/v1/hero' endpoint
    Then System should return '200' response code
    When I add working hero with 'wchs-userstory1-ac1-2.json' file by calling '/api/v1/hero' endpoint
    Then System should return '400' response code
    And System should return 'errorMsg' as 'Working Class Hero of natid: natid-12 already exists!'
    And the working class heros table should be expected based on natid 'natid-12'
      | natid    | name     | gender | birth_date          | death_date | brownie_points | salary | tax_paid |
      | natid-12 | API test | MALE   | 2000-04-08T00:00:00 | null       | 1              | 10.0   | 1.0      |

  @US1_AC2
  Scenario Outline: Application allows the creation of a single working class hero via API call. (Positive flow)
  AC2: Field validations are as follow
  1. natid must be in the format “natid-number”, where number is between 0 to 9999999
  (inclusive)
  2. name must contain letters and spaces with a length between 1 and 100 (inclusive)
  3. gender must be either MALE or FEMALE
  4. birthDate format is in yyyy-mm-dd’T’HH:mm:ss and cannot be a future date. ‘T’ is a
  literal string and the time is in HH:mm:ss
  5. deathDate format is in yyyy-mm-dd’T’HH:mm:ss
  6. salary is a decimal and cannot be negative
  7. taxPaid is a decimal and cannot be negative
  8. browniePoints and deathDate are nullable

    Given I delete working class hero record with natid '<natid>'
    When I add working hero with '<fileName>' file by calling '/api/v1/hero' endpoint
    Then System should return '200' response code
    And the working class heros table should be expected based on natid '<natid>'
      | natid   | name   | gender   | birth_date   | death_date   | brownie_points   | salary   | tax_paid   |
      | <natid> | <name> | <gender> | <birth_date> | <death_date> | <brownie_points> | <salary> | <tax_paid> |

    Examples:
      | fileName                   | natid    | name                                                                                                 | gender | birth_date          | death_date      | brownie_points | salary  | tax_paid |
      ## BUG?? Hit 500 Internal Server Error when input death_date (UI&API)
      ##java.util.regex.PatternSyntaxException: Unmatched closing ')' near index 58
      ##^([0-9]{4}-[0-9]{2}-[0-9]{2})(T[0-9]{2}:[0-9]{2}:[0-9]{2})?)*$
      | wchs-userstory1-ac2-1.json | natid-0  | A                                                                                                    | MALE   | <today_date>        | <today+30_date> | 0              | 0.0     | 0.0      |
      | wchs-userstory1-ac2-2.json | natid-13 | Lorem ipsum dolor sit amete consectetuer adipiscing elite Aenean commodo ligula eget dolore Aenean m | FEMALE | 1961-04-08T00:00:00 | null            | null           | 10000.0 | 2000.0   |

  @US1_AC2
  Scenario Outline: Application allows the creation of a single working class hero via API call. (Negative flow)
  AC2: Field validations are as follow
  1. natid must be in the format “natid-number”, where number is between 0 to 9999999
  (inclusive)
  2. name must contain letters and spaces with a length between 1 and 100 (inclusive)
  3. gender must be either MALE or FEMALE
  4. birthDate format is in yyyy-mm-dd’T’HH:mm:ss and cannot be a future date. ‘T’ is a
  literal string and the time is in HH:mm:ss
  5. deathDate format is in yyyy-mm-dd’T’HH:mm:ss
  6. salary is a decimal and cannot be negative
  7. taxPaid is a decimal and cannot be negative
  8. browniePoints and deathDate are nullable

    Given I delete working class hero record with natid '<natid>'
    When I add working hero with '<fileName>' file by calling '/api/v1/hero' endpoint
    Then System should return '400' response code
    And System should return 'errorMsg' as '<errorMsg>'
    And the working class heros table should be expected based on natid '<natid>'
      | null | null | null | null | null | null | null | null |

    Examples:
      | fileName                   | natid          | errorMsg                                      |
      | wchs-userstory1-ac2-3.json | natid-10000000 | Invalid natid                                 |
      | wchs-userstory1-ac2-4.json | natid-14       | Name must be between 1 and 100 characters     |
      | wchs-userstory1-ac2-5.json | natid-15       | Invalid gender                                |
      | wchs-userstory1-ac2-6.json | natid-16       | There are some issues with working class hero |
      | wchs-userstory1-ac2-7.json | natid-17       | Salary must be greater than or equals to zero |
      ## description never mention taxPaid
      | wchs-userstory1-ac2-8.json | natid-18       | must be greater than or equal to 0            |
