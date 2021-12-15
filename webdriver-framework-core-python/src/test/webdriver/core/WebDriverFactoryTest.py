import sys
from pathlib import Path
# sys.path.append(str(Path('.').absolute().parent))

sys.path.insert(0, '/Users/princeton99/Desktop/All/BrowserStack/BrowserStack_Projects/webdriver-framework/webdriver-framework-core-python/src/main/webdriver')

from core import WebDriverFactory

fact = WebDriverFactory.WebDriverFactoryClass.getInstance()

all_platforms = fact.getPlatforms()

webDriver = None
for platform in all_platforms:

    webDriver = fact.createWebDriverForPlatform(platform, "Test-1")
    url_to_test = fact.getTestEndpoint()

    webDriver.get(url_to_test)
    # print(webDriver)
    webDriver.quit()
    break