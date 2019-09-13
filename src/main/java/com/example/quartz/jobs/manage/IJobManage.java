package com.example.quartz.jobs.manage;

import com.example.quartz.configuration.helper.JobConfigurationMapper;
import org.quartz.TriggerKey;

import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
public interface IJobManage {

    void add(List<JobConfigurationMapper> jobConfigurationMapperList);

    void delete(String jobKey);

    void modify(TriggerKey triggerKey, JobConfigurationMapper jobConfigurationMapper);

    void select(String jobKey);

    void jobsChange(List<JobConfigurationMapper> jobConfigurationMapperList);
}
