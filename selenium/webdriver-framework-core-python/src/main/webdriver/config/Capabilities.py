class CapabilitiesClass:

    __capabilityMap     : dict = {}

    def setCapabilities(self, key, value):
        self.__capabilityMap[key] = value

    def getCapabilityMap(self):
        return self.__capabilityMap