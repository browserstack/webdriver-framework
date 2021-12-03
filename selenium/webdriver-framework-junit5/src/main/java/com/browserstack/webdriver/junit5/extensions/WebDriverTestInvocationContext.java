package com.browserstack.webdriver.junit5.extensions;

import com.browserstack.webdriver.config.Platform;
import com.browserstack.webdriver.core.WebDriverFactory;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code WebDriverTestInvocationContext} represents the <em>context</em> of a
 * single invocation of a {@linkplain WebDriverTest}.
 * <p>{@code WebDriverTestInvocationContext} instances are created by {@link WebDriverTestExtension}
 * for each platform.
 */
public class WebDriverTestInvocationContext implements TestTemplateInvocationContext {

    private final String methodName;
    private final Platform platform;
    private final WebDriverFactory webDriverFactory;

    public WebDriverTestInvocationContext(String methodName, WebDriverFactory webDriverFactory, Platform platform) {
        this.methodName = methodName;
        this.webDriverFactory = webDriverFactory;
        this.platform = platform;
    }

    /**
     * Get the display name for this invocation.
     *
     * <p>The supplied {@code invocationIndex} is incremented by the framework
     * with each test invocation. Thus, in the case of multiple active instances created from
     * {@linkplain WebDriverTestExtension WebDriverTestExtension}, only the
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
     * <p>{@code WebDriverParameterResolver} which resolves the {@link org.openqa.selenium.WebDriver WebDriver} parameter  on the specified {@code platform}.</p>
     * <p> {@code WebDriverTestWatcher} which listens to the test results and take appropriate actions.</p>
     */
    @Override
    public List<Extension> getAdditionalExtensions() {
        List<Extension> additionalExtensions = new ArrayList<>();
        additionalExtensions.add(new WebDriverParameterResolver(this.webDriverFactory, this.platform));
        additionalExtensions.add(new WebDriverTestWatcher());
       // additionalExtensions.add(new AllureListener(platform));
        return additionalExtensions;
    }
}