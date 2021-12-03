package com.browserstack.appiumdriver.junit5.extensions;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import java.util.ArrayList;
import java.util.List;

import com.browserstack.appiumdriver.config.Platform;
import com.browserstack.appiumdriver.core.AppiumDriverFactory;

/**
 * {@code AppiumDriverTestInvocationContext} represents the <em>context</em> of a
 * single invocation of a {@linkplain AppiumDriverTest}.
 * <p>{@code AppiumDriverTestInvocationContext} instances are created by {@link AppiumDriverTestExtension}
 * for each platform.
 */
public class AppiumDriverTestInvocationContext implements TestTemplateInvocationContext {

    private final String methodName;
    private final Platform platform;
    private final AppiumDriverFactory appiumDriverFactory;

    public AppiumDriverTestInvocationContext(String methodName, AppiumDriverFactory appiumDriverFactory, Platform platform) {
        this.methodName = methodName;
        this.appiumDriverFactory = appiumDriverFactory;
        this.platform = platform;
    }

    /**
     * Get the display name for this invocation.
     *
     * <p>The supplied {@code invocationIndex} is incremented by the framework
     * with each test invocation. Thus, in the case of multiple active instances created from
     * {@linkplain AppiumDriverTestExtension AppiumDriverTestExtension}, only the
     * first active provider receives indices starting with {@code 1}.
     *
     * @param invocationIndex the index of this invocation (1-based).
     * @return the display name for this invocation; never {@code null} or blank
     */
    @Override
    public String getDisplayName(int invocationIndex) {
        return methodName + "[" + invocationIndex + "]";
    }

    /**
     * Get the additional {@linkplain Extension extensions} for this invocation.
     *
     * <p>The extensions provided by this method will only be used for this
     * invocation of the test template.
     *
     * @return <p>This implementation returns a list containing -
     * <p>{@code AppiumDriverParameterResolver} which resolves the {@link io.appium.java_client.AppiumDriver AppiumDriver} parameter  on the specified {@code platform}.</p>
     * <p> {@code AppiumDriverTestWatcher} which listens to the test results and take appropriate actions.</p>
     */
    @Override
    public List<Extension> getAdditionalExtensions() {
        List<Extension> additionalExtensions = new ArrayList<>();
        additionalExtensions.add(new AppiumDriverParameterResolver(this.appiumDriverFactory, this.platform));
        additionalExtensions.add(new AppiumDriverTestWatcher());
       // additionalExtensions.add(new AllureListener(platform));
        return additionalExtensions;
    }
}