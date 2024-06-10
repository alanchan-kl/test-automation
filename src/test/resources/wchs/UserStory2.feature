@defaultdriver @US2
Feature: User Story 2

  @US2_AC1 @US2_AC2 @US2_AC3 @US2_AC4
  Scenario: As the Clerk, I should be able to upload a csv file through the portal so that I can populate the database from a UI.
  AC1: Upload csv file
  AC2: Check csv file format
  AC3: Validate csv upload successful
  AC4: Validate upload invalid format of csv file (Upload same record)

    Given I delete working class hero record with natid 'natid-11'
    And I access to 'http://localhost:9997/login' webpage
    And I access Working Class Hero System as a clerk
    When I choose to add hero by upload a csv file
    Then I should upload 'wchs-userstory2-valid.csv' file in 'upload' directory with the following data
      | natid-11 | Hero A | MALE | 2000-04-08T00:00:00 | <blank> | 10 | 1 | 1 |
    When I select 'wchs-userstory2-valid.csv' file and upload into system
    Then I should see upload success notification
    And the working class heros table should be expected based on natid '<natid>'
      | natid-11 | Hero A | MALE   | 2000-04-08T00:00:00 | <blank>    | <10            | 1.0    | 1.0      |
    When I select 'wchs-userstory2-valid.csv' file and upload into system
    Then I should see upload error notification
    ### count record make sure not insert

