# webdriver-framework


# webdriver-framework-core

A library that contains the logic for creating the WebDriver instances based on a configuration file. These WebDriver instances can be either be used for controlling cloud browsers or in-house browsers. This library will also be responsible for managing the BrowserStackLocal tunnel.

# [webdriver-framework-testng](/webdriver-framework-testng)

A library that contains the glue code and the necessary instructions for using webdriver-framework-core with the TestNG hooks. This library also contains TestNG specific code for managing the lifecycle of the WebDriver with Test Method.

# webdriver-framework-junit4

Library that contains the glue code and the necessary instructions for integrating the WebDriver instantiation using the JUnit4 hooks. This library will also contain JUnit4 specific code for tying the lifecycle of the WebDriver with that of the Test Method.

# webdriver-framework-junit5

Library that contains the glue code and the necessary instructions for integrating the WebDriver instantiation using JUnit5 hooks. This library will also contain JUnit4 specific code for tying the lifecycle of the WebDriver with that of the Test Method.
