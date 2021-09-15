package com.browserstack.webdriver.junit5.extensions;

import org.apiguardian.api.API;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

import static org.apiguardian.api.API.Status.STABLE;

/**
 * {@code @WebDriverTest} is used to signal that the annotated method is a
 * <em>web driver test</em> method.
 *
 * <p>{@code @WebDriverTest} methods must not be {@code private} or {@code static}
 * and must not return a value.
 *
 * <p>{@code @WebDriverTest} methods may optionally declare {@link org.openqa.selenium.WebDriver WebDriver} parameter to be
 * resolved by {@link com.browserstack.webdriver.junit5.extensions.WebDriverParameterResolver
 * WebDriverParameterResolver}.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = STABLE)
@TestTemplate
@ExtendWith(WebDriverTestExtension.class)
public @interface WebDriverTest {
}