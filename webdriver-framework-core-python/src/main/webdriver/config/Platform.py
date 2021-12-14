import Capabilities

class PlatformClass:

    __name           : str
    __os             : str
    __osVersion      : str
    __browser        : str
    __browserVersion : str
    __device         : str
    __realMobile     : bool
    __driverPath     : str
    __capabilities   : Capabilities.CapabilitiesClass

    def getName(self):
        return self.__name

    def setName(self, name:str):
        self.__name = name

    def getOs(self):
        return self.__os

    def setOs(self, os:str):
        self.__os = os

    def getOsVersion(self):
        return self.__osVersion

    def setOsVersion(self, osVersion:str):
        self.__osVersion = osVersion

    def getBrowser(self):
        return self.__browser

    def setBrowser(self, browser:str):
        self.__browser = browser

    def getBrowserVersion(self):
        return self.__browserVersion

    def setBrowserVersion(self, browserVersion:str):
        self.__browserVersion = browserVersion

    def getDevice(self) :
        return self.__device
    

    def setDevice(self, device:str) :
        self.__device = device
    

    def getRealMobile(self) :
        return self.__realMobile
    

    def setRealMobile(self, realMobile:bool) :
        self.__realMobile = realMobile
    

    def getDriverPath(self) :
        return self.__driverPath
    

    def setDriverPath(self, driverPath:str) :
        self.__driverPath = driverPath
    

    def getCapabilities(self) :
        return self.__capabilities
    

    def setCapabilities(self, capabilities: Capabilities.CapabilitiesClass) :
        self.__capabilities = capabilities