package com.browserstack.webdriver.junit4;

import java.util.ArrayList;
import java.util.List;
import com.browserstack.webdriver.core.WebDriverFactory;
import org.junit.Rule;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.browserstack.webdriver.config.Platform;

public abstract class AbstractWebDriverTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebDriverTest.class);


    @Parameterized.Parameter(0)
    public Platform platform;

    @Rule
    public WebDriverProviderRule webDriverProviderRule = new WebDriverProviderRule();

    @Parameterized.Parameters

    public static Iterable<Object[]> data() {
        WebDriverFactory webDriverFactory = WebDriverFactory.getInstance();
        List<Object[]> returnData = new ArrayList<>();
        List<Platform> platforms = webDriverFactory.getPlatforms();
        LOGGER.debug("Running tests on {} active platforms.", platforms.size());
        platforms.forEach(platform -> {
            returnData.add(new Object[]{platform});
        });
        return returnData;
    }

    public String getTestEndpoint() {
        return WebDriverFactory.getInstance().getTestEndpoint();
    }
}
