package com.browserstack.appiumdriver.junit5.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.platform.commons.util.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.browserstack.appiumdriver.config.Platform;
import com.browserstack.appiumdriver.core.AppiumDriverFactory;

/**
 * {@code AppiumDriverTestExtension} is an {@link org.junit.jupiter.api.extension.Extension Extension} of type  {@link org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider TestTemplateInvocationContextProvider}.
 * <p>{@code AppiumDriverTestExtension} makes it possible to execute a test template on
 * different platforms, by preparing the {@link AppiumDriverTestInvocationContext} instances for each platform.</p>
 */
public class AppiumDriverTestExtension implements TestTemplateInvocationContextProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumDriverTestExtension.class);

    /**
     * Determines if  {@code AppiumDriverTestExtension} supports providing the required invocation contexts.
     *
     * @param context the extension context for the test template method about
     *                to be invoked; never {@code null}
     * @return {@code true} if the {@code test} is of type {@link AppiumDriverTest}.
     */
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        if (!context.getTestMethod().isPresent()) {
            return false;
        }
        Method testMethod = context.getTestMethod().get();
        LOGGER.debug("Supports Test Template on Method :: {}", testMethod.getName());
        return AnnotationUtils.isAnnotated(testMethod, AppiumDriverTest.class);
    }

    /**
     * Provides {@linkplain AppiumDriverTestInvocationContext AppiumDriverTestInvocationContext}
     * for the  {@link AppiumDriverTest} methods in {@code context} for each active platform.
     *
     * <p>This method is only called by the framework if {@link #supportsTestTemplate}
     * previously returned {@code true} for the same {@link ExtensionContext}.</p>
     *
     * @param context the extension context for the test template method about
     *                to be invoked; never {@code null}
     * @return a {@code Stream} of {@code AppiumDriverTestInvocationContext}
     * instances for the invocation of the test method; never {@code null}
     */
    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        String testMethodName = context.getRequiredTestMethod().getName();
        final List<TestTemplateInvocationContext> appiumDriverTestInvocationContexts = new ArrayList<>();
        final AppiumDriverFactory appiumDriverFactory = AppiumDriverFactory.getInstance();
        List<Platform> platforms = appiumDriverFactory.getPlatforms();
        platforms.forEach(p -> appiumDriverTestInvocationContexts.add(new AppiumDriverTestInvocationContext(testMethodName, appiumDriverFactory, p)));
        return appiumDriverTestInvocationContexts.stream();
    }
}