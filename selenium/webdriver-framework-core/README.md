# Webdriver Framework Core

Webdriver Framework Core is a loosely coupled module which incorporates the following:

* Parses the external configuration files
* Initialize webdriver instances based on the configuration
* Provides APIs to inject the Webdriver instances in your tests

## Parses the external configuration files

This repository allows you to use configuration files similar to the following to run your webdriver tests on various platforms including on-premise browsers, browsers running on a remote selenium grid such as [BrowserStack Automate](https://www.browserstack.com/automate) or in a [Docker container](https://github.com/SeleniumHQ/docker-selenium). 

```yml
testEndpoint: https://bstackdemo.com
namedTestUrls:
  url_one: https://www.google.com
  url_two: https://www.yahoo.com
#driverType: onPremDriver
driverType: cloudDriver
onPremDriver:
  platforms:
    - name: chrome
      driverPath: src/test/resources/chromedriver
    - name: safari
      driverPath: src/test/resources/safaridriver
cloudDriver:
  hubUrl: https://hub-cloud.browserstack.com/wd/hub
  user: BROWSERSTACK_USERNAME
  accessKey: BROWSERSTACK_ACCESSKEY
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
    - name: Win10_IE11
      os: Windows
      os_version: '10'
      browser: Internet Explorer
      browser_version: '11.0'
      capabilities:
        browserstack.ie.arch: x32
        browserstack.selenium_version: 3.141.59
    - name: Win10_Chrome_Latest-1
      os: Windows
      os_version: '10'
      browser: Chrome
      browser_version: latest-1
      capabilities:
        browserstack.selenium_version: 3.141.59
    - name: OSX_BigSur_Chrome_Latest
      os: OS X
      os_version: Big Sur
      browser: Chrome
      browser_version: latest
      capabilities:
        browserstack.selenium_version: 3.141.59
```

## Initialize webdriver instances based on the configuration

The [WebDriverFactory](src/main/java/com/browserstack/webdriver/core/WebDriverFactory.java) class looks for environment variable named "capabilities.config" which should be pointing to the file to be used. Further the class parses the file and initialises the [WebDriverConfiguration](src/main/java/com/browserstack/webdriver/config/WebDriverConfiguration.java) class

## Provides APIs to inject the Webdriver instances in your tests

Make use of `createWebDriverForPlatform` method inside your framework to inject the webdriver into your respective test instances.
