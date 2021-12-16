from browserstack.local import Local
import os
import atexit
import threading
import string
import random

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

            if (os.getenv("https.proxyHost") != None) :
                self.setProxy("https", localOptions)
            else:
                if (os.getenv("http.proxyHost") != None) :
                    self.setProxy("http", localOptions)

            self.local.start(**localOptions)
            LocalFactoryClass.localIdentifier = localIdentifier

            print(f"Started BrowserStack Local with identifier {localIdentifier}.")
        except Exception as e :
            print("Initialization of BrowserStack Local failed.")
            print(e)
            # throw new RuntimeException("Initialization of BrowserStack Local failed.", e);
    
    def setProxy(protocol:str, localOptions: dict) :
        proxyHost = os.getenv(protocol+".proxyHost")
        proxyPort = os.getenv(protocol+".proxyPort")
        proxyUser = os.getenv(protocol+".proxyUser")
        proxyPassword = os.getenv(protocol+".proxyPassword")
        proxyHostPrefix = "proxy"
        proxyUserPrefix = "proxy"
        if (proxyHost.startsWith("local") or proxyHost.startsWith("127.")):
            proxyHostPrefix = "localProxy"
            proxyUserPrefix = "-localProxy"
        
        localOptions[proxyHostPrefix+"Host"] = proxyHost
        localOptions[proxyHostPrefix+"Port"] =  proxyPort
        if (proxyUser != None or proxyUser != "") :
            localOptions[proxyUserPrefix+ "User"] = proxyUser
            localOptions[proxyUserPrefix+"Pass"] = proxyPassword
    
        print(f"Proxy configurations detected Host : {proxyHost}, Port : {proxyPort}, User: {proxyUser}." )

    @staticmethod
    def createInstance(args:dict) :
        #Make thread safe
        if (LocalFactoryClass.instance == None) :
            with LocalFactoryClass.LocalFactoryClassLock:
                if (LocalFactoryClass.instance == None) :
                    LocalFactoryClass.instance = LocalFactoryClass(args)
                    print("Starting Local Instance...")
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
            print("Stopping Local Instance")
            LocalFactoryClass.instance.local.stop()
        else:
            print("Local is not running [IGNORE]")
        