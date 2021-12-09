from config import WebDriverConfiguration
from config import Platform
from config import OnPremDriverConfig
from config import RemoteDriverConfig
from config import LocalTunnelConfig
from config import CommonCapabilities
from config import Capabilities
from config import DriverType

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
        # print(self.webDriverConfiguration.getDriverType())
        platforms = self.webDriverConfiguration.getActivePlatforms()
        print(platforms)
        # startLocalTunnel();
        # LOGGER.debug("Running tests on {} active platforms.", platforms.size());

    def isLocalTunnelEnabled(self):
        return self.webDriverConfiguration.getCloudDriverConfig() != None and \
                 self.webDriverConfiguration.getCloudDriverConfig().getLocalTunnel() != None and \
                 self.webDriverConfiguration.getCloudDriverConfig().getLocalTunnel().isEnabled() != None and \
                 self.webDriverConfiguration.getCloudDriverConfig().getLocalTunnel().isEnabled()

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

            #Setting NamedTestURls
            allUrlsDict = configData["namedTestUrls"]
            for key in allUrlsDict:
                value = configData["namedTestUrls"][key]
                webDriverConfiguration.setNamedTestUrls(key, value)

            #Setting DriverType
            driverType = configData["driverType"]
            webDriverConfiguration.setDriverType(DriverType.DriverTypeClass[driverType])

            #Setting On Prem
            platformsForOnPrem = []

            platformsData = configData["onPremDriver"]["platforms"]
            for platform in platformsData:
                temp_platform = Platform.PlatformClass()
                if "driverPath" in platform:
                    temp_platform.setDriverPath(platform["driverPath"])
                if "name" in platform:
                    temp_platform.setName(platform["name"])

                platformsForOnPrem.append(temp_platform)

            onPremDriverConfig = OnPremDriverConfig.OnPremDriverConfigClass()
            #setting All platforms in Driver config
            onPremDriverConfig.setPlatforms(platformsForOnPrem)

            webDriverConfiguration.setOnPremDriverConfig(onPremDriverConfig)

            #Setting cloud Driver
            cloudDriverConfig = RemoteDriverConfig.RemoteDriverConfigClass()
            cloudDriverConfig.setHubUrl(configData["cloudDriver"]["hubUrl"])
            cloudDriverConfig.setUser(configData["cloudDriver"]["user"])
            cloudDriverConfig.setAccessKey(configData["cloudDriver"]["accessKey"])

            localConfig = LocalTunnelConfig.LocalTunnelConfigClass()
            localConfig.setEnabled(configData["cloudDriver"]["localTunnel"]["enabled"])

            #Add logic to put isEnabled Local Tunnel and then call LocalFactory to set local options.

            capabilities = Capabilities.CapabilitiesClass()
            capsFromConfig = configData["cloudDriver"]["common_capabilities"]["capabilities"]
            for cap in capsFromConfig:
                capabilities.setCapabilities(cap, capsFromConfig[cap])
            
            commonCaps = CommonCapabilities.CommonCapabiltiesClass()
            commonCaps.setProject(configData["cloudDriver"]["common_capabilities"]["project"])
            commonCaps.setBuildPrefix(configData["cloudDriver"]["common_capabilities"]["buildPrefix"])
            commonCaps.setCapabilities(capabilities)

            cloudDriverConfig.setCommonCapabilities(commonCaps)

            platformsForCloudDriver = []

            platformsData = configData["cloudDriver"]["platforms"]
            for platform in platformsData:
                temp_platform = Platform.PlatformClass()

                if "name" in platform:
                    temp_platform.setName(platform["name"])
                if "os" in platform:
                    temp_platform.setOs(platform["os"])
                if "os_version" in platform:
                    temp_platform.setOsVersion(platform["os_version"])
                if "browser" in platform:
                    temp_platform.setBrowser(platform["browser"])
                if "browser_version" in platform:
                    temp_platform.setBrowserVersion(platform["browser_version"])
                if "device" in platform:
                    temp_platform.setDevice(platform["device"])
                if "realMobile" in platform:
                    temp_platform.setRealMobile(platform["realMobile"])
                    
                temp_capabilities = Capabilities.CapabilitiesClass()
                temp_caps = platform["capabilities"]

                for cap in temp_caps:
                    temp_capabilities.setCapabilities(cap, temp_caps[cap])
                
                temp_platform.setCapabilities(temp_capabilities)
                
                #Adding Current Platform in list
                platformsForCloudDriver.append(temp_platform)

            #setting All platforms in Driver config
            cloudDriverConfig.setPlatforms(platformsForCloudDriver)
            #setting config to webDriverConfig
            webDriverConfiguration.setCloudDriverConfig(cloudDriverConfig)

            #Setting onPremGridDriver Config now

            onPremGridDriverConfig = RemoteDriverConfig.RemoteDriverConfigClass()
            onPremGridDriverConfig.setHubUrl(configData["onPremGridDriver"]["hubUrl"])
            onPremGridDriverConfig.setUser(configData["onPremGridDriver"]["user"])
            onPremGridDriverConfig.setAccessKey(configData["onPremGridDriver"]["accessKey"])

            platformsForOnPremGridDriver = []

            platformsData = configData["onPremGridDriver"]["platforms"]
            for platform in platformsData:
                temp_platform = Platform.PlatformClass()

                if "name" in platform:
                    temp_platform.setName(platform["name"])
                if "os" in platform:
                    temp_platform.setOs(platform["os"])
                if "os_version" in platform:
                    temp_platform.setOsVersion(platform["os_version"])
                if "browser" in platform:
                    temp_platform.setBrowser(platform["browser"])
                if "browser_version" in platform:
                    temp_platform.setBrowserVersion(platform["browser_version"])
                if "device" in platform:
                    temp_platform.setDevice(platform["device"])
                if "realMobile" in platform:
                    temp_platform.setRealMobile(platform["realMobile"])
                    
                temp_capabilities = Capabilities.CapabilitiesClass()
                temp_caps = platform["capabilities"]

                for cap in temp_caps:
                    temp_capabilities.setCapabilities(cap, temp_caps[cap])
                
                temp_platform.setCapabilities(temp_capabilities)

                #Adding Current Platform in list
                platformsForOnPremGridDriver.append(temp_platform)

            #setting All platforms in Driver config
            onPremGridDriverConfig.setPlatforms(platformsForOnPremGridDriver)
            
            #setting config to webDriverConfig
            webDriverConfiguration.setOnPremGridDriverConfig(onPremGridDriverConfig)
            
            
        except Exception as e:
            print(e)

        finally:
            print(webDriverConfiguration.getOnPremDriverConfig())
            print(webDriverConfiguration.getOnPremGridDriverConfig())
            print(webDriverConfiguration.getCloudDriverConfig())

        return webDriverConfiguration

    #return a selenium webdriver
    def createWebDriverForPlatform(self, platform:Platform, testName:str) :

        try :
            webDriver = None
            driverType = self.webDriverConfiguration.getDriverType()
            if driverType is "onPremDriver":
                webDriver = self.createOnPremWebDriver(platform)
            elif driverType is "onPremGridDriver":
                webDriver = self.createOnPremGridWebDriver(platform)
            elif driverType is "cloudDriver":
                webDriver = self.createRemoteWebDriver(platform, testName)

            return webDriver;
        except Exception as e:
           print(e)
        
    def createRemoteWebDriver(self, platform:Platform, testName: str):
        print("Creating Remote Cloud Driver")