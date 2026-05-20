package com.e_commerce.e_commerce_api.utils;

import lombok.experimental.UtilityClass;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class DeviceInfoUtils {

    private static final UserAgentAnalyzer analyzer =
            UserAgentAnalyzer.newBuilder()
                    .hideMatcherLoadStats()
                    .withCache(1000)
                    .build();

    public static Map<String, String> parse(String userAgentString) {

        UserAgent agent = analyzer.parse(userAgentString);

        Map<String, String> deviceInfo = new HashMap<>();

        deviceInfo.put("os",
                agent.getValue("OperatingSystemNameVersion"));

        deviceInfo.put("browser",
                agent.getValue("AgentNameVersion"));

        deviceInfo.put("device",
                agent.getValue("DeviceName"));

        deviceInfo.put("type",
                agent.getValue("DeviceClass"));

        return deviceInfo;
    }

    public static String getDeviceInfo(String userAgentString) {
        if (userAgentString == null || userAgentString.isEmpty()) {
            return "Unknown Device";
        }
        UserAgent agent = analyzer.parse(userAgentString);
        String os = agent.getValue("OperatingSystemNameVersion");
        String browser = agent.getValue("AgentNameVersion");
        String device = agent.getValue("DeviceName");
        
        return String.format("%s - %s (%s)", os, browser, device);
    }
}