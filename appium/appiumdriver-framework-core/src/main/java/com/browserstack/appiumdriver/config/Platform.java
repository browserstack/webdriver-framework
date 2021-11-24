package com.browserstack.appiumdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;

public class Platform {

    private String name;

    private String os;

    @JsonProperty("os_version")
    private String osVersion;

    private String device;

    private Capabilities capabilities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        if(StringUtils.isEmpty(os)){
            throw new NullPointerException("OS is not defined. Please define 'os' in config file.");
        }
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

    public String getDevice() {
        if(StringUtils.isEmpty(device)){
            throw new NullPointerException("Device is not defined. Please define 'device' in config file.");
        }
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
