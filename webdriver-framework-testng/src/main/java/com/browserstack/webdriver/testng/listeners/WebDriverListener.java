package com.browserstack.webdriver.testng.listeners;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.browserstack.webdriver.core.WebDriverFactory;
import com.browserstack.webdriver.testng.ManagedWebDriver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Anirudha Khanna
 */
public class WebDriverListener extends TestListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverListener.class);
    private static final String TEST_PASS_REASON = "Test Passed";
    private static final String TEST_PASS_STATUS = "passed";
    private static final String TEST_FAIL_STATUS = "failed";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onTestSuccess(ITestResult testResult) {
        super.onTestSuccess(testResult);
        WebDriver webDriver = getWebDriverFromParameters(testResult.getParameters());
        markAndCloseWebDriver(webDriver, TEST_PASS_STATUS, TEST_PASS_REASON);
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        super.onTestFailure(testResult);
        WebDriver webDriver = getWebDriverFromParameters(testResult.getParameters());
        markAndCloseWebDriver(webDriver, TEST_FAIL_STATUS, testResult.getThrowable().toString());
    }

    private WebDriver getWebDriverFromParameters(Object[] parameters) {
        Optional<Object> webDriverParam = Arrays.stream(parameters).filter(p -> p instanceof WebDriver).findFirst();
        WebDriver webDriver = (WebDriver) webDriverParam.orElse(null);
        if (webDriver == null) {
            Optional<Object> managedWebDriverParam = Arrays.stream(parameters).filter(p -> p instanceof ManagedWebDriver).findFirst();
            if (managedWebDriverParam.isPresent()) {
                webDriver = ((ManagedWebDriver) managedWebDriverParam.get()).getWebDriver();
            }
        }

        return webDriver;
    }


    private void markAndCloseWebDriver(WebDriver webDriver, String status, String reason) {
        if (webDriver == null) {
            return;
        }

        try {
            if (webDriver instanceof RemoteWebDriver && WebDriverFactory.getInstance().isCloudDriver()) {
                String script = createExecutorScript(status, reason);
                LOGGER.debug("Script to execute:: {}", script);
                if (StringUtils.isNotEmpty(script)) {
                    ((JavascriptExecutor) webDriver).executeScript(script);
                }
            }
        } finally {
            webDriver.quit();
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
