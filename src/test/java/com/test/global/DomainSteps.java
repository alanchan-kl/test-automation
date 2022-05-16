package com.test.global;

import com.test.global.support.TestConfig;
import com.test.global.support.WebState;
import com.test.global.support.WebUI;

public class DomainSteps {
    protected final WebUI webUI;
    protected final WebState state;
    protected final double pollTimeout;
    protected final double pollInterval;

    public DomainSteps(final WebUI webUI, final WebState state) {
        this.webUI = webUI;
        this.state = state;
        this.pollTimeout = Double.parseDouble(TestConfig.get("test.auto.polltimeout")) * 1000;
        this.pollInterval = Double.parseDouble(TestConfig.get("test.auto.pollinterval")) * 1000;
    }
}
