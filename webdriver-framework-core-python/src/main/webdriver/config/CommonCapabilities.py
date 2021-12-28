from config import Capabilities

class CommonCapabiltiesClass:

    __project       : str 
    __buildPrefix   : str 
    __capabilities  : Capabilities.CapabilitiesClass 

    def getProject(self):
        return self.__project

    def setProject(self, project: str):
        self.__project = project
    

    def getBuildPrefix(self):
        return self.__buildPrefix

    def setBuildPrefix(self, buildPrefix : str):
        self.__buildPrefix = buildPrefix
    

    def getCapabilities(self):
        return self.__capabilities
    

    def setCapabilities(self, capabilities : Capabilities.CapabilitiesClass):
        self.__capabilities = capabilities