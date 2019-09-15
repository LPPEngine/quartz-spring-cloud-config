package com.example.quartz.jobs.manage;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.configuration.helper.PushHotelJobConfigurationMapper;
import org.quartz.TriggerKey;

import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
public interface IJobManage {

    void add(List<BaseMapper> jobConfigurationMapperList);

    void delete(String jobKey);

    void modify(TriggerKey triggerKey, BaseMapper pushHotelJobConfigurationMapper);

    void select(String jobKey);

    <E extends BaseMapper> void jobsChange(List<E> jobConfigurationMapperList);
}
