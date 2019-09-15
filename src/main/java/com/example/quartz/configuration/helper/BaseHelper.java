package com.example.quartz.configuration.helper;

import com.example.quartz.configuration.observer.Observer;
import com.example.quartz.jobs.init.JobsFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseHelper {
    @Autowired
    JobsFactory jobsFactory;
    @Autowired
    Observer jobConfigurationObserver;

    /**
     * get job configurations
     * @param <E> jobConfigurationMapper
     * @return job configurations
     */
    public abstract <E extends BaseMapper> List<E> getJobConfigurationList();
}
