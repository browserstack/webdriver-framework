package com.browserstack.appiumdriver.junit4;

import java.util.ArrayList;
import java.util.List;

import com.browserstack.appiumdriver.config.Platform;
import com.browserstack.appiumdriver.core.AppiumDriverFactory;

import org.junit.Rule;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAppiumDriverTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAppiumDriverTest.class);


    @Parameterized.Parameter(0)
    public Platform platform;

    @Rule
    public AppiumDriverProviderRule appiumDriverProviderRule = new AppiumDriverProviderRule();

    @Parameterized.Parameters

    public static Iterable<Object[]> data() {
        AppiumDriverFactory appiumDriverFactory = AppiumDriverFactory.getInstance();
        List<Object[]> returnData = new ArrayList<>();
        List<Platform> platforms = appiumDriverFactory.getPlatforms();
        LOGGER.debug("Running tests on {} active platforms.", platforms.size());
        platforms.forEach(platform -> {
            returnData.add(new Object[]{platform});
        });
        return returnData;
    }
}
