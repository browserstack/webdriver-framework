package com.browserstack.webdriver.junit5.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.Platform;

import java.util.Optional;

public class AllureListener implements TestWatcher {

    private final Platform platform;

    public AllureListener(Platform platform) {
        this.platform = platform;
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        markAllure(context);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        markAllure(context);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        markAllure(context);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        markAllure(context);
    }

    private void markAllure(ExtensionContext context){}

    // TODO : Support for Allure
    /*
    private void markAllure(ExtensionContext context){
        AllureLifecycle allureLifecycle = Allure.getLifecycle();
        DriverType driverType = WebDriverFactory.getInstance().getDriverType();
        String suite = WebDriverFactory.getInstance().getSuiteName();
        allureLifecycle.updateTestCase(testResult -> {
            List<Label> labels = testResult.getLabels();
            List<Link> links = testResult.getLinks();
            List<Parameter> parameters = testResult.getParameters();
            labels.stream()
                    .filter(label -> label.getName().equals(ResultsUtils.SUITE_LABEL_NAME))
                    .findFirst()
                    .get()
                    .setValue(suite);
            labels.add(new Label().setName("tag").setValue("Profile: "+suite));
            labels.add(new Label().setName("tag").setValue("Platform: "+platform.getName()));
            if (driverType.equals(DriverType.cloudDriver)){
                String testName = context.getDisplayName();
                RemoteWebDriver webDriver = (RemoteWebDriver) context.getStore(WebDriverParameterResolver.STORE_NAMESPACE).get(testName, WebDriver.class);
                links.add(new Link().setName("Automate Session Link").setUrl("https://automate.browserstack.com/dashboard/v2/sessions/"+webDriver.getSessionId()));
            }
            parameters.add(new Parameter().setName("Driver Type").setValue(WebDriverFactory.getInstance().getDriverType().toString()));
            if (platform.getName()!=null){
                parameters.add(new Parameter().setName("Name").setValue(platform.getName()));
            }
            if (platform.getOs()!=null){
                parameters.add(new Parameter().setName("OS").setValue(platform.getOs()));
            }
            if (platform.getOsVersion()!=null){
                parameters.add(new Parameter().setName("OS Version").setValue(platform.getOsVersion()));
            }
            if (platform.getBrowser()!=null){
                parameters.add(new Parameter().setName("Browser").setValue(platform.getBrowser()));
            }
            if (platform.getBrowserVersion()!=null){
                parameters.add(new Parameter().setName("Browser Version").setValue(platform.getBrowserVersion()));
            }
            if (platform.getDevice()!=null){
                parameters.add(new Parameter().setName("Device").setValue(platform.getDevice()));
            }
        });
    }
     */
}
