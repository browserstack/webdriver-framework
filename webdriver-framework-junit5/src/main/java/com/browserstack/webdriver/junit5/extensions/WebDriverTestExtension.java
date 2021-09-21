package com.browserstack.webdriver.junit5.extensions;

import com.browserstack.webdriver.config.Platform;
import com.browserstack.webdriver.core.WebDriverFactory;
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

/**
 * {@code WebDriverTestExtension} is an {@link org.junit.jupiter.api.extension.Extension Extension} of type  {@link org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider TestTemplateInvocationContextProvider}.
 * <p>{@code WebDriverTestExtension} makes it possible to execute a test template on
 * different platforms, by preparing the {@link WebDriverTestInvocationContext} instances for each platform.</p>
 */
public class WebDriverTestExtension implements TestTemplateInvocationContextProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverTestExtension.class);

    /**
     * Determines if  {@code WebDriverTestExtension} supports providing the required invocation contexts.
     *
     * @param context the extension context for the test template method about
     *                to be invoked; never {@code null}
     * @return {@code true} if the {@code test} is of type {@link WebDriverTest}.
     */
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        if (!context.getTestMethod().isPresent()) {
            return false;
        }
        Method testMethod = context.getTestMethod().get();
        LOGGER.debug("Supports Test Template on Method :: {}", testMethod.getName());
        return AnnotationUtils.isAnnotated(testMethod, WebDriverTest.class);
    }

    /**
     * Provides {@linkplain WebDriverTestInvocationContext WebDriverTestInvocationContext}
     * for the  {@link WebDriverTest} methods in {@code context} for each active platform.
     *
     * <p>This method is only called by the framework if {@link #supportsTestTemplate}
     * previously returned {@code true} for the same {@link ExtensionContext}.</p>
     *
     * @param context the extension context for the test template method about
     *                to be invoked; never {@code null}
     * @return a {@code Stream} of {@code WebDriverTestInvocationContext}
     * instances for the invocation of the test method; never {@code null}
     */
    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        String testMethodName = context.getRequiredTestMethod().getName();
        final List<TestTemplateInvocationContext> webDriverTestInvocationContexts = new ArrayList<>();
        final WebDriverFactory webDriverFactory = WebDriverFactory.getInstance();
        List<Platform> platforms = webDriverFactory.getPlatforms();
        platforms.forEach(p -> webDriverTestInvocationContexts.add(new WebDriverTestInvocationContext(testMethodName, webDriverFactory, p)));
        return webDriverTestInvocationContexts.stream();
    }
}