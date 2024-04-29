package com.test.wchs.pom;

import com.test.global.support.TestConfig;
import com.test.global.support.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage {
    private final WebUI webUI;

    By inputUsername = By.xpath("//input[@id='username-in']");
    By inputPassword = By.xpath("//input[@id='password-in']");
    By submitBtn = By.xpath("//input[@type='submit']");

    public LoginPage(final WebUI webUI) {
        this.webUI = webUI;
    }

    public void navigateUrl(String url) {
        webUI.navigateURL(url, false);
        webUI.waitForPageLoad();
        webUI.manageTimeout0();
    }

    public void setCredentialAndSubmit(String username, String password) throws Exception {
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(inputUsername));
        webUI.findElement(inputUsername).sendKeys(username);
        webUI.findElement(inputPassword).click();
        webUI.findElement(inputPassword).sendKeys(password);
        webUI.findElement(submitBtn).click();
    }
}
