package com.test.wchs.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.java.en.Given;

public class BookKeeperPage extends DomainSteps {
    public com.test.wchs.pom.BookKeeperPage bookKeeperPage;

    public BookKeeperPage(WebUI webUI, WebState state) {
        super(webUI, state);
        bookKeeperPage = new com.test.wchs.pom.BookKeeperPage(webUI);
    }

    @Given("^I generate tax relief file$")
    public void createHeroViaCsv() {
        bookKeeperPage.clickGenerateFile();
    }

}
