# AppiumDriver Framework JUnit4

A library that contains the glue code and the necessary instructions for using appiumdriver-framework-core with the JUnit4 hooks. This library also contains JUnit4 specific code for managing the lifecycle of the AppiumDriver with Test Method.

## Integration Instructions

This is the JUnit4 specific framework library intended to be used by developers who are using JUnit4 as their Testing framework. The steps to include this into your code is as follows,

1. <b>Add the library dependency to your pom.xml.</b>

```xml
<dependency>
  <groupId>com.browserstack</groupId>
  <artifactId>appiumdriver-framework-junit4</artifactId>
  <version>LATEST</version>
</dependency>
```

2. <b>Create AppiumDriver Test</b>: Create your test methods with AppiumDriver as a parameter.

```java
public class SampleTest extends AbstractAppiumDriverTest {

    @Test
    public void test () {
        AppiumDriver<?> appiumDriver = this.appiumDriverProviderRule.getAppiumDriver(platform);
        ...
   }

}
```

3. <b>Annotate your test classes with `@RunWith(Parameterized.class)` Annotations</b>: Parameterized tests allow a developer to run the same test over and over again using different values.

```java
@RunWith(Parameterized.class)
public class SampleTest extends AbstractAppiumDriverTest {

    @Test
    public void test () {
        AppiumDriver<?> appiumDriver = this.appiumDriverProviderRule.getAppiumDriver(platform);
        ...
   }

}
```

4. <b>To ensure that the AppiumDriver instances are stopped when the test is completed, a Test Watcher is provided as part of this library</b>

The TestWatcher class is responsible for,

- Marking the tests as pass/fail along with the appropriate error messages.

- Quitting the AppiumDriver instance such that the test session on BrowserStack also is ended cleanly.
