import CommonCapabilities
import Capabilities

caps = Capabilities.CapabilitiesClass()

caps.setCapabilities("User1","Princeton")
caps.setCapabilities("User2","Rajan")

print(caps.getCapabilityMap())

commCaps = CommonCapabilities.CommonCapabiltiesClass()
commCaps.setProject("ProjectName")
commCaps.setBuildPrefix("BuildName")

print(commCaps.getProject() + commCaps.getBuildPrefix())

commCaps.setCapabilities(caps)

print("**********")

print(commCaps.getCapabilities().getCapabilityMap())
