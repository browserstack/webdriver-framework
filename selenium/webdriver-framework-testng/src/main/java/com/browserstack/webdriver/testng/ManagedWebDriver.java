package com.browserstack.webdriver.testng;

import org.openqa.selenium.WebDriver;

import com.browserstack.webdriver.config.Platform;
import com.browserstack.webdriver.core.WebDriverFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Anirudha Khanna
 */
public class ManagedWebDriver {

    private String testName;
    private final WebDriverFactory webDriverFactory;
    private final Platform platform;
    private WebDriver webDriver;

    public ManagedWebDriver(String testMethodName, Platform platform) {
        this.testName = testMethodName;
        this.platform = platform;
        this.webDriverFactory = WebDriverFactory.getInstance();
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }


    public WebDriver getWebDriver() {
        if (this.webDriver == null) {
            this.webDriver = this.webDriverFactory.createWebDriverForPlatform(platform, testName);
        }
        return this.webDriver;
    }

    public Platform getPlatform() {
        return this.platform;
    }

    public void quitDriver() {
        if (this.webDriver != null) {
            this.webDriver.quit();
            this.webDriver = null;
        }
    }

}
