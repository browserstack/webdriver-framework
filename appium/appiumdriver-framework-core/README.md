# AppiumDriver Framework Core

AppiumDriver Framework Core is a loosely coupled module which incorporates the following:

* Parses the external configuration files
* Initialize appiumdriver instances based on the configuration
* Provides APIs to inject the AppiumDriver instances in your tests

## Parses the external configuration files

This repository allows you to use configuration files similar to the following to run your appiumdriver tests on various platforms including on-premise browsers, browsers running on a remote selenium grid such as [BrowserStack Automate](https://www.browserstack.com/automate) or in a [Docker container](https://github.com/SeleniumHQ/docker-selenium). 

```yml

driverType: cloudDriver

onPremDriver:
  app:
    ios_path: /path/to/ipa
    android_path: /path/to/apk
  platforms:
    - name:
      os: Android,
      os_version: 11.0
      device: Google Pixel 4
      capabilities:
        appPackage: com.browserstack.sampleapp
        appActivity: com.browserstack.sampleapp.MainActivity
    - name:
      os: IOS,
      os_version: 14
      device: iPhone 11
      capabilities:
        udid: <udid>

onPremGridDriver:
  hubUrl: http://localhost:4723/wd/hub
  app:
    ios_path: /path/to/ipa
    android_path: /path/to/apk
  platforms:
    - name:
      os: Android
      os_version: 11.0
      device: Google Pixel 4
      capabilities:
        appPackage: com.browserstack.sampleapp
        appActivity: com.browserstack.sampleapp.MainActivity
    - name:
      os: IOS,
      os_version: 14
      device: iPhone 11
      capabilities:
        udid: <udid> 
        
cloudDriver:
  hubUrl: https://hub-cloud.browserstack.com/wd/hub
  app:
    android_hash_id: <hash-id>
    ios_custom_id: <custom-id> | <custom-sharable-id>
  localTunnel:
    enabled: false
  common_capabilities:
    project: BrowserStack Demo Repository
    buildPrefix: browserstack-examples-testng
    capabilities:
      browserstack.debug: true
      browserstack.networkLogs: true
      browserstack.console: debug
  platforms:
    - name: IOS - iPhone 12
      os: iOS
      os_version: 14
      device: iPhone 12
      capabilities:
        browserstack.appium_version: 1.21.0
    - name: Android  - Pixel 3
      os: Android
      os_version: 10
      device: "Google Pixel 3"
      capabilities:
        browserstack.appium_version: 1.21.0

```

## Initialize Appium Driver instances based on the configuration

The [AppiumDriverFactory](src/main/java/com/browserstack/appiumdriver/core/AppiumDriverFactory.java) class looks for environment variable named "capabilities.config" which should be pointing to the file to be used. Further the class parses the file and initialises the [AppiumDriverConfiguration](src/main/java/com/browserstack/appiumdriver/config/AppiumDriverConfiguration.java) class

## Provides APIs to inject the AppiumDriver instances in your tests

Make use of `createAppiumDriverForPlatform` method inside your framework to inject the appiumdriver into your respective test instances.
