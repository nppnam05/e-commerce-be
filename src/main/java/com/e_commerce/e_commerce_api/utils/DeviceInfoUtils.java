package com.e_commerce.e_commerce_api.utils;

import lombok.experimental.UtilityClass;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class DeviceInfoUtils {

        private static final UserAgentAnalyzer analyzer = UserAgentAnalyzer.newBuilder()
                        .hideMatcherLoadStats()
                        .withCache(1000)
                        .build();

        public static Map<String, String> parse(String userAgentString) {

                if (userAgentString == null || userAgentString.isBlank()) {
                        return Map.of(
                                        "os", "Unknown",
                                        "browser", "Unknown",
                                        "deviceType", "Unknown");
                }

                UserAgent agent = analyzer.parse(userAgentString);

                Map<String, String> info = new HashMap<>();

                info.put("os",
                                agent.getValue("OperatingSystemNameVersion"));

                info.put("browser",
                                agent.getValue("AgentNameVersion"));

                info.put("deviceType",
                                agent.getValue("DeviceClass"));

                return info;
        }
}