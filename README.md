<h1 align="center"> <a href="/">AppiumDriver Framework</a> </h1>

This is a framework designed to manage Webdrivers efficiently.
Selenium-based tests make extensive use of AppiumDriver APIs. Hence, AppiumDriver is a dependency for all Selenium-based test suites.There are no good dependency management frameworks for Selenium Webdrivers in Java. 

A good dependency injection framework should be able to isolate the creation of WebDrivers from the test code. It should also be able to externalise all the configuration (selenium capabilities) required for the AppiumDriver initialization. It should be flexible enough to support various test environments including on premise execution or execution on a selenium grid such as BrowserStack Automate.


### :pushpin: [AppiumDriver Framework Core](/appiumdriver-framework-core)
AppiumDriver Framework Core is a loosely coupled module which incorporates the following:
* Parses the external configuration files
* Initialize appiumdriver instances based on the configuration
* Provides APIs to inject the AppiumDriver instances in your tests


### :pushpin: [AppiumDriver Framework TestNG](/appiumdriver-framework-testng)
AppiumDriver Framework TestNG is a module built on top of *AppiumDriver Framework Core* to facilitate appiumdriver injection into TestNG based tests. It also manages the appiumdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the AppiumDriver instance


### :pushpin: [AppiumDriver Framework JUnit4](/appiumdriver-framework-junit4)
AppiumDriver Framework JUnit4 is a module built on top of *AppiumDriver Framework Core* to facilitate appiumdriver injection into JUnit4 based tests. It also manages the appiumdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the AppiumDriver instance


### :pushpin: [AppiumDriver Framework JUnit5](/appiumdriver-framework-junit5)
AppiumDriver Framework JUnit5 is a module built on top of *AppiumDriver Framework Core* to facilitate appiumdriver injection into JUnit5 based tests. It also manages the appiumdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the AppiumDriver instance
