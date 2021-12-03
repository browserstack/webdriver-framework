package com.browserstack.webdriver.junit5.extensions;

import com.browserstack.webdriver.config.Platform;
import com.browserstack.webdriver.core.WebDriverFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

/**
 * {@code WebDriverParameterResolver} is an {@link org.junit.jupiter.api.extension.Extension Extension} of type  {@link org.junit.jupiter.api.extension.ParameterResolver ParameterResolver}.
 * {@code WebDriverParameterResolver} resolves parameter of type {@link org.openqa.selenium.WebDriver WebDriver} for {@link com.browserstack.webdriver.junit5.extensions.WebDriverTest WebDriverTest}.
 */
public class WebDriverParameterResolver implements ParameterResolver {

    public static final ExtensionContext.Namespace STORE_NAMESPACE =
            ExtensionContext.Namespace.create("com.browserstack.examples");
    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverParameterResolver.class);

    private final WebDriverFactory webDriverFactory;
    private final Platform platform;

    public WebDriverParameterResolver(WebDriverFactory webDriverFactory, Platform platform) {
        this.webDriverFactory = webDriverFactory;
        this.platform = platform;
    }

    /**
     * Determines if  {@code WebDriverParameterResolver} supports resolution of an argument.
     *
     * @param parameterContext the context for the parameter for which an argument should
     *                         be resolved; never {@code null}.
     * @param extensionContext the extension context for the {@code Method}
     *                         about to be invoked; never {@code null}.
     * @return {@code true} if the {@code parameter} is of type {@link WebDriver}.
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(WebDriver.class);
    }

    /**
     * Resolves an argument of type {@link WebDriver} in the supplied {@link ParameterContext}
     * for the supplied {@link ExtensionContext}.
     *
     * <p>This method is only called by the framework if {@link #supportsParameter}
     * previously returned {@code true} for the same {@link ParameterContext}
     * and {@link ExtensionContext}.
     *
     * @param parameterContext the context for the parameter for which an argument should
     *                         be resolved; never {@code null}
     * @param extensionContext the extension context for the {@code Executable}
     *                         about to be invoked; never {@code null}
     * @return the resolved argument for the {@code parameter} of type {@link WebDriver};
     * @throws ParameterResolutionException is thrown when {@link WebDriver} initialisation fails.
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        String testMethodName = extensionContext.getDisplayName();
        WebDriver webDriver = createWebDriver(testMethodName);
        if (webDriver == null) {
            throw new ParameterResolutionException("Unable to create WebDriver for Platform :: "
                    + this.platform.getName() + " method :: " + testMethodName);
        }
        extensionContext.getStore(STORE_NAMESPACE).put(testMethodName, webDriver);
        return webDriver;
    }

    private WebDriver createWebDriver(String testMethodName) {
        WebDriver webDriver;
        //TODO : Support for SpecificCapabilities
        // webDriver = this.webDriverFactory.createWebDriverForPlatform(platform, testMethodName, specificCapabilities);
        webDriver = this.webDriverFactory.createWebDriverForPlatform(platform, testMethodName);
        return webDriver;
    }

}