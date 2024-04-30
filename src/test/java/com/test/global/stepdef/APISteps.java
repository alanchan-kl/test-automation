package com.test.global.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.APIHelper;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class APISteps extends DomainSteps {
    public APIHelper apiHelper;
    private final WebUI webUI;

    public APISteps(final WebUI webUI, final WebState state) {
        super(webUI, state);
        apiHelper = new APIHelper(webUI);
        this.webUI = webUI;
    }

    @Given("^I add working hero with '(.*)' file by calling '(.*)' endpoint$")
    public void setCredentialAndSubmit(String fileName, String endPoint) {
        apiHelper.sendRespondData(endPoint, fileName);
    }

    @And("^I should see '(.*)' response code$")
    public void verifyStatusCode(String statusCode) {
        Assert.assertEquals("Response code does not match with expected. ", statusCode, webUI.getStateVariable("%responseCode%"));
    }
}
