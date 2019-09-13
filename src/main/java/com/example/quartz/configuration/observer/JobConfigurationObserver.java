package com.example.quartz.configuration.observer;

import com.example.quartz.configuration.helper.JobConfigurationMapper;
import com.example.quartz.jobs.manage.IJobManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobConfigurationObserver implements Observer {

    @Autowired
    private IJobManage jobManage;
    @Override
    public void jobConfigurationChange(List<JobConfigurationMapper> jobConfigurationMapperList) {
        jobManage.jobsChange(jobConfigurationMapperList);
    }
}
