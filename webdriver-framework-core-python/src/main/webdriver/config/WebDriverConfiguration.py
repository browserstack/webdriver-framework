from config import DriverType
from config import OnPremDriverConfig
from config import RemoteDriverConfig

class WebDriverConfigurationClass:

    __testEndpoint              :str = None
    __namedTestUrls             :dict = {} 
    __driverType                : DriverType.DriverTypeClass = None
    __onPremDriverConfig        : OnPremDriverConfig.OnPremDriverConfigClass = None
    __onPremGridDriverConfig    : RemoteDriverConfig.RemoteDriverConfigClass = None
    __cloudDriverConfig         : RemoteDriverConfig.RemoteDriverConfigClass = None

    def getActivePlatforms(self):
        activePlatforms = []

        if(self.__driverType.name == "onPremDriver" ):
            activePlatforms = self.__onPremDriverConfig.getPlatforms()

        elif(self.__driverType.name == "onPremGridDriver"):
            activePlatforms = self.__onPremGridDriverConfig.getPlatforms()

        elif(self.__driverType.name == "cloudDriver"):
            activePlatforms = self.__cloudDriverConfig.getPlatforms()

        return activePlatforms

    def getTestEndpoint(self):
        return self.__testEndpoint

    def setTestEndpoint(self, testEndpoint :str ):
        self.__testEndpoint = testEndpoint

    def getDriverType(self):
        return self.__driverType.name

    def setDriverType(self, driverType : DriverType.DriverTypeClass):
        self.__driverType = driverType

    def getOnPremDriverConfig(self):
        return self.__onPremDriverConfig

    def setOnPremDriverConfig(self, onPremDriverConfig : OnPremDriverConfig):
        self.__onPremDriverConfig = onPremDriverConfig

    def getOnPremGridDriverConfig(self) :
        return self.__onPremGridDriverConfig

    def setOnPremGridDriverConfig(self, onPremGridDriverConfig: RemoteDriverConfig):
        self.__onPremGridDriverConfig = onPremGridDriverConfig

    def getCloudDriverConfig(self):
        return self.__cloudDriverConfig

    def setCloudDriverConfig(self, cloudDriverConfig: RemoteDriverConfig):
        self.__cloudDriverConfig = cloudDriverConfig

    def setNamedTestUrls(self, key: str, value: str):
        self.__namedTestUrls[key] = value

    def getNamedTestUrls(self):
        return self.__namedTestUrls

    def getNamedUrl(self, name: str):
        if (name == None or name == ""):
            return ""
        return self.__namedTestUrls[name]