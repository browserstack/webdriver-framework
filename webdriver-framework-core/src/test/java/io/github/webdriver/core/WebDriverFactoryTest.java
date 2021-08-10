package io.github.webdriver.core;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.browserstack.webdriver.core.WebDriverFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Anirudha Khanna
 */
public class WebDriverFactoryTest {

    @Test
    public void testName() throws Exception {
        /* =================== Prepare ================= */
        System.setProperty(WebDriverFactory.CAPABILITIES_FILE_PROP, "webdriver-config.yml");

        /* =================== Execute ================= */
        Map<String, String> additionalUrls = WebDriverFactory.getInstance().getNamedTestUrls();

        /* =================== Verify ================= */
        Assert.assertTrue(additionalUrls.size() > 0);
    }
}
