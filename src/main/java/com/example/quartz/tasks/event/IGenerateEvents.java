package com.example.quartz.tasks.event;

import com.example.quartz.configuration.helper.BaseMapper;

public interface IGenerateEvents {
    <T extends BaseMapper> String generateEvents(T jobConfigurationMapper);
}
