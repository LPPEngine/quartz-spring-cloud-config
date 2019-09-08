package com.example.quartz.configuration.manager;

import com.example.quartz.jobs.init.InitialJobs;
import com.example.quartz.jobs.manage.IJobManage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@ConfigurationProperties
@Component
public class JobConfigurationHelper implements InitializingBean {

    @Autowired
    private InitialJobs initialJobs;

    @Autowired
    private IJobManage jobManage;


    private final Map<String,String> map = new HashMap<>();

    private final List<JobConfigurationMapper> jobConfigurationList = new ArrayList<>();

    public Map<String, String> getMap() {
        return map;
    }

    public List<JobConfigurationMapper> getJobConfigurationList() {
        return jobConfigurationList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialJobs.initialAllJobs(jobConfigurationList);
    }

    /**
     *     Todo: when configuration change , will refresh relative jobs configuration
     */
    @EventListener(EnvironmentChangeEvent.class)
    public void refreshProperties(){
        jobManage.jobsChange(jobConfigurationList);
    }
}
