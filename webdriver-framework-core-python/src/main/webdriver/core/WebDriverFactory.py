from config import WebDriverConfiguration
from config import Platform
from config import OnPremDriverConfig
from config import RemoteDriverConfig
from config import LocalTunnelConfig
from config import CommonCapabilities
from config import Capabilities
from config import DriverType
from core import LocalFactory

from datetime import datetime
import yaml
import json
import os
import threading

from selenium import webdriver

class WebDriverFactoryClass:
    DEFAULT_CAPABILITIES_FILE = "/Users/princeton99/Desktop/All/BrowserStack/BrowserStack_Projects/webdriver-framework/webdriver-framework-core-python/src/test/resources/webdriver-config.yml"

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

    WebDriverFactoryClassLock = threading.Lock()

    def __init__(self) :
        self.defaultBuildSuffix = str(datetime.now())
        self.webDriverConfiguration = self.parseWebDriverConfig()
        platforms = self.webDriverConfiguration.getActivePlatforms()
        if(self.isCloudDriver()):
            self.startLocalTunnel()
        print(f"Running tests on {len(platforms)} active platforms." )

    @staticmethod
    def getInstance() :
        if (WebDriverFactoryClass.instance == None) :
            with WebDriverFactoryClass.WebDriverFactoryClassLock :
                if (WebDriverFactoryClass.instance == None) :
                    WebDriverFactoryClass.instance = WebDriverFactoryClass()

        return WebDriverFactoryClass.instance

    def isLocalTunnelEnabled(self):
        return self.webDriverConfiguration.getCloudDriverConfig() != None and \
                 self.webDriverConfiguration.getCloudDriverConfig().getLocalTunnel() != None and \
                 self.webDriverConfiguration.getCloudDriverConfig().getLocalTunnel().isEnabled() != None and \
                 self.webDriverConfiguration.getCloudDriverConfig().getLocalTunnel().isEnabled()

    def startLocalTunnel(self) :
        if (self.isLocalTunnelEnabled()) :
            localOptions = self.webDriverConfiguration.getCloudDriverConfig().getLocalTunnel().getLocalOptions()
            accessKey = self.webDriverConfiguration.getCloudDriverConfig().getAccessKey()
            if (os.getenv("BROWSERSTACK_ACCESS_KEY")):
                accessKey = os.getenv("BROWSERSTACK_ACCESS_KEY")
            
            localOptions["key"] =  accessKey
            LocalFactory.LocalFactoryClass.createInstance(self.webDriverConfiguration.getCloudDriverConfig().getLocalTunnel().getLocalOptions())
      
    def parseWebDriverConfig(self) :

        capabilitiesConfigFile = self.DEFAULT_CAPABILITIES_FILE
        print(f"Using capabilities configuration from FILE :: {capabilitiesConfigFile}");
        ##Read yml file 
        configData = {}
        with open(capabilitiesConfigFile) as f:
            configData = yaml.safe_load(f)

        webDriverConfiguration : WebDriverConfiguration = None
        try:
            #Mapping config file to objects

            webDriverConfiguration = WebDriverConfiguration.WebDriverConfigurationClass()

            #Setting Test Endpoint
            webDriverConfiguration.setTestEndpoint(configData["testEndpoint"])

            #Setting NamedTestURls
            if "namedTestUrls" in configData:
                allUrlsDict = configData["namedTestUrls"]
                for key in allUrlsDict:
                    value = configData["namedTestUrls"][key]
                    webDriverConfiguration.setNamedTestUrls(key, value)

            #Setting DriverType
            if "driverType" in configData:
                driverType = configData["driverType"]
                webDriverConfiguration.setDriverType(DriverType.DriverTypeClass[driverType])
            else:
                print("Please provide a driverType")

            #Setting On Prem
            platformsForOnPrem = []
            if "onPremDriver" in configData:
                platformsData = configData["onPremDriver"]["platforms"]
                for platform in platformsData:
                    temp_platform = Platform.PlatformClass()
                    if "browser" in platform:
                        temp_platform.setBrowser(platform["browser"])
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
            if "cloudDriver" in configData:
                cloudDriverConfig = RemoteDriverConfig.RemoteDriverConfigClass()
                cloudDriverConfig.setHubUrl(configData["cloudDriver"]["hubUrl"])
                if "user" in configData["cloudDriver"]:
                    cloudDriverConfig.setUser(configData["cloudDriver"]["user"])
                if "accessKey" in configData["cloudDriver"]:
                    cloudDriverConfig.setAccessKey(configData["cloudDriver"]["accessKey"])

                localConfig = LocalTunnelConfig.LocalTunnelConfigClass()
                if "localTunnel" in configData["cloudDriver"] and "enabled" in configData["cloudDriver"]["localTunnel"]:
                    localConfig.setEnabled(configData["cloudDriver"]["localTunnel"]["enabled"])
                    #Setting local Options
                    if("local_options" in configData["cloudDriver"]["localTunnel"]):
                        localOptionsDictionary = configData["cloudDriver"]["localTunnel"]["local_options"]
                        for key in localOptionsDictionary:
                            localConfig.setLocalOption(key, localOptionsDictionary[key])

                cloudDriverConfig.setLocalTunnel(localConfig)

                if "common_capabilities" in configData["cloudDriver"] and "capabilities" in configData["cloudDriver"]["common_capabilities"]:
                    capabilities = Capabilities.CapabilitiesClass()
                    capsFromConfig = configData["cloudDriver"]["common_capabilities"]["capabilities"]
                    for cap in capsFromConfig:
                        capabilities.setCapabilities(cap, capsFromConfig[cap])
                    
                    commonCaps = CommonCapabilities.CommonCapabiltiesClass()
                    if "project" in configData["cloudDriver"]["common_capabilities"]:
                        commonCaps.setProject(configData["cloudDriver"]["common_capabilities"]["project"])
                    if "buildPrefix" in configData["cloudDriver"]["common_capabilities"]:
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
                        
                    if "capabilities" in platform:
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
            if "onPremGridDriver" in configData:
                onPremGridDriverConfig = RemoteDriverConfig.RemoteDriverConfigClass()
                onPremGridDriverConfig.setHubUrl(configData["onPremGridDriver"]["hubUrl"])
                if "user" in configData["onPremGridDriver"]:
                    onPremGridDriverConfig.setUser(configData["onPremGridDriver"]["user"])
                if "accessKey" in configData["onPremGridDriver"]:
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
                        
                    if "capabilities" in platform:
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

        return webDriverConfiguration

    #return a selenium webdriver
    def createWebDriverForPlatform(self, platform:Platform, testName:str) :

        try :
            webDriver = None
            driverType = self.webDriverConfiguration.getDriverType()
            if driverType == "onPremDriver":
                webDriver = self.createOnPremWebDriver(platform)
            elif driverType == "onPremGridDriver":
                webDriver = self.createOnPremGridWebDriver(platform)
            elif driverType == "cloudDriver":
                webDriver = self.createRemoteWebDriver(platform, testName)

            return webDriver
        except Exception as e:
           print(e)
        
    def createRemoteWebDriver(self, platform:Platform, testName: str):

        remoteDriverConfig = self.webDriverConfiguration.getCloudDriverConfig()
        commonCapabilities = remoteDriverConfig.getCommonCapabilities()
        platformCapabilities = {}

        if platform.getDevice() != None:
            platformCapabilities["device"] = platform.getDevice()

        platformCapabilities["browser"] = platform.getBrowser()
        platformCapabilities["browser_version"] = platform.getBrowserVersion()
        platformCapabilities["os"] = platform.getOs()
        platformCapabilities["os_version"] = platform.getOsVersion()
        platformCapabilities["name"] = testName
        platformCapabilities["project"] = commonCapabilities.getProject()
        platformCapabilities["build"] = self.createBuildName(commonCapabilities.getBuildPrefix())
        if (commonCapabilities.getCapabilities() != None) :
            commonCaps = commonCapabilities.getCapabilities().getCapabilityMap()
            platformCapabilities.update(commonCaps)
        if (platform.getCapabilities() != None):
            platformCaps = platform.getCapabilities().getCapabilityMap()
            platformCapabilities.update(platformCaps)
        
        user = remoteDriverConfig.getUser()
        if (os.getenv("BROWSERSTACK_USERNAME") != "" or os.getenv("BROWSERSTACK_USERNAME") != None ) :
            user = os.getenv("BROWSERSTACK_USERNAME")
        
        accessKey = remoteDriverConfig.getAccessKey()
        if (os.getenv("BROWSERSTACK_ACCESS_KEY") != "" or os.getenv("BROWSERSTACK_ACCESS_KEY") != None ) :
            accessKey = os.getenv("BROWSERSTACK_ACCESS_KEY")

        platformCapabilities["browserstack.user"] = user
        platformCapabilities["browserstack.key"] = accessKey
        
        if (self.isLocalTunnelEnabled()) :
            platformCapabilities["browserstack.local"] = "true"
            platformCapabilities["browserstack.localIdentifier"] = LocalFactory.LocalFactoryClass.getInstance().getLocalIdentifier()


        print(f"Initialising RemoteWebDriver with capabilities : {platformCapabilities} ")
        cloudWebDriver =   webdriver.Remote( \
                        command_executor=remoteDriverConfig.getHubUrl(), \
                        desired_capabilities=platformCapabilities)

        return cloudWebDriver
        
    def createOnPremGridWebDriver(self, platform:Platform):
        
        browerType = platform.getBrowser().toUpperCase()
        capabilities = {}
        if (platform.getCapabilities() != None) :
            capabilities = platform.getCapabilities().getCapabilityMap()
        
        print(f"Initialising RemoteWebDriver with capabilities : {capabilities} ")
        onPremGridWebDriver =   webdriver.Remote( \
                        command_executor=self.webDriverConfiguration.getOnPremGridDriverConfig().getHubUrl(), \
                        desired_capabilities=capabilities)

        return onPremGridWebDriver

    def createOnPremWebDriver(self, platform:Platform):
        
        onPremWebDriver = None
        browserType = platform.getBrowser().upper()

        if browserType == "CHROME":
            os.environ['WEBDRIVER_CHROME_DRIVER'] = str(platform.getDriverPath())
            onPremWebDriver = webdriver.Chrome()
        elif browserType == "FIREFOX":
            os.environ['WEBDRIVER_GECKO_DRIVER'] = str(platform.getDriverPath())
            onPremWebDriver = webdriver.Firefox()
        elif browserType == "IE":
            os.environ['WEBDRIVER_IE_DRIVER'] = str(platform.getDriverPath())
            onPremWebDriver = webdriver.Ie()
        elif browserType == "EDGE":
            os.environ['WEBDRIVER_EDGE_DRIVER'] = str(platform.getDriverPath())
            onPremWebDriver = webdriver.Edge()
        elif browserType == "SAFARI" :
            onPremWebDriver = webdriver.Safari()
        elif browserType == "OPERA" :
            onPremWebDriver = webdriver.Opera()
        else:
            print("Not supported")
            return None
        
        print(f"Initialising Driver for : {browserType} ")
        return onPremWebDriver

    def getTestEndpoint(self):
        return self.webDriverConfiguration.getTestEndpoint()

    def getNamedTestUrls(self):
        return self.webDriverConfiguration.getNamedTestUrls();

    def getNamedUrl(name :str) :
        return WebDriverFactoryClass.getInstance().webDriverConfiguration.getNamedUrl(name)

    def getDriverType(self) :
        return self.webDriverConfiguration.getDriverType()

    def isCloudDriver(self) :
        return self.webDriverConfiguration.getDriverType() == "cloudDriver"

    def getPlatforms(self) :
        return self.webDriverConfiguration.getActivePlatforms()

    def createBuildName(self, buildPrefix:str):
        if(os.getenv('DEFAULT_BUILD_ENV_NAME')):
            return os.getenv('DEFAULT_BUILD_ENV_NAME')
        
        if (buildPrefix == "" or buildPrefix is None) :
            buildPrefix = self.DEFAULT_BUILD_NAME
        
        buildName = buildPrefix

        buildSuffix = self.defaultBuildSuffix
        if (os.getenv("BUILD_ID")) :
            buildSuffix = os.getenv("BUILD_ID")

        return buildName + "-" + buildSuffix