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

    @Given("^I send an external service request with '(.*)' endpoint to get owe money status$")
    public void sendGetRespondData(String endPoint) {
        apiHelper.sendGetRespondData(endPoint);
    }

    @Given("^I add working hero with '(.*)' file by calling '(.*)' endpoint$")
    public void sendPostRespondData(String fileName, String endPoint) {
        apiHelper.sendPostRespondData(endPoint, fileName);
    }

    @And("^System should return '(.*)' response code$")
    public void verifyStatusCode(String statusCode) {
        Assert.assertEquals("Response code does not match with expected. ", statusCode, webUI.getStateVariable("%responseCode%"));
    }

    @And("^System should return '(.*)' response body$")
    public void verifyRequestBody(String requestbody) {
        String actualResult = webUI.getStateVariable("%responseBody%");
        actualResult = actualResult.replaceAll("<COMMA>",",");
        actualResult = actualResult.replaceAll("<DOUBLE_QUOTES>","\"");
        actualResult = actualResult.replaceAll("<","{");
        actualResult = actualResult.replaceAll(">","}");
        Assert.assertTrue("Response body does not match with expected.", actualResult.matches(requestbody));
    }

    @And("^System should return '(.*)' as '(.*)'$")
    public void verifyResponseBody(String responsebody, String expected){
        String actualResult = webUI.getStateVariable("%responseBody%");
        actualResult = actualResult.replaceAll("<COMMA>",",");
        actualResult = actualResult.replaceAll("<DOUBLE_QUOTES>","\"");
        actualResult = actualResult.replaceAll("<","{");
        actualResult = actualResult.replaceAll(">","}");
        JSONObject jsonObject = new JSONObject(actualResult);
        Assert.assertEquals("Response code does not match with expected. ", expected, jsonObject.get(responsebody));
    }
}
