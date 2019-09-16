package com.example.quartz.configuration.helper;

import com.example.quartz.configuration.observer.Observer;
import com.example.quartz.jobs.init.JobsFactory;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@ConfigurationProperties
@Component
@Data
public class JobConfigurationHelper implements Serializable, InitializingBean {

    @Autowired
    private JobsFactory jobsFactory;

    @Autowired
    private Observer jobConfigurationObserver;

    private static boolean jobConfigurationChange = false;

    /**
     *     we should init the capacity if we know the configurationList size
     */
    private List<PushHotelJobConfigurationMapper> pushHotelJobConfigurationList = new ArrayList<>();

    /**
     *     we should init the capacity if we know the configurationList size
     */
    private List<FeedJobConfigurationMapper> feedJobConfigurationList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!jobConfigurationChange){
            jobsFactory.initialAllJobs(this.getPushHotelJobConfigurationList());
            jobsFactory.initialAllJobs(this.getFeedJobConfigurationList());
        }else {
            //notify observer
            jobConfigurationObserver.jobConfigurationChange(this.getPushHotelJobConfigurationList());
            jobConfigurationObserver.jobConfigurationChange(this.getFeedJobConfigurationList());
            jobConfigurationChange = false;
        }
    }
    /**
     *     Todo: add a job/jobs or delete a job/jobs through detect configurations
     */
    @EventListener(EnvironmentChangeEvent.class)
    public void jobConfigurationListener(){
        jobConfigurationChange = true;
    }
}
