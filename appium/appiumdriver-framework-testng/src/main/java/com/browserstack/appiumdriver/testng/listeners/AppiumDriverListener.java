package com.browserstack.appiumdriver.testng.listeners;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import io.appium.java_client.AppiumDriver;

import com.browserstack.appiumdriver.core.AppiumDriverFactory;
import com.browserstack.appiumdriver.testng.ManagedAppiumDriver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Anirudha Khanna
 */
public class AppiumDriverListener extends TestListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumDriverListener.class);
    private static final String TEST_PASS_REASON = "Test Passed";
    private static final String TEST_PASS_STATUS = "passed";
    private static final String TEST_FAIL_STATUS = "failed";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onTestSuccess(ITestResult testResult) {
        super.onTestSuccess(testResult);
        AppiumDriver<?> appiumDriver = getAppiumDriverFromParameters(testResult.getParameters());
        markAndCloseAppiumDriver(appiumDriver, TEST_PASS_STATUS, TEST_PASS_REASON);
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        super.onTestFailure(testResult);
        AppiumDriver<?> appiumDriver = getAppiumDriverFromParameters(testResult.getParameters());
        markAndCloseAppiumDriver(appiumDriver, TEST_FAIL_STATUS, testResult.getThrowable().toString());
    }

    private AppiumDriver<?> getAppiumDriverFromParameters(Object[] parameters) {
        Optional<Object> appiumDriverParam = Arrays.stream(parameters).filter(p -> p instanceof AppiumDriver).findFirst();
        AppiumDriver<?> appiumDriver = (AppiumDriver<?>) appiumDriverParam.orElse(null);
        if (appiumDriver == null) {
            Optional<Object> managedAppiumDriverParam = Arrays.stream(parameters).filter(p -> p instanceof ManagedAppiumDriver).findFirst();
            if (managedAppiumDriverParam.isPresent()) {
                appiumDriver = ((ManagedAppiumDriver) managedAppiumDriverParam.get()).getAppiumDriver();
            }
        }

        return appiumDriver;
    }


    private void markAndCloseAppiumDriver(AppiumDriver<?> appiumDriver, String status, String reason) {
        if (appiumDriver == null) {
            return;
        }

        try {
            if (appiumDriver instanceof AppiumDriver && AppiumDriverFactory.getInstance().isCloudDriver()) {
                String script = createExecutorScript(status, reason);
                LOGGER.debug("Script to execute:: {}", script);
                if (StringUtils.isNotEmpty(script)) {
                    ((JavascriptExecutor) appiumDriver).executeScript(script);
                }
            }
        } finally {
            appiumDriver.quit();
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
