package com.browserstack.webdriver.junit4;

import java.net.MalformedURLException;
import com.browserstack.webdriver.config.DriverType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private String methodName;
    private WebDriver driver;
    private final WebDriverFactory webDriverFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        if (webDriverFactory.getDriverType() == DriverType.cloudDriver) {
            String script = createExecutorScript("passed", "Test Passed");
            ((JavascriptExecutor) driver).executeScript(script);
        }
    }

    /**
     * Invoked when a test fails
     */
    protected void failed(Throwable e, Description description) {
        LOGGER.info("Failed Test :: {} WebDriver Session :: {}", description.getDisplayName(), this.driver, e);
        if (webDriverFactory.getDriverType() == DriverType.cloudDriver) {
            String script = createExecutorScript("failed", e.getMessage());
            ((JavascriptExecutor) driver).executeScript(script);
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

    private String createExecutorScript(String status, String reason) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode argumentsNode = objectMapper.createObjectNode();

        // Read only the first line of the error message
        reason = reason.split("\n")[0];
        // Limit the error message to only 255 characters
        if (reason.length() >= 255) {
            reason = reason.substring(0, 255);
        }

        argumentsNode.put("status", status);
        argumentsNode.put("reason", reason);

        rootNode.put("action", "setSessionStatus");
        rootNode.set("arguments", argumentsNode);
        String executorStr = "";
        try {
            executorStr = objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new Error("Error creating JSON object for Marking tests", e);
        }
        return "browserstack_executor: " + executorStr;
    }

}
