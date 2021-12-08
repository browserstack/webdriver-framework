import CommonCapabilities
# import Capabilities

# caps = Capabilities.CapabilitiesClass()

# caps.setCapabilities("User1","Princeton")
# caps.setCapabilities("User2","Rajan")

# print(caps.getCapabilityMap())

# commCaps = CommonCapabilities.CommonCapabiltiesClass()
# commCaps.setProject("ProjectName")
# commCaps.setBuildPrefix("BuildName")

# print(commCaps.getProject() + commCaps.getBuildPrefix())

# commCaps.setCapabilities(caps)

# print("**********")

# print(commCaps.getCapabilities().getCapabilityMap())

# import DriverType
# import enum 

# driver : DriverType.DriverTypeClass = DriverType.DriverTypeClass["cloudDriver"]


# if(driver == DriverType.DriverTypeClass.cloudDriver):
#     print("true")
# else:
#     print("false")
import sys
from pathlib import Path
sys.path.append(str(Path('.').absolute().parent))
from core import WebDriverFactory

fact = WebDriverFactory.WebDriverFactoryClass()
