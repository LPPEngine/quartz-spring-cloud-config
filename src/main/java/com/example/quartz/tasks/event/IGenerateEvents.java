package com.example.quartz.tasks.event;

import com.example.quartz.configuration.helper.JobConfigurationMapper;

public interface IGenerateEvents {
    String generateEvents(JobConfigurationMapper jobConfigurationMapper);
}
