package com.test.facebook.pom;

import com.test.global.support.Accounts;
import com.test.global.support.TestConfig;
import com.test.global.support.WebUI;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage {
    private final WebUI webUI;

    By inputEmail = By.xpath("//input[@id='email']");
    By inputPassword = By.xpath("//input[@id='pass']");
    By btnLogin = By.xpath("//button[text()='Log In']");
    By labelEmailErrorMsg1 = By.xpath("//div[contains(text(),'The email address you entered')]");
    String emailErrorMsg1 = "The email address you entered isn't connected to an account. Find your account and log in.";

    public LoginPage(final WebUI webUI) {
        this.webUI = webUI;
    }

    public void navigateUrl() {
        webUI.navigateURL("https://facebook.com", false);
        webUI.waitForPageLoad();
        webUI.manageTimeout0();
    }

    public void setCredentialAndSubmit(String email, String password) throws Exception {
        switch (email.toLowerCase()) {
            case "default":
                email = Accounts.getInstance("Facebook").getNextUsername("user");
                break;
        }

        switch (password.toLowerCase()) {
            case "default":
                password = TestConfig.get("test.auto.common.password");
                break;
        }

        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(inputEmail));
        webUI.findElement(inputEmail).click();
        webUI.findElement(inputEmail).sendKeys(email);
        webUI.findElement(inputPassword).click();
        webUI.findElement(inputPassword).sendKeys(password);
        webUI.findElement(btnLogin).click();
    }

    public void verifyEmailErrorMsg1() {
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(labelEmailErrorMsg1));
        String getText = webUI.findElement(labelEmailErrorMsg1).getText();
        System.out.println("Expected Text: " + emailErrorMsg1);
        System.out.println("Actual Text: " + getText);
        Assert.assertEquals(emailErrorMsg1, getText);
    }
}
