package com.example.quartz.jobs.init;

import com.example.quartz.configuration.manager.JobConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.jobs.entity.PushHotelJob;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.event.GenerateEventsTask;
import com.example.quartz.tasks.event.PushEventsTask;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
public class JobsFactory implements InitializingBean {

    @Autowired
    Scheduler scheduler;

    @Autowired
    IJobManage jobManage;

    @Autowired
    private PushEventsTask pushEventsTask;
    @Autowired
    private GenerateEventsTask generateEventsTask;
    @Autowired
    private JobConfigurationHelper jobConfigurationHelper;

    private void initialAllJobs() throws SchedulerException {
        // initial jobs through configurations from config server
        for (JobConfigurationMapper jobConfigurationMapper: jobConfigurationHelper.getJobConfigurationList()) {
            JobKey jobKey = new JobKey(jobConfigurationMapper.getJobName(),jobConfigurationMapper.getJobGroup());
            if(scheduler.checkExists(jobKey)){
                continue;
            }
            newQuartzJobs(jobConfigurationMapper, jobKey);
        }

    }

    public void addJobs(List<JobConfigurationMapper> jobConfigurationMapperList) throws SchedulerException {
        for (JobConfigurationMapper jobConfigurationMapper: jobConfigurationMapperList) {
            JobKey jobKey = new JobKey(jobConfigurationMapper.getJobName(),jobConfigurationMapper.getJobGroup());
//            if(scheduler.checkExists(jobKey)){
//                continue;
//            }
            newQuartzJobs(jobConfigurationMapper, jobKey);
        }
    }

    private void newQuartzJobs(JobConfigurationMapper jobConfigurationMapper, JobKey jobKey) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("generateEventsTask", generateEventsTask);
        jobDataMap.put("pushEventsTask", pushEventsTask);
        jobDataMap.put("jobConfigurationHelper", jobConfigurationHelper);
        jobDataMap.put("jobManage", jobManage);
        JobDetail singJob = JobBuilder.newJob(PushHotelJob.class)
                .withIdentity(jobKey)
                .withDescription("this is a job that push hotel price!")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
        TriggerKey triggerKey = new TriggerKey(jobConfigurationMapper.getTriggerName(), jobConfigurationMapper.getTriggerGroup());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startNow()
                .withDescription("job test trigger!")
                .withSchedule(CronScheduleBuilder.cronSchedule(jobConfigurationMapper.getPeriod()))
                .build();
        scheduler.scheduleJob(singJob, trigger);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        initialAllJobs();
    }
}
