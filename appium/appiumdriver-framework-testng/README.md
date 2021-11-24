# AppiumDriver Framework TestNG

A library that contains the glue code and the necessary instructions for using webdriver-framework-core with the TestNG hooks. This library also contains TestNG specific code for managing the lifecycle of the AppiumDriver with Test Method.

## Integration Instructions 

This is the testng specific framework library intended to be used by developers who are using TestNG as their Testing framework. The steps to include this into your code is as follows,

1. <b>Add the library dependency to your pom.xml.</b>
```xml
<dependency>
  <groupId>com.browserstack</groupId>
  <artifactId>webdriver-framework-testng</artifactId>
  <version>LATEST</version>
</dependency>
 ```
 
2. <b>Create the Data Provider for injecting of the AppiumDriver</b>:  TestNG enables you to pass in parameters into your test method via the use of Data Providers. We use the @DataProvider annotation on a method that is then responsible for providing the number of AppiumDrivers that are configured in the AppiumDriver config. This data provider method can be added to a base class from which all your tests are extended and used as below

```java
public abstract class BaseTest {

   @DataProvider(name="appiumdriver", parallel = true)
   public static Iterator<Object[]> provideAppiumDrivers(Method testMethod) {
        return new LazyInitAppiumDriverIterator(testMethod.getName(),
                                             AppiumDriverFactory.getInstance().getPlatforms(),
                                             new Object[0]);
   }
   
}
```

The LazyInitAppiumDriverIterator is responsible for creating the AppiumDriver instances just before the test method is called by TestNG. This it does a Lazy initialization off the AppiumDriver object just before it used. This is done because in the case of Cloud Providers like BrowserStack as soon as the AppiumDriver object is created a corresponding Test session is started on BrowserStack.

3. <b>To ensure that the AppiumDriver instances are stopped when the test is completed, be sure to also integrate a Test Listener provided as part of this library</b>. This can be done as shown below,

```java
@Listeners({AppiumDriverListener.class})
public abstract class BaseTest {

   @DataProvider(name="appiumdriver", parallel = true)
   public static Iterator<Object[]> provideAppiumDrivers(Method testMethod) {
        return new LazyInitAppiumDriverIterator(testMethod.getName(),
                                             AppiumDriverFactory.getInstance().getPlatforms(),
                                             new Object[0]);
   }
   
}
```
The AppiumDriverListener class is responsible for,

- Marking the tests as pass/fail along with the appropriate error messages.

- Quitting the AppiumDriver instance such that the test session on BrowserStack also is ended cleanly.


Now, the `BaseTest` class can then be extended by your webdriver based test classes as shown below

```java
public class SomeUsefulTestClass extends BaseTest {

    @Test(dataProvider = "appiumdriver")
    public void placeOrder(AppiumDriver<?> appiumDriver) {
        // Write a useful test with the AppiumDriver
    }
    
}
```
