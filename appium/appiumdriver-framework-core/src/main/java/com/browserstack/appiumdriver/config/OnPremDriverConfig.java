package com.browserstack.appiumdriver.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OnPremDriverConfig {

    private List<Platform> platforms;

    @JsonProperty("app")
    private AppConfig app;

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public AppConfig getAppConfig() {
        return app;
    }
}
