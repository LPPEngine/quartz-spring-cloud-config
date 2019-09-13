package com.example.quartz.jobs.init;

import com.example.quartz.configuration.helper.JobConfigurationHelper;
import com.example.quartz.configuration.helper.JobConfigurationMapper;
import com.example.quartz.enums.JobTypeEnum;
import com.example.quartz.jobs.entity.PushHotelJob;
import com.example.quartz.jobs.manage.IJobManage;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
@PersistJobDataAfterExecution
public class JobsFactory implements Serializable {
    /**
     * We must inject springboot auto configurable quartz scheduler,else we can not inject spring beans in quartz job class
     */
    @Resource
    Scheduler quartzScheduler;

    public void initialAllJobs(List<JobConfigurationMapper> jobConfigurationMapperList) throws SchedulerException {
        // initial jobs through configurations from config server
        for (JobConfigurationMapper jobConfigurationMapper: jobConfigurationMapperList) {
            JobKey jobKey = new JobKey(jobConfigurationMapper.getJobName(),jobConfigurationMapper.getJobGroup());
            if(quartzScheduler.checkExists(jobKey)){
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
        JobDetail singJob = JobBuilder.newJob(JobTypeEnum.getJobClass(jobConfigurationMapper.getJobType()))
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
        // if cluster, we need to consider thread safe such as multiple node to add jobs or delete jobs,or change job configuration.
        // What we can do to prevent the safe problem occurring
        quartzScheduler.scheduleJob(singJob, trigger);
    }
}
