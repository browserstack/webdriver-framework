import setuptools

setuptools.setup(name='webdriverFrameworkCorePython',
version='0.1',
description='Webdriver Manager for Python',
url='https://github.com/browserstack/webdriver-framework/tree/webdriver-framework-core-python',
author='Princeton Baretto',
install_requires=['selenium==3.141.0','browserstack-local','pyaml'],
author_email='princeton.b@browserstack.com',
packages=setuptools.find_packages(),
zip_safe=False)