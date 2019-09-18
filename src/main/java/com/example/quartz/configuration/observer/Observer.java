package com.example.quartz.configuration.observer;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.configuration.helper.PushHotelJobConfigurationMapper;

import java.util.List;

public interface Observer {

    <E extends BaseMapper> void jobConfigurationChange(List<E> jobConfigurationMapperList,String jobGroup);
}
