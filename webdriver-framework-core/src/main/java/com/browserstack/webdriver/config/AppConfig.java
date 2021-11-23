package com.browserstack.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;

public class AppConfig {
    @JsonProperty("ios_hash_id")
    private String iosHashId;
    @JsonProperty("android_hash_id")
    private String androidHashId;
    @JsonProperty("ios_custom_id")
    private String iosCustomId;
    @JsonProperty("android_custom_id")
    private String androidCustomId;
    @JsonProperty("ios_path")
    private String iosPath;
    @JsonProperty("android_path")
    private String androidPath;

    public String getApp(String os, DriverType driverType) {
        if (driverType == DriverType.cloudDriver) {
            String hashId = this.getHashId(os.toLowerCase());
            String customId = this.getCustomId(os.toLowerCase());
            return StringUtils.isEmpty(hashId) ? customId : hashId;
        } else {
            return this.getAppPath(os.toLowerCase());
        }
    }

    private String getHashId(String os) {
        switch (DeviceType.valueOf(os.toUpperCase())) {
        case ANDROID:
            return androidHashId;
        case IOS:
            return iosHashId;
        default:
            throw new RuntimeException("Unsupported Operating System : " + os);
        }
    }

    private String getCustomId(String os) {
        switch (DeviceType.valueOf(os.toUpperCase())) {
        case ANDROID:
            return androidCustomId;
        case IOS:
            return iosCustomId;
        default:
            throw new RuntimeException("Unsupported Operating System : " + os);
        }
    }

    private String getAppPath(String os) {
        switch (DeviceType.valueOf(os.toUpperCase())) {
        case ANDROID:
            return androidPath;
        case IOS:
            return iosPath;
        default:
            throw new RuntimeException("Unsupported Operating System : " + os);
        }
    }
}
