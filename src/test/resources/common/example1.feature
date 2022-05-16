@defaultdriver
Feature: Demo Feature 1

  @DEMO1
  Scenario: User access to facebook webpage 1
    Given I access to facebook webpage
    When I enter my email "default" and password "default" on 面子书 and click login
    Then I should able to see invalid email error message on 面子书

  @DEMO1
  Scenario: User access to facebook webpage 2
    Given I access to facebook webpage
    When I enter my email "default" and password "default" on 面子书 and click login
