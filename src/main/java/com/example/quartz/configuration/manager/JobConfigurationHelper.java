package com.example.quartz.configuration.manager;

import com.example.quartz.jobs.init.JobsFactory;
import com.example.quartz.jobs.manage.IJobManage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@ConfigurationProperties
@Component
public class JobConfigurationHelper implements InitializingBean, Serializable {

//    @Autowired
//    private JobsFactory jobsFactory;

    @Autowired
    private IJobManage jobManage;

    private boolean jobConfigurationChanged;


    private final Map<String,String> map = new HashMap<>();

    private List<JobConfigurationMapper> jobConfigurationList = new ArrayList<>();



    public Map<String, String> getMap() {
        return map;
    }

    public List<JobConfigurationMapper> getJobConfigurationList() {
        return jobConfigurationList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (jobConfigurationChanged) {
            jobManage.jobsChange();
            jobConfigurationChanged = false;
        }
    }

    /**
     *     Todo: when configuration change , will refresh relative jobs configuration
     */
    @EventListener(EnvironmentChangeEvent.class)
    public void refreshProperties(){
        jobConfigurationChanged = true;
        System.out.println("job configurations have changed");
    }
}
