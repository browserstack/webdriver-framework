class LocalTunnelConfigClass:

    __localOptions  : dict = {}
    __enabled       : bool

    def isEnabled(self):
        return self.__enabled

    def setEnabled(self, enable: bool):
        self.__enabled = enable

    def getLocalOptions(self) :
        return self.__localOptions

    def setLocalOption(self, key: str, value: str):
        self.__localOptions[key] = value