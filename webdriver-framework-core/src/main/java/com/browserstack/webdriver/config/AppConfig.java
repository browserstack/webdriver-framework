package com.browserstack.webdriver.config;

import org.apache.commons.lang3.StringUtils;

public class AppConfig {
    private String ios_hash_id;
    private String android_hash_id;
    private String ios_custom_id;
    private String android_custom_id;
    private String ios_path;
    private String android_path;

    public String getApp(String os, DriverType driverType) {
        if (driverType == DriverType.cloudDriver) {
            String hashId = this.getHashId(os);
            String customId = this.getCustomId(os);
            return StringUtils.isEmpty(hashId) ? customId : hashId;
        } else {
            return this.getAppPath(os.toLowerCase());
        }
    }

    private String getHashId(String os) {
        switch (os) {
        case "android":
            return android_hash_id;
        case "ios":
            return ios_hash_id;
        default:
            throw new RuntimeException("Unsupported Operating System : " + os);
        }
    }

    private String getCustomId(String os) {
        switch (os) {
        case "android":
            return android_custom_id;
        case "ios":
            return ios_custom_id;
        default:
            throw new RuntimeException("Unsupported Operating System : " + os);
        }
    }

    private String getAppPath(String os) {
        switch (os) {
        case "android":
            return android_path;
        case "ios":
            return ios_path;
        default:
            throw new RuntimeException("Unsupported Operating System : " + os);
        }
    }
}
