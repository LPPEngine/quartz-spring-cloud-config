package com.example.quartz.configuration.observer;

import com.example.quartz.configuration.helper.JobConfigurationMapper;

import java.util.List;

public interface Observer {

    void jobConfigurationChange(List<JobConfigurationMapper> jobConfigurationMapperList);
}
