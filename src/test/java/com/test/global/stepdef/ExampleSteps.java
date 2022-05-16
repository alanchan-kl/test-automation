package com.test.global.stepdef;

import com.test.facebook.pom.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import com.test.global.DomainSteps;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;

public class ExampleSteps extends DomainSteps {
    public LoginPage loginPage;

    public ExampleSteps(final WebUI webUI, final WebState state) {
        super(webUI, state);
        loginPage = new LoginPage(webUI);
    }
}
