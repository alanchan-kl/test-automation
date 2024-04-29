package com.test.wchs.pom;

import com.test.global.support.TestConfig;
import com.test.global.support.WebUI;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ClerkPage {
    private final WebUI webUI;

    By labelTitle = By.xpath("//h1[text()='Clerk Dashboard']");
    By addHeroBtn = By.xpath("//button[text()='Add a hero']");
    By addBtn = By.xpath("//a[text()='Add']/parent::li");
    By uploadCsvBtn = By.xpath("//a[text()='Upload a csv file']/parent::li");

    By labelAddHero = By.xpath("//p[text()='Add a hero']");

    By labelUploadFile = By.xpath("//label[text()='Upload CSV file']");
    By inputUploadCsv = By.xpath("//input[@type='file'][@id='upload-csv-file']");
    By createBtn = By.xpath("//button[text()='Create']");
    By labelNotification = By.xpath("//div[@id='notification-block']");

    public ClerkPage(final WebUI webUI) {
        this.webUI = webUI;
    }

    public void selectAction(String action) {
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(labelTitle));
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(addHeroBtn));
        webUI.findElement(addHeroBtn).click();
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(addBtn));

        if (action.equals("manual")) {
            webUI.findElement(addBtn).click();
            webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(labelAddHero));
        } else if (action.equals("csv")) {
            webUI.findElement(uploadCsvBtn).click();
            webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(labelUploadFile));
        }
    }

    public void uploadCsvFileAndCreate(String fileName, Boolean enableSubmit) throws FileNotFoundException {
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(inputUploadCsv));

        File folder = new File(TestConfig.get("test.auto.upload.dir"));
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null)
            throw new NullPointerException("Unable to file directory: " + TestConfig.get("test.auto.upload.dir"));

        boolean isFileFound = false;
        for (File listOfFile : listOfFiles) {
            if (!listOfFile.isDirectory()) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equalsIgnoreCase(fileName)) {
                        isFileFound = true;
                        System.out.println("Uploading file: " + listOfFile.getAbsolutePath());
                        webUI.findElement(inputUploadCsv).sendKeys(listOfFile.getAbsolutePath());
                    }
                }
            }
        }

        if (!isFileFound) {
            throw new FileNotFoundException(String.format("Error in uploading file: ") + fileName);
        }

        if (enableSubmit == true) {
            webUI.findElement(createBtn).click();
        }
    }

    public void verifyCsvFile(String path, String fileName, DataTable table) throws IOException {
        List<List<String>> expectedList = table.asLists(String.class);
        List<String> expectedListResult = new ArrayList<>();

        try {
            for (List<String> col : expectedList) {
                for (String str : col) {
                    if (str != null && !str.isEmpty()) {
                        if (str.contains("<newLine>") || str.contains("<blank>")) {
                            str = "";
                        }
                        expectedListResult.add(str);
                    }
                }
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("Somethings wrong with the datatable, please check.");
        }

        FileReader fr = null;

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null)
            throw new NullPointerException("Unable to find directory: " + path);

        boolean isFileFound = false;
        for (File listOfFile : listOfFiles) {
            if (!listOfFile.isDirectory()) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equalsIgnoreCase(fileName)) {
                        isFileFound = true;
                        System.out.println("Reading file: " + listOfFile.getAbsolutePath());
                        fr = new FileReader(listOfFile.getAbsolutePath());
                    }
                }
            }
        }

        if (!isFileFound) {
            throw new FileNotFoundException(String.format("Error in reading file: ") + fileName);
        }

        List<List<String>> actualTable = new ArrayList<>();
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        while (line != null) {
            List<String> lineData = Arrays.asList(line.split(","));
            actualTable.add(lineData);
            line = br.readLine();
        }

        List<String> actualListResult = actualTable.stream().flatMap(Collection::stream).collect(Collectors.toList());
        br.close();

        Assert.assertEquals("CSV does not match with expected. ", expectedListResult, actualListResult);
    }

    public void verifyNotificationMsg(String expectedMessage){
        webUI.waitUntil(Integer.parseInt(TestConfig.get("test.auto.polltimeout")), ExpectedConditions.visibilityOfElementLocated(labelNotification));
        String getText = webUI.findElement(labelNotification).getText();
        System.out.println("Expected Text: " + expectedMessage);
        System.out.println("Actual Text: " + getText);
        Assert.assertEquals(expectedMessage, getText);
    }
}
