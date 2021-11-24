package com.browserstack.appiumdriver.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import com.browserstack.appiumdriver.config.CommonCapabilities;
import com.browserstack.appiumdriver.config.DeviceType;
import com.browserstack.appiumdriver.config.DriverType;
import com.browserstack.appiumdriver.config.OnPremDriverConfig;
import com.browserstack.appiumdriver.config.Platform;
import com.browserstack.appiumdriver.config.RemoteDriverConfig;
import com.browserstack.appiumdriver.config.AppiumDriverConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class AppiumDriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumDriverFactory.class);
    protected static final String CAPABILITIES_FILE_PROP = "capabilities.config";
    private static final String DEFAULT_CAPABILITIES_FILE = "capabilities.yml";
    private static final String BROWSERSTACK_USERNAME = "BROWSERSTACK_USERNAME";
    private static final String BROWSERSTACK_ACCESS_KEY = "BROWSERSTACK_ACCESS_KEY";
    private static final String BUILD_ID = "BUILD_ID";
    private static final String DEFAULT_BUILD_NAME = "browserstack-examples-junit5";
    private static final String DEFAULT_BUILD_ENV_NAME = "BROWSERSTACK_BUILD_NAME";


    private static volatile AppiumDriverFactory instance;

    private final AppiumDriverConfiguration appiumDriverConfiguration;
    private final String defaultBuildSuffix;

    private AppiumDriverFactory() {
        this.defaultBuildSuffix = String.valueOf(System.currentTimeMillis());
        this.appiumDriverConfiguration = parseAppiumDriverConfig();
        List<Platform> platforms = appiumDriverConfiguration.getActivePlatforms();
        startLocalTunnel();
        LOGGER.debug("Running tests on {} active platforms.", platforms.size());
    }

    private boolean isLocalTunnelEnabled() {
        return appiumDriverConfiguration.getCloudDriverConfig() != null &&
                 appiumDriverConfiguration.getCloudDriverConfig().getLocalTunnel() != null &&
                 appiumDriverConfiguration.getCloudDriverConfig().getLocalTunnel().isEnabled() != null &&
                 appiumDriverConfiguration.getCloudDriverConfig().getLocalTunnel().isEnabled();
    }

    public static AppiumDriverFactory getInstance() {
        if (instance == null) {
            synchronized (AppiumDriverFactory.class) {
                if (instance == null) {
                    instance = new AppiumDriverFactory();
                }
            }
        }
        return instance;
    }

    private AppiumDriverConfiguration parseAppiumDriverConfig() {
        String capabilitiesConfigFile = System.getProperty(CAPABILITIES_FILE_PROP, DEFAULT_CAPABILITIES_FILE);
        LOGGER.debug("Using capabilities configuration from FILE :: {}", capabilitiesConfigFile);
        URL resourceURL = AppiumDriverFactory.class.getClassLoader().getResource(capabilitiesConfigFile);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        AppiumDriverConfiguration appiumDriverConfiguration;
        try {
            appiumDriverConfiguration = objectMapper.readValue(resourceURL, AppiumDriverConfiguration.class);
        } catch (IOException ioe) {
            throw new Error("Unable to parse capabilities file " + capabilitiesConfigFile, ioe);
        }
        return appiumDriverConfiguration;
    }

    private void startLocalTunnel() {
        if (isLocalTunnelEnabled()) {
            Map<String, String> localOptions = appiumDriverConfiguration.getCloudDriverConfig().getLocalTunnel().getLocalOptions();
            String accessKey = appiumDriverConfiguration.getCloudDriverConfig().getAccessKey();
            if (StringUtils.isNoneEmpty(System.getenv(BROWSERSTACK_ACCESS_KEY))) {
                accessKey = System.getenv(BROWSERSTACK_ACCESS_KEY);
            }
            localOptions.put("key", accessKey);
            LocalFactory.createInstance(appiumDriverConfiguration.getCloudDriverConfig().getLocalTunnel().getLocalOptions());
        }
    }

    public AppiumDriver<?> createAppiumDriverForPlatform(Platform platform, String testName) {

        try {
            AppiumDriver<?> appiumDriver = null;
            switch (this.appiumDriverConfiguration.getDriverType()) {
                case onPremDriver:
                    appiumDriver = createOnPremAppiumDriver(platform);
                    break;
                case onPremGridDriver:
                    appiumDriver = createOnPremGridAppiumDriver(platform);
                    break;
                case cloudDriver:
                    appiumDriver = createRemoteAppiumDriver(platform, testName);
            }
            return appiumDriver;
        } catch (MalformedURLException mae) {
            throw new Error("Unable to create AppiumDriver", mae);
        }
    }

    public DriverType getDriverType() {
        return appiumDriverConfiguration.getDriverType();
    }

    public boolean isCloudDriver() {
        return appiumDriverConfiguration.getDriverType() == DriverType.cloudDriver;
    }

    public List<Platform> getPlatforms() {
        return this.appiumDriverConfiguration.getActivePlatforms();
    }

    private AppiumDriver<?> createRemoteAppiumDriver(Platform platform, String testName) throws MalformedURLException {
        RemoteDriverConfig remoteDriverConfig = this.appiumDriverConfiguration.getCloudDriverConfig();
        CommonCapabilities commonCapabilities = remoteDriverConfig.getCommonCapabilities();
        DesiredCapabilities platformCapabilities = new DesiredCapabilities();
        String app = remoteDriverConfig.getAppConfig().getApp(platform.getOs(), this.getDriverType());
        if (commonCapabilities.getCapabilities() != null) {
            commonCapabilities.getCapabilities().getCapabilityMap().forEach(platformCapabilities::setCapability);
        }
        if (platform.getCapabilities() != null) {
            platform.getCapabilities().getCapabilityMap().forEach(platformCapabilities::setCapability);
        }
        platformCapabilities.setCapability("app", app);
        platformCapabilities.setCapability("os_version", platform.getOsVersion());
        platformCapabilities.setCapability("device", platform.getDevice());
        platformCapabilities.setCapability("name", testName);
        platformCapabilities.setCapability("project", commonCapabilities.getProject());
        platformCapabilities.setCapability("build", createBuildName(commonCapabilities.getBuildPrefix()));
        String user = remoteDriverConfig.getUser();
        if (StringUtils.isNoneEmpty(System.getenv(BROWSERSTACK_USERNAME))) {
            user = System.getenv(BROWSERSTACK_USERNAME);
        }
        String accessKey = remoteDriverConfig.getAccessKey();
        if (StringUtils.isNoneEmpty(System.getenv(BROWSERSTACK_ACCESS_KEY))) {
            accessKey = System.getenv(BROWSERSTACK_ACCESS_KEY);
        }
        platformCapabilities.setCapability("browserstack.user", user);
        platformCapabilities.setCapability("browserstack.key", accessKey);

        if (isLocalTunnelEnabled()) {
            platformCapabilities.setCapability("browserstack.localIdentifier",
                    LocalFactory.getInstance().getLocalIdentifier());
        }
        LOGGER.debug("Initialising RemoteAppiumDriver with capabilities : {}", platformCapabilities);
        return initializeDriverForMobilePlatform(platform, platformCapabilities);
    }

    private AppiumDriver<?> createOnPremGridAppiumDriver(Platform platform) throws MalformedURLException {
        RemoteDriverConfig remoteDriverConfig = this.appiumDriverConfiguration.getCloudDriverConfig();
        CommonCapabilities commonCapabilities = remoteDriverConfig.getCommonCapabilities();
        DesiredCapabilities platformCapabilities = new DesiredCapabilities();
        String app = remoteDriverConfig.getAppConfig().getApp(platform.getOs(), this.getDriverType());
        if (commonCapabilities.getCapabilities() != null) {
            commonCapabilities.getCapabilities().getCapabilityMap().forEach(platformCapabilities::setCapability);
        }
        if (platform.getCapabilities() != null) {
            platform.getCapabilities().getCapabilityMap().forEach(platformCapabilities::setCapability);
        }
        platformCapabilities.setCapability("app", app);
        platformCapabilities.setCapability("platformName", platform.getOs());
        platformCapabilities.setCapability("platformVersion", platform.getOsVersion());
        platformCapabilities.setCapability("deviceName", platform.getDevice());
        LOGGER.debug("Initialising RemoteAppiumDriver with capabilities : {}", platformCapabilities);
        return initializeDriverForMobilePlatform(platform, platformCapabilities);
    }

    private AppiumDriver<?> createOnPremAppiumDriver(Platform platform) throws MalformedURLException {
        DesiredCapabilities platformCapabilities = new DesiredCapabilities();
        OnPremDriverConfig onPremDriverConfig = this.appiumDriverConfiguration.getOnPremDriverConfig();
        String app = onPremDriverConfig.getAppConfig().getApp(platform.getOs(), this.getDriverType());
        if (platform.getCapabilities() != null) {
            platform.getCapabilities().getCapabilityMap().forEach(platformCapabilities::setCapability);
        }
        platformCapabilities.setCapability("app", app);
        platformCapabilities.setCapability("platformName", platform.getOs());
        platformCapabilities.setCapability("platformVersion", platform.getOsVersion());
        platformCapabilities.setCapability("deviceName", platform.getDevice());
        LOGGER.debug("Initialising AppiumDriver with capabilities : {}", platformCapabilities);
        return initializeDriverForMobilePlatform(platform, platformCapabilities);
    }

    private String createBuildName(String buildPrefix) {
        if(StringUtils.isNotEmpty(System.getenv(DEFAULT_BUILD_ENV_NAME))){
            return System.getenv(DEFAULT_BUILD_ENV_NAME);
        }
        if (StringUtils.isEmpty(buildPrefix)) {
            buildPrefix = DEFAULT_BUILD_NAME;
        }
        String buildName = buildPrefix;

        String buildSuffix = this.defaultBuildSuffix;
        if (StringUtils.isNotEmpty(System.getenv(BUILD_ID))) {
            buildSuffix = System.getenv(BUILD_ID);
        }
        return buildName + "-" + buildSuffix;
    }

    private AppiumDriver<?> initializeDriverForMobilePlatform(Platform platform, DesiredCapabilities platformCapabilities)
            throws MalformedURLException {
        String hubUrl = "";
        if (this.appiumDriverConfiguration.getDriverType() == DriverType.cloudDriver) {
            hubUrl = this.appiumDriverConfiguration.getCloudDriverConfig().getHubUrl();
        } else if (this.appiumDriverConfiguration.getDriverType() == DriverType.onPremGridDriver) {
            hubUrl = this.appiumDriverConfiguration.getOnPremGridDriverConfig().getHubUrl();
        }
        switch (DeviceType.valueOf(platform.getOs().toUpperCase())) {
        case ANDROID:
            return StringUtils.isEmpty(hubUrl) ? new AndroidDriver<>(platformCapabilities)
                    : new AndroidDriver<>(new URL(hubUrl), platformCapabilities);
        case IOS:
            return StringUtils.isEmpty(hubUrl) ? new IOSDriver<>(platformCapabilities)
                    : new IOSDriver<>(new URL(hubUrl), platformCapabilities);
        default:
            throw new RuntimeException("Unsupported Operating System : " + platform.getOs());
        }
    }
}
