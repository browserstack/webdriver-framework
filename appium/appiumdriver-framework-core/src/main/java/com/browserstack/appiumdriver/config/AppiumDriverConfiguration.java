package com.browserstack.appiumdriver.config;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AppiumDriverConfiguration {

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
}
