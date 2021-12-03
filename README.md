<h1 align="center"> <a href="/">Webdriver Framework</a> </h1>

This is a framework designed to manage Webdrivers efficiently.
Selenium-based tests make extensive use of Webdriver APIs. Hence, Webdriver is a dependency for all Selenium-based test suites.There are no good dependency management frameworks for Selenium Webdrivers in Java. 

A good dependency injection framework should be able to isolate the creation of WebDrivers from the test code. It should also be able to externalise all the configuration (selenium capabilities) required for the WebDriver initialization. It should be flexible enough to support various test environments including on premise execution or execution on a selenium grid such as BrowserStack Automate.


### :pushpin: [Webdriver Framework Core](/selenium/webdriver-framework-core)
Webdriver Framework Core is a loosely coupled module which incorporates the following:
* Parses the external configuration files
* Initialize webdriver instances based on the configuration
* Provides APIs to inject the Webdriver instances in your tests


### :pushpin: [Webdriver Framework TestNG](/selenium/webdriver-framework-testng)
Webdriver Framework TestNG is a module built on top of *Webdriver Framework Core* to facilitate webdriver injection into TestNG based tests. It also manages the webdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Webdriver instance


### :pushpin: [Webdriver Framework JUnit4](/selenium/webdriver-framework-junit4)
Webdriver Framework JUnit4 is a module built on top of *Webdriver Framework Core* to facilitate webdriver injection into JUnit4 based tests. It also manages the webdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Webdriver instance


### :pushpin: [Webdriver Framework JUnit5](/selenium/webdriver-framework-junit5)
Webdriver Framework JUnit5 is a module built on top of *Webdriver Framework Core* to facilitate webdriver injection into JUnit5 based tests. It also manages the webdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Webdriver instance

<h1 align="center"> <a href="/">Appiumdriver Framework</a> </h1>

This is a framework designed to manage Appiumdrivers efficiently.
Similar to the webdriver-framework, appiumdriver-framework helps to manager lifecycle of an appium based driver that is used for mobile automation.


### :pushpin: [Appiumdriver Framework Core](/appium/appiumdriver-framework-core)
Appiumdriver Framework Core is a loosely coupled module which incorporates the following:
* Parses the external configuration files
* Initialize appiumdriver instances based on the configuration
* Provides APIs to inject the Appiumdriver instances in your tests


### :pushpin: [Appiumdriver Framework TestNG](/appium/appiumdriver-framework-testng)
Appiumdriver Framework TestNG is a module built on top of *Appiumdriver Framework Core* to facilitate appiumdriver injection into TestNG based tests. It also manages the appiumdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Appiumdriver instance


### :pushpin: [Appiumdriver Framework JUnit4](/appium/appiumdriver-framework-junit4)
Appiumdriver Framework JUnit4 is a module built on top of *Appiumdriver Framework Core* to facilitate appiumdriver injection into JUnit4 based tests. It also manages the appiumdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Appiumdriver instance


### :pushpin: [Appiumdriver Framework JUnit5](/appium/appiumdriver-framework-junit5)
Appiumdriver Framework JUnit5 is a module built on top of *Appiumdriver Framework Core* to facilitate appiumdriver injection into JUnit5 based tests. It also manages the appiumdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Appiumdriver instance

