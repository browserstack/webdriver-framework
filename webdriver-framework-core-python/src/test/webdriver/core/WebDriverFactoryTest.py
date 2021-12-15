import sys
from pathlib import Path
from threading import Thread

sys.path.insert(0, '../../../main/webdriver')

from core import WebDriverFactory

webDriver = None
fact = WebDriverFactory.WebDriverFactoryClass.getInstance()

all_platforms = fact.getPlatforms()
url_to_test = fact.getTestEndpoint()

all_threads = []

def threaded_function(driver):
    driver.get(url_to_test)
    driver.quit()

#Creating Threads for execution
for platform in all_platforms:
    webDriver = fact.createWebDriverForPlatform(platform, "Test-1")
    if(webDriver != None):
        all_threads.append(Thread(target = threaded_function, args = (webDriver, )))

#Starting the Threads
for thread in all_threads:
    thread.start()


    