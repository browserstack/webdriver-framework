from browserstack.local import Local
import os
import atexit
import threading
import string
import random
import logging
logger=logging.getLogger()

class LocalFactoryClass:
    instance = None 

    local = Local()
    localIdentifier : str = ""

    LocalFactoryClassLock = threading.Lock()

    def __init__(self, localOptions) :
        try :
            localIdentifier = ''.join(random.choices(string.ascii_uppercase + string.digits, k = 7))
            if "localIdentifier" in localOptions :
                localIdentifier = localOptions["localIdentifier"]

            localOptions["localIdentifier"] =  localIdentifier

            if (os.getenv("https_proxy") != None) : 
                self.setProxy("https", localOptions)
            else:
                if (os.getenv("http_proxy") != None) :
                    self.setProxy("http", localOptions)

            self.local.start(**localOptions)
            LocalFactoryClass.localIdentifier = localIdentifier

            logger.debug(f"Started BrowserStack Local with identifier {localIdentifier}.")
        except Exception as exception :
            logger.debug("Initialization of BrowserStack Local failed.")
            raise exception
    
    def setProxy(self, protocol:str, localOptions: dict) :
        proxyHost = os.getenv(protocol+"_proxyHost")
        proxyPort = os.getenv(protocol+"_proxyPort")
        proxyUser = os.getenv(protocol+"_proxyUser")
        proxyPassword = os.getenv(protocol+"_proxyPassword")
        proxyHostPrefix = "proxy"
        proxyUserPrefix = "proxy"
        if (proxyHost.startswith("local") or proxyHost.startswith("127.")):
            proxyHostPrefix = "localProxy"
            proxyUserPrefix = "-localProxy"
        
        localOptions[proxyHostPrefix+"Host"] = proxyHost
        localOptions[proxyHostPrefix+"Port"] =  proxyPort
        if (proxyUser != None or proxyUser != "") :
            localOptions[proxyUserPrefix+ "User"] = proxyUser
            localOptions[proxyUserPrefix+"Pass"] = proxyPassword
    
        logger.debug(f"Proxy configurations detected Host : {proxyHost}, Port : {proxyPort}, User: {proxyUser}." )

    @staticmethod
    def createInstance(args:dict) :
        #Make thread safe
        if (LocalFactoryClass.instance == None) :
            with LocalFactoryClass.LocalFactoryClassLock:
                if (LocalFactoryClass.instance == None) :
                    LocalFactoryClass.instance = LocalFactoryClass(args)
                    logger.debug("Starting Local Instance...")
                    atexit.register(LocalFactoryClass.stopLocal)
            
    @staticmethod
    def getInstance() :
        return LocalFactoryClass.instance

    @staticmethod
    def getLocalIdentifier():
        return LocalFactoryClass.instance.localIdentifier

    @staticmethod
    def stopLocal():
        if (LocalFactoryClass.instance.local.isRunning()):
            logger.debug("Stopping Local Instance")
            LocalFactoryClass.instance.local.stop()
        else:
            logger.debug("Local is not running [IGNORE]")
        