package com.test.global.support;

import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUI {
    private WebDriver driver;
    private final WebState state;
    private final double pollTimeout;
    private final double pollInterval;
    private WebUI previousDriver = null;
    private WebUI newDriver = null;
    private String currentWindow = null;
    private String previousWindow = null;

    private static volatile long totalTimeBed;
    public List<String> consoleLogs = new ArrayList<>();
    public static String downloadpath = "";
    public static String uploadpath = "";

    static {
        //TODO: not optimal until we get @AfterAll hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.printf("Total sleep time: %dms\n", totalTimeBed);
            }
        });
    }

    public WebUI(final WebState state) {
        this.state = state;
        this.pollTimeout = Double.parseDouble(TestConfig.get("test.auto.polltimeout")) * 1000;
        this.pollInterval = Double.parseDouble(TestConfig.get("test.auto.pollinterval")) * 1000;
    }

    public void initDriver(String type) throws MalformedURLException {
        boolean headlessMode = false;
        String windowSize, uploadDir, downloadDir, headless;
        boolean runRemote = isRemote();
        String remoteHub = TestConfig.get("test.remote.hub");

        if (type.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", TestConfig.get("test.auto.driver.chrome"));

            ChromeOptions options = new ChromeOptions();
            headless = TestConfig.get("test.auto.driver.chrome.headless");
            if (headless != null) {
                if (headless.toLowerCase().contains("true")) {
                    options.addArguments("--headless");
                    headlessMode = true;
                }
            }

            uploadDir = TestConfig.get("test.auto.upload.dir");

            if (uploadDir != null) {
                uploadpath = uploadDir;
            }

            downloadDir = TestConfig.get("test.auto.download.dir");

            if (downloadDir != null) {
                if (runRemote) {
                    downloadpath = downloadDir;
                } else {
                    downloadpath = "target/test-classes/" + downloadDir;
                }

                File downloadFile = new File(downloadpath);

                if (!downloadFile.exists()) {
                    System.out.println("Trying to create directory");
                    downloadFile.mkdirs();
                }

                HashMap<String, Object> prefs = new HashMap<String, Object>();
                System.out.println(("obs download path"));
                System.out.println(downloadFile.getAbsolutePath());
                prefs.put("safebrowsing.enabled", false);
                prefs.put("safebrowsing.disable_download_protection", true);
                prefs.put("download.prompt_for_download", false);
                if (!this.isRemote()) {
                    prefs.put("download.default_directory", downloadFile.getAbsolutePath());
                } else {
                    prefs.put("download.default_directory", downloadDir);
                }
                options.setExperimentalOption("prefs", prefs);
            }
            //options.addArguments("start-fullscreen");
            options.addArguments("--window-size=1512,944");
            options.addArguments("--disable-site-isolation-trials");
            options.setCapability("goog:chromeOptions", options);

            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("goog:loggingPrefs", logPrefs);

            if (runRemote) {
                driver = new RemoteWebDriver(new URL(remoteHub), options);
            } else {
                driver = new ChromeDriver(options);
            }
        } else if (type.equals("firefox")) {
            System.setProperty("webdriver.gecko.driver", TestConfig.get("test.auto.driver.firefox"));

        } else if (type.equals("edge")) {

        }

        /***
         windowSize = TestConfig.get("test.auto.window.size");
         if (!headlessMode) {

         } else if (windowSize != null) {
         Matcher matcher = Pattern.compile("(\\d+)x(\\d+)").matcher(windowSize);
         if (!matcher.matches()) throw new IllegalArgumentException("Invalid window size option: " + windowSize);

         int width = Integer.parseInt(matcher.group(1));
         int height = Integer.parseInt(matcher.group(2));
         driver.manage().window().setSize(new Dimension(width, height));
         }
         ***/

        getScreenSize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
    }

    public Dimension getScreenSize() {
        Dimension screenSize = driver.manage().window().getSize();
        System.out.println("Screen Size: " + screenSize);
        return screenSize;
    }

    public void manageTimeout0() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    public void manageTimeout1() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
    }

    public void waitForPageLoad() {
        ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
    }

    public void scrollToWebElement(WebElement we) throws Exception {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", we);
    }

    public void scrollToWebElementAndClick(WebElement we) throws Exception {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", we);
    }

    public void navigateURL(final String url, boolean host) {
        if (host) {
            driver.get(TestConfig.get("test.auto.server") + url);
        } else {
            driver.get(url);
        }
        this.captureConsoleLogs();
    }

    public void captureConsoleLogs() {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries) {
            this.consoleLogs.add(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        }
    }

    public <V> V waitUntil(long timeout, Function<? super WebDriver, V> function) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(function);
    }

    public boolean alertAvailable() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException ex) {
            return false;
        }
    }

    public void closeAlert(boolean ok) {
        if (ok) {
            driver.switchTo().alert().accept();
        } else {
            driver.switchTo().alert().dismiss();
        }
    }

    public WebElement findElement(String locator, String locatorValue) {
        By byLocator = null;

        switch (locator.toLowerCase()) {
            case "classname":
                byLocator = By.className(locatorValue);
                break;
            case "cssselector":
                byLocator = By.cssSelector(locatorValue);
                break;
            case "id":
                byLocator = By.id(locatorValue);
                break;
            case "linktext":
                byLocator = By.linkText(locatorValue);
                break;
            case "name":
                byLocator = By.name(locatorValue);
                break;
            case "partiallinktext":
                byLocator = By.partialLinkText(locatorValue);
                break;
            case "tagname":
                byLocator = By.tagName(locatorValue);
                break;

            case "xpath":
            default:
                byLocator = By.xpath(locatorValue);
                break;
        }
        System.out.println("Finding element: " + byLocator.toString());
        return driver.findElement(byLocator);
    }

    public WebElement findElement(By by) {
        System.out.println("Finding element: " + by.toString());
        return driver.findElement(by);
    }

    public List<WebElement> findElements(String locator, String locatorValue) {
        switch (locator.toLowerCase()) {
            case "classname":
                return driver.findElements(By.className(locatorValue));
            case "cssselector":
                return driver.findElements(By.cssSelector(locatorValue));
            case "id":
                return driver.findElements(By.id(locatorValue));
            case "linktext":
                return driver.findElements(By.linkText(locatorValue));
            case "name":
                return driver.findElements(By.name(locatorValue));
            case "partiallinktext":
                return driver.findElements(By.partialLinkText(locatorValue));
            case "tagname":
                return driver.findElements(By.tagName(locatorValue));
            case "xpath":
                return driver.findElements(By.xpath(locatorValue));
            default:
                return null;
        }
    }

    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    public String getAttribute(WebElement webElement, String type) {
        String result;
        switch (type) {
            case "id":
                result = webElement.getAttribute("id");
                if (result != null)
                    if (result.length() > 0)
                        return result;
                break;
            case "text":
                if (webElement.getTagName().equalsIgnoreCase("input")
                        || webElement.getTagName().equalsIgnoreCase("textarea"))
                    return webElement.getAttribute("value");

                result = webElement.getText();
                if (result != null)
                    if (result.length() > 0)
                        return result;

                result = webElement.getAttribute("innerText");
                if (result != null)
                    if (result.length() > 0)
                        return result;
                break;
            case "value":
                result = webElement.getAttribute("value");
                if (result != null)
                    if (result.length() > 0)
                        return result;
                break;
            default:
                result = webElement.getAttribute(type);
                if (result != null)
                    if (result.length() > 0)
                        return result;
                break;
        }
        return "";
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    public byte[] getScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public void close() {
        driver.close();
    }

    public void quit() {
        driver.quit();
    }

    /**********Non WebDriver method**********/
    public void sleep(long ms) throws InterruptedException {
        totalTimeBed += ms;
        Thread.sleep(ms);
    }

    public Boolean isRemote() {
        return Boolean.valueOf(TestConfig.get("test.remote"));
    }
}
