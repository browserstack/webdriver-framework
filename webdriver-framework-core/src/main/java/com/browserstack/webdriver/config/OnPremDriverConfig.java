package com.browserstack.webdriver.config;

import java.util.List;

public class OnPremDriverConfig {

    private List<Platform> platforms;
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
