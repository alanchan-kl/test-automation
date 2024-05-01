package com.test.wchs.pom;

import com.test.global.support.TestConfig;
import com.test.global.support.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BookKeeperPage {
    private final WebUI webUI;

    By labelTitle = By.xpath("//h1[text()='Book Keeper Dashboard']");
    By generateBtn = By.xpath("//button[@id='tax_relief_btn']");

    public BookKeeperPage(final WebUI webUI) {
        this.webUI = webUI;
    }

    public void clickGenerateFile() {
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(labelTitle));
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(generateBtn));
        webUI.findElement(generateBtn).click();
    }
}
