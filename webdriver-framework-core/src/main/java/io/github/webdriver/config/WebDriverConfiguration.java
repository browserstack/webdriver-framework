package io.github.webdriver.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WebDriverConfiguration {

    private String testEndpoint;

    private Map<String, String> namedTestUrls;

    private DriverType driverType;

    @JsonProperty("onPremDriver")
    private OnPremDriverConfig onPremDriverConfig;

    @JsonProperty("onPremGridDriver")
    private RemoteDriverConfig onPremGridDriverConfig;

    @JsonProperty("cloudDriver")
    private RemoteDriverConfig cloudDriverConfig;

    public List<Platform> getActivePlatforms() {
        List<Platform> activePlatforms = Collections.emptyList();
        switch (driverType) {
            case onPremDriver:
                activePlatforms = onPremDriverConfig.getPlatforms();
                break;
            case onPremGridDriver:
                activePlatforms = onPremGridDriverConfig.getPlatforms();
                break;
            case cloudDriver:
                activePlatforms = cloudDriverConfig.getPlatforms();
                break;
        }
        return activePlatforms;
    }

    public String getTestEndpoint() {
        return testEndpoint;
    }

    public void setTestEndpoint(String testEndpoint) {
        this.testEndpoint = testEndpoint;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }

    public OnPremDriverConfig getOnPremDriverConfig() {
        return onPremDriverConfig;
    }

    public void setOnPremDriverConfig(OnPremDriverConfig onPremDriverConfig) {
        this.onPremDriverConfig = onPremDriverConfig;
    }

    public RemoteDriverConfig getOnPremGridDriverConfig() {
        return onPremGridDriverConfig;
    }

    public void setOnPremGridDriverConfig(RemoteDriverConfig onPremGridDriverConfig) {
        this.onPremGridDriverConfig = onPremGridDriverConfig;
    }

    public RemoteDriverConfig getCloudDriverConfig() {
        return cloudDriverConfig;
    }

    public void setCloudDriverConfig(RemoteDriverConfig cloudDriverConfig) {
        this.cloudDriverConfig = cloudDriverConfig;
    }

    @JsonAnySetter
    public void setNamedTestUrls(String key, String value) {
        this.namedTestUrls.put(key, value);
    }

    public Map<String, String> getNamedTestUrls() {
        return this.namedTestUrls;
    }

    public String getNamedUrl(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        return this.namedTestUrls.get(name);
    }
}
