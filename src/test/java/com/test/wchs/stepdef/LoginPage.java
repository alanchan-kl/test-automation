package com.test.wchs.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.java.en.*;

public class LoginPage extends DomainSteps {
    public com.test.wchs.pom.LoginPage loginPage;

    public LoginPage(WebUI webUI, WebState state) {
        super(webUI, state);
        loginPage = new com.test.wchs.pom.LoginPage(webUI);
    }

    @Given("^I access to '(.*)' webpage$")
    public void accessPage(String url) {
        loginPage.navigateUrl(url);
    }

    @When("I access Working Class Hero System as a (clerk|book keeper)$")
    public void setCredentialAndSubmit(String role) throws Exception {
        if(role.equals("clerk")){
            loginPage.setCredentialAndSubmit("clerk", "clerk");
        } else if(role.equals("book keeper")) {
            loginPage.setCredentialAndSubmit("bk", "bk");
        }
    }
}
