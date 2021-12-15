package com.browserstack.appiumdriver.core;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.browserstack.appiumdriver.config.AppConfig;
import com.browserstack.appiumdriver.config.Platform;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.remote.SessionId;

import io.appium.java_client.AppiumDriver;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Anirudha Khanna
 */
public class AppiumDriverFactoryTest {

    AppiumDriverFactory factory;
    @Before
    public void setup(){
        System.setProperty("capabilities.config", "appiumdriver-config.yml");
       factory = AppiumDriverFactory.getInstance();
    }
    @Test
    public void platformSize() throws Exception {
       assertEquals(factory.getPlatforms().size(), 2);
    }
    @Test
    public void androidAppPresent() throws Exception {
       Platform android = factory.getPlatforms().get(1);
       AppConfig config = factory.getAppConfiguration();
       assertEquals(config.getApp(android.getOs(), factory.getDriverType()), "DemoApp");
    }
    @Test
    public void iosAppPresent() throws Exception {
       Platform ios = factory.getPlatforms().get(0);
       AppConfig config = factory.getAppConfiguration();
       assertEquals(config.getApp(ios.getOs(), factory.getDriverType()), "bs://395666525eaa9c4156a746db0e95d5f17c300305");
    }
    @Test @Ignore
    public void cloudDriverCreation() throws Exception {
        // You need to set browserstack credentials as environment variables before running this
       Platform platform = factory.getPlatforms().get(1);
       AppiumDriver<?> driver =  factory.createAppiumDriverForPlatform(platform, "Test");
       SessionId sessionID = driver.getSessionId();
       driver.quit();
       assertTrue(sessionID != null);
    }
}
