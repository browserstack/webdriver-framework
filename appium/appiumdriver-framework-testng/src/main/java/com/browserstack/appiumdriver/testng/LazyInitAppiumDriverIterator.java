package com.browserstack.appiumdriver.testng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.browserstack.appiumdriver.config.Platform;
import com.browserstack.appiumdriver.core.AppiumDriverFactory;

import org.apache.commons.lang3.StringUtils;

import io.appium.java_client.AppiumDriver;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Anirudha Khanna
 */
public class LazyInitAppiumDriverIterator implements Iterator<Object[]> {

    private final String testMethodName;
    private final List<Platform> platforms;
    private final List<Object[]> testParams;
    private final boolean createManagedAppiumDriver;
    private int paramIdx = 0;

    public LazyInitAppiumDriverIterator(String testMethodName, Object[] testParams) {
        this.testMethodName = testMethodName;
        this.platforms = AppiumDriverFactory.getInstance().getPlatforms();
        this.createManagedAppiumDriver = false;

        if (testParams == null) {
            testParams = new Object[0];
        }
        List<Object[]> otherParamsList = new ArrayList<>();
        otherParamsList.add(testParams);
        this.testParams = populateTestParams(otherParamsList);
    }

    public LazyInitAppiumDriverIterator(String testMethodName, List<Object[]> testParams) {
        this.testMethodName = testMethodName;
        this.platforms = AppiumDriverFactory.getInstance().getPlatforms();
        this.createManagedAppiumDriver = false;

        if (testParams == null) {
            testParams = new ArrayList<>();
        }
        this.testParams = populateTestParams(testParams);
    }

    public LazyInitAppiumDriverIterator(Boolean createManagedAppiumDriver,
                                     Object[][] testParams) {
        this.testMethodName = StringUtils.EMPTY;
        this.platforms = AppiumDriverFactory.getInstance().getPlatforms();
        this.createManagedAppiumDriver = createManagedAppiumDriver;

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
            throw new NoSuchElementException("No More Platforms configured to create AppiumDriver for.");
        }

        Object[] methodTestParams = this.testParams.get(paramIdx++);
        if (methodTestParams[methodTestParams.length - 1] instanceof Platform) {
            Platform platform = (Platform) methodTestParams[methodTestParams.length - 1];
            if (this.createManagedAppiumDriver) {
                ManagedAppiumDriver managedAppiumDriver = new ManagedAppiumDriver(testMethodName, platform);
                methodTestParams[methodTestParams.length - 1] = managedAppiumDriver;
            } else {
                AppiumDriver<?> appiumDriver = AppiumDriverFactory.getInstance().createAppiumDriverForPlatform(platform, this.testMethodName);
                methodTestParams[methodTestParams.length - 1] = appiumDriver;
            }
        }

        return methodTestParams;
    }
}
