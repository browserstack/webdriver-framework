package com.browserstack.webdriver.junit5.extensions;

import com.browserstack.webdriver.config.DriverType;
import com.browserstack.webdriver.core.WebDriverFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * {@code WebDriverTestWatcher} is an {@link org.junit.jupiter.api.extension.Extension Extension} of type  {@link org.junit.jupiter.api.extension.TestWatcher TestWatcher}.
 * <p>{@code WebDriverTestWatcher} methods are called after a test has been skipped or executed.</p>
 */
public class WebDriverTestWatcher implements TestWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverTestWatcher.class);
    private static final String TEST_PASS_REASON = "Test Passed";
    private static final String TEST_PASS_STATUS = "passed";
    private static final String TEST_FAIL_STATUS = "failed";

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Invoked after a disabled test has been skipped.
     *
     * @param context the current extension context; never {@code null}
     * @param reason  the reason the test is disabled; never {@code null} but
     *                potentially <em>empty</em>
     */
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        TestWatcher.super.testDisabled(context, reason);
    }

    /**
     * Invoked after a test has completed successfully.
     *
     * <p>This method calls {@link #markAndCloseWebDriver} to quit the driver and mark the test as passed.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void testSuccessful(ExtensionContext context) {
        markAndCloseWebDriver(context, TEST_PASS_STATUS, TEST_PASS_REASON);
    }

    /**
     * Invoked after after a test has been aborted.
     *
     * <p>This method calls {@link #markAndCloseWebDriver} to quit the driver and mark the test as failed.
     *
     * @param context the current extension context; never {@code null}
     * @param cause   the throwable responsible for the test being aborted; may be {@code null}
     */
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        markAndCloseWebDriver(context, TEST_FAIL_STATUS, cause.getMessage());
    }

    /**
     * Invoked after a test has failed.
     *
     * <p>This method calls {@link #markAndCloseWebDriver} to quit the driver and mark the test as failed.
     *
     * @param context the current extension context; never {@code null}
     * @param cause   the throwable that caused test failure; may be {@code null}
     */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        markAndCloseWebDriver(context, TEST_FAIL_STATUS, cause.getMessage());
    }

    /**
     * Invoked by other methods to quit driver and mark test statuses.
     *
     * @param context the current extension context; never {@code null}
     * @param status  the status of the test; never be {@code null}
     * @param reason  the reason that caused test status; never be {@code null}
     */
    private void markAndCloseWebDriver(ExtensionContext context, String status, String reason) {
        String testName = context.getDisplayName();
        WebDriver webDriver = context.getStore(WebDriverParameterResolver.STORE_NAMESPACE).get(testName, WebDriver.class);
        try {
            if (WebDriverFactory.getInstance().getDriverType().equals(DriverType.cloudDriver)) {
                String script = createExecutorScript(status, reason);
                LOGGER.debug("Script to execute:: {}", script);
                if (StringUtils.isNotEmpty(script)) {
                    ((JavascriptExecutor) webDriver).executeScript(script);
                }
            }
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
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
        // Replacing all the special characters with whitespace
        reason.replaceAll("^[^a-zA-Z0-9]"," ");

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