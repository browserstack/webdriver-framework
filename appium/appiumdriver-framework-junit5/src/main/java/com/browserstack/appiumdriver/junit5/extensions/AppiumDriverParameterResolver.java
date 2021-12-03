package com.browserstack.appiumdriver.junit5.extensions;

import com.browserstack.appiumdriver.config.Platform;
import com.browserstack.appiumdriver.core.AppiumDriverFactory;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.appium.java_client.AppiumDriver;


/**
 * {@code AppiumDriverParameterResolver} is an {@link org.junit.jupiter.api.extension.Extension Extension} of type  {@link org.junit.jupiter.api.extension.ParameterResolver ParameterResolver}.
 * {@code AppiumDriverParameterResolver} resolves parameter of type {@link io.appium.java_client.AppiumDriver AppiumDriver} for {@link com.browserstack.appiumdriver.junit5.extensions.AppiumDriverTest AppiumDriverTest}.
 */
public class AppiumDriverParameterResolver implements ParameterResolver {

    public static final ExtensionContext.Namespace STORE_NAMESPACE =
            ExtensionContext.Namespace.create("com.browserstack.examples");


    private final AppiumDriverFactory appiumDriverFactory;
    private final Platform platform;

    public AppiumDriverParameterResolver(AppiumDriverFactory appiumDriverFactory, Platform platform) {
        this.appiumDriverFactory = appiumDriverFactory;
        this.platform = platform;
    }

    /**
     * Determines if  {@code AppiumDriverParameterResolver} supports resolution of an argument.
     *
     * @param parameterContext the context for the parameter for which an argument should
     *                         be resolved; never {@code null}.
     * @param extensionContext the extension context for the {@code Method}
     *                         about to be invoked; never {@code null}.
     * @return {@code true} if the {@code parameter} is of type {@link AppiumDriver}.
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(AppiumDriver.class);
    }

    /**
     * Resolves an argument of type {@link AppiumDriver} in the supplied {@link ParameterContext}
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
     * @return the resolved argument for the {@code parameter} of type {@link AppiumDriver};
     * @throws ParameterResolutionException is thrown when {@link AppiumDriver} initialisation fails.
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        String testMethodName = extensionContext.getDisplayName();
        AppiumDriver<?> appiumDriver = createAppiumDriver(testMethodName);
        if (appiumDriver == null) {
            throw new ParameterResolutionException("Unable to create AppiumDriver for Platform :: "
                    + this.platform.getName() + " method :: " + testMethodName);
        }
        extensionContext.getStore(STORE_NAMESPACE).put(testMethodName, appiumDriver);
        return appiumDriver;
    }

    private AppiumDriver<?> createAppiumDriver(String testMethodName) {
        AppiumDriver<?> appiumDriver;
        //TODO : Support for SpecificCapabilities
        // appiumDriver = this.appiumDriverFactory.createAppiumDriverForPlatform(platform, testMethodName, specificCapabilities);
        appiumDriver = this.appiumDriverFactory.createAppiumDriverForPlatform(platform, testMethodName);
        return appiumDriver;
    }

}