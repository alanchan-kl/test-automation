package com.test.global.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.java.en.And;

public class CommonSteps extends DomainSteps {
    //public LoginPage loginPage;

    public CommonSteps(final WebUI webUI, final WebState state) {
        super(webUI, state);
        //loginPage = new LoginPage(webUI);
    }

    @And("^I refresh current page$")
    public void refreshPage() {
        webUI.refreshPage();
    }
}
