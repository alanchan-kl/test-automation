package com.test.facebook.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.java.en.Given;

public class LoginPage extends DomainSteps {
    public com.test.facebook.pom.LoginPage loginPage;

    public LoginPage(WebUI webUI, WebState state) {
        super(webUI, state);
        loginPage = new com.test.facebook.pom.LoginPage(webUI);
    }

    @Given("I access to facebook webpage")
    public void accessFbPage() {
        loginPage.navigateUrl();
    }

    @Given("I enter my email {string} and password {string} on 面子书 and click login")
    public void setCredentialAndSubmit(String email, String password) throws Exception {
        loginPage.setCredentialAndSubmit(email,password);
    }

    @Given("I should able to see invalid email error message on 面子书")
    public void verifyEmailErrorMsg1(){
        loginPage.verifyEmailErrorMsg1();
    }
}
