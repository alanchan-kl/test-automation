@defaultdriver
Feature: User Story 1

  @US1
  Scenario: Application allows the creation of a single working class hero via API call.
  AC1: POST request /api/v1/hero
  AC4: Verify record is created in database table WORKING_CLASS_HEROES

    Given I delete working class heros record with natid 'natid-12'
    When I add working hero with 'wchs-userstory1-valid.json' file by calling '/api/v1/hero' endpoint
    Then I should see '200' response code