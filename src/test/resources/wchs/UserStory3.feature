@defaultdriver @US3
Feature: User Story 3

  @US3_AC1 @US3_AC2 @US3_AC3 @US3_AC4
  Scenario: As the Book Keeper, I should be able to generate a Tax Relief Egress File from the UI
  AC1: When I Iogin as a Book Keeper, I should be able to generate a tax relief egress file taxrelief.txt
  by clicking the Generate Tax Relief File button
  AC2: File contain a body where each line is in the format: <natid>, <tax relief amount>, followed
  by a footer which is a number that indicates the total number of records written to the file
  AC3: If there are no records to be generated, a file is still being generated containing only the
  footer
  AC4: Each time a file process is being triggered, a file of FILE_TYPE: TAX_RELIEF record is
  being persisted into a database table FILE containing the file status, total count of records
  being written into file. File statuses at the end of a job are COMPLETED (on successful generation)
  or ERROR otherwise
  ## not sure where to verify the <tax relief amount>
  ## FILE status stuck at 'NEW', not sure how to verify 'COMPLETED' and 'ERROR'
  ## FILE total_count always shows 0
  ## Generate button disabled after refresh, and click generate multiples times
  ## Test has to run first due to check no records

    Given I delete all file records
    And I delete all voucher records
    And I delete all working class hero records
    And I access to 'http://localhost:9997/login' webpage
    And I access Working Class Hero System as a book keeper
    When I generate tax relief file
    Then I should upload 'tax_relief_file.txt' file in 'downloads' directory with the following data
      | 0 |
    When I add working hero with 'wchs-userstory1-ac1-1.json' file by calling '/api/v1/hero' endpoint
    Then System should return '200' response code
    And I delete all file records
    And I refresh current page
    When I generate tax relief file
    Then I should upload 'tax_relief_file.txt' file in 'downloads' directory with the following data
      | natid-12 | 7.2 |
      | 1        |     |
    And the file table should be expected based on file path 'alanchan'
      | file_status | total_count |
      | NEW         | 0           |



