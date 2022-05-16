package com.test.global.support;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hooks {
    private static final Logger LOGGER = Logger.getLogger(Hooks.class.getName());

    private boolean hasDriver;
    private final WebUI webUI;
    private final WebState webState;
    private static int scenarioRunId;
    public static int count = 0;

    public Hooks(final WebUI webUI, final WebState webState) {
        this.webUI = webUI;
        this.webState = webState;
    }

    private boolean containsTag(Scenario scenario, String arg) {
        for (String tagName : scenario.getSourceTagNames())
            if (tagName.toLowerCase().equals(arg))
                return true;
        return false;
    }

    private boolean containsPartialTag(Scenario scenario, String arg) {
        for (String tagName : scenario.getSourceTagNames())
            if (tagName.toLowerCase().contains(arg))
                return true;
        return false;
    }

    @Before
    public void before(Scenario scenario) throws Throwable {
        String cucumberOptions = System.getProperty("cucumber.options");

        //Initializing driver
        hasDriver = false;
        if (containsTag(scenario, "@chromedriver")) {
            webUI.initDriver("chrome");
            hasDriver = true;
        } else if (containsTag(scenario, "@defaultdriver")) {
            webUI.initDriver(TestConfig.get("test.auto.driver"));
            hasDriver = true;
        }
    }

    @AfterStep
    public void afterStep(Scenario scenario) throws Throwable {
        if (Boolean.parseBoolean(TestConfig.get("test.auto.driver.screenshot")) && !scenario.isFailed()) {
            scenario.attach(webUI.getScreenshot(), "image/png", "screenshot");
        }
    }

    @After
    public void after(Scenario scenario) throws Throwable {
        webState.clear();

        if (!hasDriver) return;
        if (scenario.isFailed()) {
            if (webUI.alertAvailable()) {
                webUI.closeAlert(false);
            }

            scenario.attach(webUI.getScreenshot(), "image/png", "screenshot");
            LOGGER.log(Level.INFO, "Scenario failed {0}", scenario.getName());
        }

        this.printLogs(scenario);

        webUI.quit();
    }

    private void printLogs(Scenario scenario) {
        if (scenario.isFailed()) {
            LOGGER.log(Level.INFO, "Console log for Scenario: {0}", scenario.getName());

            for (String log : webUI.consoleLogs) {
                System.out.println(log);
            }

            webUI.consoleLogs = new ArrayList<String>();
        }
    }
}
