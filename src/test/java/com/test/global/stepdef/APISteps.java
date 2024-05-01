package com.test.global.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.APIHelper;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.json.JSONObject;
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

    @And("^System should return '(.*)' response code$")
    public void verifyStatusCode(String statusCode) {
        Assert.assertEquals("Response code does not match with expected. ", statusCode, webUI.getStateVariable("%responseCode%"));
    }

    @And("^System should return '(.*)' as '(.*)'$")
    public void verifyResponseBody(String responsebody, String expected){
        String getResult = webUI.getStateVariable("%responseBody%");
        getResult = getResult.replaceAll("<COMMA>",",");
        getResult = getResult.replaceAll("<DOUBLE_QUOTES>","\"");
        getResult = getResult.replaceAll("<","{");
        getResult = getResult.replaceAll(">","}");
        JSONObject jsonObject = new JSONObject(getResult);
        Assert.assertEquals("Response code does not match with expected. ", expected, jsonObject.get(responsebody));
    }
}
