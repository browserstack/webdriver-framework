package com.browserstack.webdriver.junit5.extensions;

import com.browserstack.webdriver.config.DriverType;
import com.browserstack.webdriver.core.WebDriverFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

/**
 * {@code WebDriverTestWatcher} is an {@link org.junit.jupiter.api.extension.Extension Extension} of type  {@link org.junit.jupiter.api.extension.TestWatcher TestWatcher}.
 * <p>{@code WebDriverTestWatcher} methods are called after a test has been skipped or executed.</p>
 */
public class WebDriverTestWatcher implements TestWatcher {

    private static final String TEST_STATUS_SCRIPT = "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"%s\", \"reason\": \"%s\"}}";

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
        markAndCloseWebDriver(context, "passed", "Test passed");
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
        markAndCloseWebDriver(context, "failed", cause.getMessage());
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
        markAndCloseWebDriver(context, "failed", cause.getMessage());
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
                ((JavascriptExecutor) webDriver).executeScript(String.format(TEST_STATUS_SCRIPT, status, reason));
            }
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }
}