package com.browserstack.webdriver.core;

import com.browserstack.local.Local;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LocalFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFactory.class);

    private static volatile LocalFactory instance;

    private final Local local = new Local();
    private final String localIdentifier;

    private LocalFactory(Map<String, String> localOptions) {
        try {
            localIdentifier = localOptions.containsKey("localIdentifier")?localOptions.get("localIdentifier"):RandomStringUtils.randomAlphabetic(8);
            localOptions.put("localIdentifier", localIdentifier);
            if (System.getProperty("https.proxyHost") != null) {
                setProxy("https", localOptions);
            } else {
                if (System.getProperty("http.proxyHost") != null) {
                    setProxy("http", localOptions);
                }
            }
            local.start(localOptions);
            LOGGER.debug("Started BrowserStack Local with identifier {}.", localIdentifier);
        } catch (Exception e) {
            LOGGER.error("Initialization of BrowserStack Local failed.");
            throw new RuntimeException("Initialization of BrowserStack Local failed.", e);
        }
    }

    private void setProxy(String protocol, Map<String, String> localOptions) {
        String proxyHost = System.getProperty(String.format("%s.proxyHost", protocol));
        String proxyPort = System.getProperty(String.format("%s.proxyPort", protocol));
        String proxyUser = System.getProperty(String.format("%s.proxyUser", protocol));
        String proxyPassword = System.getProperty(String.format("%s.proxyPassword", protocol));
        String proxyHostPrefix = "proxy";
        String proxyUserPrefix = "proxy";
        if (proxyHost.startsWith("local") || proxyHost.startsWith("127.")) {
            proxyHostPrefix = "localProxy";
            proxyUserPrefix = "-localProxy";
        }
        localOptions.put(String.format("%sHost", proxyHostPrefix), proxyHost);
        localOptions.put(String.format("%sPort", proxyHostPrefix), proxyPort);
        if (StringUtils.isNotEmpty(proxyUser)) {
            localOptions.put(String.format("%sUser", proxyUserPrefix), proxyUser);
            localOptions.put(String.format("%sPass", proxyUserPrefix), proxyPassword);
        }
        LOGGER.debug("Proxy configurations detected Host : {}, Port : {}, User: {}.", proxyHost, proxyPort, proxyUser);
    }

    public static void createInstance(Map<String, String> args) {
        if (instance == null) {
            synchronized (LocalFactory.class) {
                if (instance == null) {
                    instance = new LocalFactory(args);
                    Runtime.getRuntime().addShutdownHook(new Closer(instance.local));
                }
            }
        }
    }

    public static LocalFactory getInstance() {
        return instance;
    }

    public String getLocalIdentifier() {
        return instance.localIdentifier;
    }

    private static class Closer extends Thread {
        private final Local local;

        public Closer(Local local) {
            this.local = local;
        }

        @Override
        public void run() {
            try {
                if (local.isRunning()) {
                    local.stop();
                }
            } catch (Exception e) {
               LOGGER.error("Error encountered while stopping BrowserStack Local { }",e);
            }
        }
    }
}