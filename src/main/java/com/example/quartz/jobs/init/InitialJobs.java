package com.example.quartz.jobs.init;

import com.example.quartz.configuration.manager.ConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.configuration.manager.LPPEngineProperties;
import com.example.quartz.jobs.entity.PushHotelJob;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.event.GenerateEventsTask;
import com.example.quartz.tasks.SongTextShow;
import com.example.quartz.tasks.event.PushEventsTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
public class InitialJobs {

    @Autowired
    Scheduler scheduler;

    @Autowired
    IJobManage jobManage;

    @Autowired
    SongTextShow songTextShow;
    @Autowired
    ConfigurationHelper configurationHelper;
    @Autowired
    LPPEngineProperties lppEngineProperties;
    @Autowired
    private PushEventsTask pushEventsTask;
    @Autowired
    private GenerateEventsTask generateEventsTask;


    public void initialAllJobs(Map map) throws SchedulerException {
        if(!CollectionUtils.isEmpty(map)){
            JobKey jobKey = new JobKey(map.get("jobName").toString());
            if(scheduler.checkExists(jobKey)){
                return;
            }
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("songTextShow",songTextShow);
            jobDataMap.put("configurationHelper",configurationHelper);
            jobDataMap.put("singer",configurationHelper.getSinger());
//        jobDataMap.put("song",configurationHelper.getSong());
            jobDataMap.put("song",lppEngineProperties.getSong());
//            jobDataMap.put("generateEventsTask", generateEventsTask);
//        jobDataMap.put("song",environment.getProperty("song"));
//            jobDataMap.put("jobConfigurationHelper",jobConfigurationHelper);
            JobDetail singJob = JobBuilder.newJob(PushHotelJob.class)
                    .withIdentity(jobKey)
                    .withDescription(map.get("jobDescrition").toString())
                    .setJobData(jobDataMap)
                    .storeDurably()
                    .build();
            TriggerKey triggerKey = new TriggerKey(map.get("triggerName").toString());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startNow()
                    .withDescription(map.get("triggerDescription").toString())
                    .withSchedule(CronScheduleBuilder.cronSchedule(map.get("period").toString()))
                    .build();
            scheduler.scheduleJob(singJob,trigger);
        }
//        jobManage.add(allPropertiesHelper.getAllProperties());
    }

    public void initialAllJobs(List<JobConfigurationMapper> jobConfigurationList) throws SchedulerException {
        int i = 0;
        // initial jobs through configurations from config server
        for (JobConfigurationMapper jobConfigurationMapper: jobConfigurationList) {
            JobKey jobKey = new JobKey(jobConfigurationMapper.getJobName(),jobConfigurationMapper.getJobGroup());
            if(scheduler.checkExists(jobKey)){
                continue;
            }
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("generateEventsTask", generateEventsTask);
            jobDataMap.put("pushEventsTask", pushEventsTask);
            jobDataMap.put("jobConfigurationMapper",jobConfigurationMapper);
            jobDataMap.put("jobManage",jobManage);
            JobDetail singJob = JobBuilder.newJob(PushHotelJob.class)
                    .withIdentity(jobKey)
                    .withDescription("this is a job that sing a song!")
                    .setJobData(jobDataMap)
                    .storeDurably()
                    .build();
            TriggerKey triggerKey = new TriggerKey("job"+ ++i + "Trigger");
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startNow()
                    .withDescription("job test trigger!")
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobConfigurationMapper.getPeriod()))
                    .build();
            scheduler.scheduleJob(singJob,trigger);
        }

    }


}
