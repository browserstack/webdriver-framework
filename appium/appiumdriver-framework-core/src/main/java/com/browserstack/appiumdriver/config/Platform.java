package com.browserstack.appiumdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Platform {

    private String name;

    private String os;

    @JsonProperty("os_version")
    private String osVersion;

    private String browser;

    @JsonProperty("browser_version")
    private String browserVersion;

    private String device;

    @JsonProperty("real_mobile")
    private Boolean realMobile;

    private Capabilities capabilities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }
}
