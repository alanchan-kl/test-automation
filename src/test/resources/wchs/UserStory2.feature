@defaultdriver
Feature: User Story 2

  @TEST1
  Scenario: As the Clerk, I should be able to upload a csv file through the portal so that I can populate the database from a UI.
  AC1: Upload csv file
  AC2: Check csv file format
  AC3: Validate csv upload successful
  AC4: Validate upload invalid format of csv file (Upload same record)

    Given I access to 'http://localhost:9997/login' webpage
    And I access Working Class Hero System as a clerk
    When I choose to add hero by upload a csv file
    Then I should upload 'wchs-userstory2-valid.csv' file in 'upload' directory with the following data
      | natid-11 | Hero A | MALE | 2000-04-08T00:00:00 | <blank> | 10 | 1 | 1 |
    When I select 'wchs-userstory2-valid.csv' file and upload into system
    Then I should see upload success notification
    When I select 'wchs-userstory2-valid.csv' file and upload into system
    Then I should see upload error notification


