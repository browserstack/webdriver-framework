package com.browserstack.webdriver.testng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.browserstack.webdriver.config.Platform;
import com.browserstack.webdriver.core.WebDriverFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Anirudha Khanna
 */
public class LazyInitWebDriverIterator implements Iterator<Object[]> {

    private final String testMethodName;
    private final List<Platform> platforms;
    private final List<Object[]> testParams;
    private final boolean createManagedWebDriver;
    private int paramIdx = 0;

    public LazyInitWebDriverIterator(String testMethodName, Object[] testParams) {
        this.testMethodName = testMethodName;
        this.platforms = WebDriverFactory.getInstance().getPlatforms();
        this.createManagedWebDriver = false;

        if (testParams == null) {
            testParams = new Object[0];
        }
        List<Object[]> otherParamsList = new ArrayList<>();
        otherParamsList.add(testParams);
        this.testParams = populateTestParams(otherParamsList);
    }

    public LazyInitWebDriverIterator(String testMethodName, List<Object[]> testParams) {
        this.testMethodName = testMethodName;
        this.platforms = WebDriverFactory.getInstance().getPlatforms();
        this.createManagedWebDriver = false;

        if (testParams == null) {
            testParams = new ArrayList<>();
        }
        this.testParams = populateTestParams(testParams);
    }

    public LazyInitWebDriverIterator(Boolean createManagedWebDriver,
                                     Object[][] testParams) {
        this.testMethodName = StringUtils.EMPTY;
        this.platforms = WebDriverFactory.getInstance().getPlatforms();
        this.createManagedWebDriver = createManagedWebDriver;

        List<Object[]> testParamsList = new ArrayList<>();
        if (testParams != null) {
            testParamsList = Arrays.stream(testParams).collect(Collectors.toList());
        }
        this.testParams = populateTestParams(testParamsList);
    }

    private List<Object[]> populateTestParams(List<Object[]> testParams) {
        int idx = 0;
        List<Object[]> tempTestParams = new ArrayList<>();
        do {
            Object[] testParam = testParams.get(idx);
            if (testParam == null) {
                testParam = new Object[0];
            }
            for (Platform platform : platforms) {
                Object[] paramsWithPlatform = Arrays.copyOf(testParam, testParam.length + 1);
                paramsWithPlatform[paramsWithPlatform.length - 1] = platform;
                tempTestParams.add(paramsWithPlatform);
            }
            idx++;
        } while(idx < testParams.size());
        return tempTestParams;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return paramIdx < this.testParams.size();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Object[] next() {
        if (this.paramIdx >= this.testParams.size()) {
            throw new NoSuchElementException("No More Platforms configured to create WebDriver for.");
        }

        Object[] methodTestParams = this.testParams.get(paramIdx++);
        if (methodTestParams[methodTestParams.length - 1] instanceof Platform) {
            Platform platform = (Platform) methodTestParams[methodTestParams.length - 1];
            if (this.createManagedWebDriver) {
                ManagedWebDriver managedWebDriver = new ManagedWebDriver(testMethodName, platform);
                methodTestParams[methodTestParams.length - 1] = managedWebDriver;
            } else {
                WebDriver webDriver = WebDriverFactory.getInstance().createWebDriverForPlatform(platform, this.testMethodName);
                methodTestParams[methodTestParams.length - 1] = webDriver;
            }
        }

        return methodTestParams;
    }
}
