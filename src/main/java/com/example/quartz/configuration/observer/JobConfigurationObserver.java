package com.example.quartz.configuration.observer;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.jobs.manage.IJobManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobConfigurationObserver implements Observer {

    @Autowired
    private IJobManage jobManage;
    @Override
    public <E extends BaseMapper> void jobConfigurationChange(List<E> jobConfigurationMapperList,String jobGroup) {
        jobManage.jobsChange(jobConfigurationMapperList,jobGroup);
    }
}
