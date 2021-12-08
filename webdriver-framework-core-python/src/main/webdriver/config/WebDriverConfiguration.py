import DriverType
import OnPremDriverConfig
import RemoteDriverConfig

class WebDriverConfigurationClass:

    __testEndpoint              :str
    __namedTestUrls             :dict = {}
    __driverType                : DriverType.DriverTypeClass
    __onPremDriverConfig        : OnPremDriverConfig.OnPremDriverConfigClass
    __onPremGridDriverConfig    : RemoteDriverConfig.RemoteDriverConfigClass
    __cloudDriverConfig         : RemoteDriverConfig.RemoteDriverConfigClass

    def getActivePlatforms(self):
        activePlatforms = []

        if(self.__driverType == DriverType.DriverTypeClass.onPremDriver ):
            activePlatforms = self.__onPremDriverConfig.getPlatforms()

        elif(self.__driverType == DriverType.DriverTypeClass.onPremGridDriver):
            activePlatforms = self.__onPremGridDriverConfig.getPlatforms()

        elif(self.__driverType == DriverType.DriverTypeClass.cloudDriver):
            activePlatforms = self.__cloudDriverConfig.getPlatforms()

        return activePlatforms

    def getTestEndpoint(self):
        return self.__testEndpoint

    def setTestEndpoint(self, testEndpoint :str ):
        self.__testEndpoint = testEndpoint

    def getDriverType(self):
        return self.__driverType

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