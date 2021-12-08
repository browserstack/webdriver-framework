from _typeshed import Self
import CommonCapabilities
import LocalTunnelConfig

class RemoteDriverConfigClass :

    __hubUrl             : str
    __user               : str
    __accessKey          : str
    __commonCapabilities : CommonCapabilities.CommonCapabiltiesClass
    __localTunnel        : LocalTunnelConfig.LocalTunnelConfigClass
    __platforms          : list

    def getHubUrl(self):
        return self.__hubUrl

    def setHubUrl(self, hubUrl : str):
        self.__hubUrl = hubUrl

    def getUser(self):
        return self.__user

    def setUser(self, user : str):
        self.__user = user

    def getAccessKey(self):
        return self.__accessKey

    def setAccessKey(self, accessKey : str):
        self.__accessKey = accessKey

    def getCommonCapabilities(self):
        return self.__commonCapabilities

    def setCommonCapabilities(self, commonCapabilities : CommonCapabilities.CommonCapabiltiesClass):
        self.__commonCapabilities = commonCapabilities

    def getLocalTunnel(self):
        return self.__localTunnel

    def setLocalTunnel(self, localTunnel : LocalTunnelConfig.LocalTunnelConfigClass):
        self.__localTunnel = localTunnel

    def getPlatforms(self):
        return self.__platforms

    def setPlatforms(self, platforms : list):
        self.__platforms = platforms