package com.browserstack.appiumdriver.testng;

import com.browserstack.appiumdriver.config.Platform;
import com.browserstack.appiumdriver.core.AppiumDriverFactory;

import io.appium.java_client.AppiumDriver;


/**
 * Created with IntelliJ IDEA.
 *
 * @author Anirudha Khanna
 */
public class ManagedAppiumDriver {

    private String testName;
    private final AppiumDriverFactory appiumDriverFactory;
    private final Platform platform;
    private AppiumDriver<?> appiumDriver;

    public ManagedAppiumDriver(String testMethodName, Platform platform) {
        this.testName = testMethodName;
        this.platform = platform;
        this.appiumDriverFactory = AppiumDriverFactory.getInstance();
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }


    public AppiumDriver<?> getAppiumDriver() {
        if (this.appiumDriver == null) {
            this.appiumDriver = this.appiumDriverFactory.createAppiumDriverForPlatform(platform, testName);
        }
        return this.appiumDriver;
    }

    public Platform getPlatform() {
        return this.platform;
    }

    public void quitDriver() {
        if (this.appiumDriver != null) {
            this.appiumDriver.quit();
            this.appiumDriver = null;
        }
    }

}
