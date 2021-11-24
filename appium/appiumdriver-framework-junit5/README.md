# AppiumDriver Framework JUnit5

A library that contains the glue code and the necessary instructions for using webdriver-framework-core with the JUnit5 hooks. This library also contains JUnit5 specific code for managing the lifecycle of the AppiumDriver with Test Method.

## Integration Instructions 

This is the JUnit5 specific framework library intended to be used by developers who are using JUnit5 as their Testing framework. The steps to include this into your code is as follows,

1. <b>Add the library dependency to your pom.xml.</b>
```xml
<dependency>
  <groupId>com.browserstack</groupId>
  <artifactId>webdriver-framework-junit5</artifactId>
  <version>LATEST</version>
</dependency>
 ```
 
2. <b>Create AppiumDriver Test</b>:  Create your test methods with AppiumDriver as a parameter. 

```java
public class SampleTest {

   public void test (AppiumDriver<?> appiumDriver) {
      ...
   }
   
}
```

3. <b>Annotate your tests with @AppiumDriverTest Annotations</b>: This will inject AppiumDrivers based on your configuration files.

```java
public class SampleTest {
    
   @AppiumDriverTest
   public void test (AppiumDriver<?> appiumDriver) {
      ...
   }
   
}
```


4. <b>To ensure that the AppiumDriver instances are stopped when the test is completed, a Test Watcher is provided as part of this library</b>

The TestWatcher class is responsible for,

- Marking the tests as pass/fail along with the appropriate error messages.

- Quitting the AppiumDriver instance such that the test session on BrowserStack also is ended cleanly.
