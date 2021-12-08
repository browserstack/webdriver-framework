from config import WebDriverConfiguration
from config import Platform
from config import OnPremDriverConfig

from datetime import datetime
import yaml
import json

class WebDriverFactoryClass:
    DEFAULT_CAPABILITIES_FILE = "./capabilities.yml"


    BROWSERSTACK_USERNAME = "BROWSERSTACK_USERNAME"
    BROWSERSTACK_ACCESS_KEY = "BROWSERSTACK_ACCESS_KEY"
    BUILD_ID = "BUILD_ID"
    DEFAULT_BUILD_NAME = "browserstack-examples-junit5"
    DEFAULT_BUILD_ENV_NAME = "BROWSERSTACK_BUILD_NAME"

    WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver"
    WEBDRIVER_GECKO_DRIVER = "webdriver.gecko.driver"
    WEBDRIVER_IE_DRIVER = "webdriver.ie.driver"
    WEBDRIVER_EDGE_DRIVER = "webdriver.edge.driver"

    instance = None

    webDriverConfiguration : WebDriverConfiguration
    defaultBuildSuffix     : str

    def __init__(self) :
        self.defaultBuildSuffix = datetime.now()
        self.webDriverConfiguration = self.parseWebDriverConfig()
        # platforms = self.webDriverConfiguration.getActivePlatforms()
        # startLocalTunnel();
        # LOGGER.debug("Running tests on {} active platforms.", platforms.size());

    def parseWebDriverConfig(self) :
        capabilitiesConfigFile = self.DEFAULT_CAPABILITIES_FILE
        # LOGGER.debug("Using capabilities configuration from FILE :: {}", capabilitiesConfigFile);
        ##Read yml file 
        configData = {}
        with open(capabilitiesConfigFile) as f:
            configData = yaml.safe_load(f)

        webDriverConfiguration : WebDriverConfiguration = None
        try:
            #map to object 
            print("Mapping object...")
            webDriverConfiguration = WebDriverConfiguration.WebDriverConfigurationClass()

            #Setting Test Endpoint
            webDriverConfiguration.setTestEndpoint(configData["testEndpoint"])
            endpoint = webDriverConfiguration.getTestEndpoint()
            print(f"Test Endpoint: {endpoint}")

            #Setting NamedTestURls
            allUrlsDict = configData["namedTestUrls"]
            for key in allUrlsDict:
                value = configData["namedTestUrls"][key]
                webDriverConfiguration.setNamedTestUrls(key, value)

            urls = webDriverConfiguration.getNamedTestUrls()
            print(f"Named Test URLS: {urls}")

            #Setting DriverType
            driverType = configData["driverType"]
            webDriverConfiguration.setDriverType(driverType)
            dtype = webDriverConfiguration.getDriverType()
            print(f"Driver Type: {dtype}")

            #Setting On Prem
            platformsForOnPrem = []

            platformsData = configData["onPremDriver"]["platforms"]
            for platform in platformsData:
                temp_platform = Platform.PlatformClass()

                temp_platform.setDriverPath(platform["driverPath"])
                temp_platform.setName(platform["name"])

                platformsForOnPrem.append(temp_platform)

            onPremDriverConfig = OnPremDriverConfig.OnPremDriverConfigClass()
            onPremDriverConfig.setPlatforms(platformsForOnPrem)

            webDriverConfiguration.setOnPremDriverConfig(onPremDriverConfig)

            #Setting cloud Driver
                
                
            # allPlatforms = configData["cloudDriver"]["platforms"]
            # for platform in allPlatforms:
            #     print(platform)
            #     print("################")
            # webDriverConfiguration = objectMapper.readValue(resourceURL, WebDriverConfiguration.class)
        except Exception as e:
            print(e)
            # throw new Error("Unable to parse capabilities file " + capabilitiesConfigFile, ioe)
        
        return webDriverConfiguration
