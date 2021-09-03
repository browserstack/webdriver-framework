<h1 align="center"> <a href="/">Webdriver Framework</a> </h1>

In the Java ecosystem there isn’t any readily available framework/library that will allow Automation Developers to create WebDriver instances via a configuration file. The Webdriver Framework an attempt to create a simple framework that is responsible for

:page_facing_up:  Parsing a simple configuration file to create the WebDriver instances.
<br><br>
:recycle: Providing glue code for the different test frameworks so that the user can use these to manage the lifecycle of the WebDriver.
<br><br>
:pushpin: Enable users to use BrowserStack Automate efficiently with best practices such as optimal parallel utilization, marking test statuses and following good  naming conventions (such as project name) etc.


The dependency will host a Java library that will have the following components,



<h3> :pushpin: <a href="/webdriver-framework-core"> webdriver-framework-core</a></h3>
 
  Responsible for parsing configuration files and initialise the WebDriver instances

### :pushpin: [webdriver-framework-testng](/webdriver-framework-testng)

A wrapper using webdriver-framework-core to manage WebDrivers for TestNG tests.

### :pushpin: [webdriver-framework-junit4](/webdriver-framework-junit4)

A wrapper using webdriver-framework-core to manage WebDrivers for JUnit4 tests.

### :pushpin: [webdriver-framework-junit5](/webdriver-framework-junit5)

A wrapper using webdriver-framework-core to manage WebDrivers for JUnit5 tests.

