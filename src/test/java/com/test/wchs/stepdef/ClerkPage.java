package com.test.wchs.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ClerkPage extends DomainSteps {
    public com.test.wchs.pom.ClerkPage clerkPage;

    public ClerkPage(WebUI webUI, WebState state) {
        super(webUI, state);
        clerkPage = new com.test.wchs.pom.ClerkPage(webUI);
    }

    @Given("I choose to add hero by upload a csv file")
    public void selectAction() {
        clerkPage.selectAction("csv");
    }

    @And("^I select '(.*)' file and upload into system$")
    public void createHeroViaCsv(String fileName) throws FileNotFoundException, InterruptedException {
        clerkPage.uploadCsvFileAndCreate(fileName, true);
    }

    @And("^I should upload '(.*)' file in '(.*)' directory with the following data$")
    public void verifyFile(String fileName, String path, DataTable table) throws IOException, InterruptedException {
        clerkPage.verifyFile(path, fileName, table);
    }

    @And("^I should see upload success notification$")
    public void verifyUploadSuccess() {
        clerkPage.verifyNotificationMsg("Created Successfully!");
    }

    @And("^I should see upload error notification$")
    public void verifyUploadError() {
        clerkPage.verifyNotificationMsg("Unable to create hero!\n" +
                "There are 1 records which were not persisted! Please contact tech support for help!");
    }
}
