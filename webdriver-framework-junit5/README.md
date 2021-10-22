# Webdriver Framework JUnit5

A library that contains the glue code and the necessary instructions for using webdriver-framework-core with the JUnit5 hooks. This library also contains JUnit5 specific code for managing the lifecycle of the WebDriver with Test Method.

## Integration Instructions 

This is the JUnit5 specific framework library intended to be used by developers who are using JUnit5 as their Testing framework. The steps to include this into your code is as follows,

1. <b>Add the library dependency to your pom.xml.</b>
```xml
<dependency>
  <groupId>io.github.webdriver.junit5</groupId>
  <artifactId>webdriver-framework-junit5</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
 ```
 
2. <b>Create Webdriver Test</b>:  Create your test methods with Webdriver as a parameter. 

```java
public class SampleTest {

   public void test (WebDriver webDriver) {
      ...
   }
   
}
```

3. <b>Annotate your tests with @WebDriverTest Annotations</b>: This will inject Webdrivers based on your configuration files.

```java
public class SampleTest {
    
   @WebDriverTest
   public void test (WebDriver webDriver) {
      ...
   }
   
}
```


4. <b>To ensure that the WebDriver instances are stopped when the test is completed, a Test Watcher is provided as part of this library</b>

The TestWatcher class is responsible for,

- Marking the tests as pass/fail along with the appropriate error messages.

- Quitting the WebDriver instance such that the test session on BrowserStack also is ended cleanly.
