package com.test.global.stepdef;

import com.test.global.DomainSteps;
import com.test.global.support.SQLHelper;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;
import io.cucumber.datatable.DataTable;
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

    @Given("^I delete working class hero record with natid '(.*)'$")
    public void deleteWorkingClassHeroRecordByNatid(String natid) throws SQLException {
        sqlHelper.deleteWorkingClassHeroRecordByNatid(natid);
    }

    @And("^I delete voucher record with natid '(.*)'$")
    public void deleteVoucherRecordByNatid(String natid) throws Throwable {
        sqlHelper.deleteVoucherByNatid(natid);
    }

    @And("^the working class heros table should be expected based on natid '(.*)'$")
    public void verifyWorkingClassHeroRecordByNatid(String natid, DataTable table) throws Throwable {
        sqlHelper.selectRecord("select natid, name, gender, DATE_FORMAT(birth_date, '%Y-%m-%dT%H:%i:%s') as birth_date, DATE_FORMAT(death_date, '%Y-%m-%dT%H:%i:%s') as death_date, brownie_points, salary, tax_paid from testdb.working_class_heroes where natid = '" + natid + "';", table);
    }

    @And("^the vouchers table should be expected based on natid '(.*)'$")
    public void verifyVoucherByNatid(String natid, DataTable table) throws Throwable {
        sqlHelper.selectRecord("select name, voucher_type from testdb.vouchers where working_class_hero_id = (select id from testdb.working_class_heroes where natid = '" + natid + "');", table);
    }
}
