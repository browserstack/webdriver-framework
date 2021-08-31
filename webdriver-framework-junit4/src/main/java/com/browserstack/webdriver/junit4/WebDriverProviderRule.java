package com.browserstack.webdriver.junit4;

import java.net.MalformedURLException;
import com.browserstack.webdriver.config.DriverType;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.browserstack.webdriver.config.Platform;
import com.browserstack.webdriver.core.WebDriverFactory;

public class WebDriverProviderRule extends TestWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverProviderRule.class);
    private static final String TEST_STATUS_SCRIPT = "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"%s\", \"reason\": \"%s\"}}";
    private String methodName;
    private WebDriver driver;
    private final WebDriverFactory webDriverFactory;

    public WebDriverProviderRule() {
        this.webDriverFactory = WebDriverFactory.getInstance();
    }

    /**
     * @return the name of the currently-running test method
     */
    public WebDriver getWebDriver(Platform platform) throws MalformedURLException {
        this.driver = webDriverFactory.createWebDriverForPlatform(platform, this.methodName);
        return driver;
    }

    /**
     * Invoked when a test succeeds
     */
    protected void succeeded(Description description) {
        LOGGER.info("Succeeded Test :: {} WebDriver Session :: {}", description.getDisplayName(), this.driver);
        if(webDriverFactory.getDriverType() == DriverType.cloudDriver){
            ((JavascriptExecutor) driver).executeScript(String.format(TEST_STATUS_SCRIPT, "passed", "Test Passed"));
        }
    }

    /**
     * Invoked when a test fails
     */
    protected void failed(Throwable e, Description description) {
        LOGGER.info("Failed Test :: {} WebDriver Session :: {}", description.getDisplayName(), this.driver, e);
        if(webDriverFactory.getDriverType() == DriverType.cloudDriver){
            ((JavascriptExecutor) driver).executeScript(String.format(TEST_STATUS_SCRIPT, "failed", e.getMessage()));
        }
    }


    @Override
    protected void starting(Description d) {
        methodName = d.getMethodName();
    }

    /**
     * Invoked when a test method finishes (whether passing or failing)
     */
    protected void finished(Description description) {
        LOGGER.info("Finished Test :: {} WebDriver Session :: {}", this.methodName, this.driver);
        LOGGER.info("Quitting the WebDriver instance :: {}", this.driver);
        if (this.driver != null) {
            this.driver.quit();
        }
    }


}
