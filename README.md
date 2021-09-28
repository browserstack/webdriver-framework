<h1 align="center"> <a href="/">Webdriver Framework</a> </h1>

This is a framework designed to manage Webdrivers efficiently.
Selenium-based tests make extensive use of Webdriver APIs. Hence, Webdriver is a dependency for all Selenium-based test suites.There are no good dependency management frameworks for Selenium Webdrivers in Java. 

A good dependency injection framework should be able to isolate the creation of WebDrivers from the test code. It should also be able to externalise all the configuration (selenium capabilities) required for the WebDriver initialization. It should be flexible enough to support various test environments including on premise execution or execution on a selenium grid such as BrowserStack Automate.


### :pushpin: [Webdriver Framework Core](/webdriver-framework-core)
Webdriver Framework Core is a loosely coupled module which incorporates the following:
* Parses the external configuration files
* Initialize webdriver instances based on the configuration
* Provides APIs to inject the Webdriver instances in your tests


### :pushpin: [Webdriver Framework TestNG](/webdriver-framework-testng)
Webdriver Framework TestNG is a module built on top of *Webdriver Framework Core* to facilitate webdriver injection into TestNG based tests. It also manages the webdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Webdriver instance


### :pushpin: [Webdriver Framework JUnit4](/webdriver-framework-junit4)
Webdriver Framework JUnit4 is a module built on top of *Webdriver Framework Core* to facilitate webdriver injection into JUnit4 based tests. It also manages the webdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Webdriver instance


### :pushpin: [Webdriver Framework JUnit5](/webdriver-framework-junit5)
Webdriver Framework JUnit5 is a module built on top of *Webdriver Framework Core* to facilitate webdriver injection into JUnit5 based tests. It also manages the webdriver lifecycle with features such as:
* Marking the status of tests running on BrowserStack Automate
* Terminating the Webdriver instance
