package com.fengwenyi.springboothbase;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Erwin Feng
 * @since 2019-03-28 16:14
 */
@ConfigurationProperties(prefix = "hbase")
public class HbaseProperties {
    private Map<String, String> config;
    public Map<String, String> getConfig() {
        return config;
    }
    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
