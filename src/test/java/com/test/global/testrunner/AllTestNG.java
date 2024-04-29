package com.test.global.testrunner;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@Test
@CucumberOptions(
        features = {"src/test/resources/"},
        glue = "com.test",
        plugin = {"pretty",
                "rerun:target/site/rerun.txt",
                "json:target/site/cucumber-reports/cucumber.json",
                "html:target/site/cucumber-pretty"
        },
        monochrome = true
)

public class AllTestNG extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
