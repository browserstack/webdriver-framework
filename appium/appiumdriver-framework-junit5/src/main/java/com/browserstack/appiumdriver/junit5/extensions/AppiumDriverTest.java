package com.browserstack.appiumdriver.junit5.extensions;

import org.apiguardian.api.API;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

import static org.apiguardian.api.API.Status.STABLE;

/**
 * {@code @AppiumDriverTest} is used to signal that the annotated method is a
 * <em>web driver test</em> method.
 *
 * <p>{@code @AppiumDriverTest} methods must not be {@code private} or {@code static}
 * and must not return a value.
 *
 * <p>{@code @AppiumDriverTest} methods may optionally declare {@link io.appium.java_client.AppiumDriver AppiumDriver} parameter to be
 * resolved by {@link com.browserstack.appiumdriver.junit5.extensions.AppiumDriverParameterResolver
 * AppiumDriverParameterResolver}.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = STABLE)
@TestTemplate
@ExtendWith(AppiumDriverTestExtension.class)
public @interface AppiumDriverTest {
}