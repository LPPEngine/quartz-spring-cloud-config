package com.example.quartz.configuration.manager;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@ConfigurationProperties
@Component
public class JobConfigurationHelper implements Serializable {


    private final Map<String,String> map = new HashMap<>();

    private List<JobConfigurationMapper> jobConfigurationList = new ArrayList<>();


    public Map<String, String> getMap() {
        return map;
    }

    public List<JobConfigurationMapper> getJobConfigurationList() {
        return jobConfigurationList;
    }

}
