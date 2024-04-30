package com.test.global.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.SQLHelper;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.java.en.*;

import java.sql.SQLException;

public class SQLSteps extends DomainSteps {
    public SQLHelper sqlHelper;
    private final WebUI webUI;

    public SQLSteps(final WebUI webUI, final WebState state) {
        super(webUI, state);
        sqlHelper = new SQLHelper(webUI);
        this.webUI = webUI;
    }

    @Given("^I delete working class heros record with natid '(.*)'$")
    public void setCredentialAndSubmit(String natid) throws SQLException {
        sqlHelper.deleteWorkingClassHerosRecordByNatid(natid);
    }
}
