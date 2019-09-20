package com.example.quartz.jobs.init;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.enums.JobTypeEnum;
import org.quartz.*;
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
     * We must inject springboot auto configurable quartz scheduler,otherwise we can not inject spring beans in quartz job class
     */
    @Resource
    Scheduler quartzScheduler;

    public <E extends BaseMapper> void initialAllJobs(List<E> jobConfigurationMapperList) throws SchedulerException {
        // initial jobs through configurations from config server
        for (BaseMapper jobConfigurationMapper : jobConfigurationMapperList) {
            JobKey jobKey = new JobKey(jobConfigurationMapper.getJobName(), jobConfigurationMapper.getJobGroup());
            if(quartzScheduler.checkExists(jobKey)){
                continue;
            }
            // Todo: get lock successfully(mysql row lock or redis lock)
            newQuartzJobs(jobConfigurationMapper, jobKey);
            // Todo: release lock successfully
        }

    }

    public void addJobs(List<BaseMapper> jobConfigurationMapperList) throws SchedulerException {
        for (BaseMapper jobConfigurationMapper : jobConfigurationMapperList) {
            JobKey jobKey = new JobKey(jobConfigurationMapper.getJobName(), jobConfigurationMapper.getJobGroup());
//            if(scheduler.checkExists(jobKey)){
//                continue;
//            }
            newQuartzJobs(jobConfigurationMapper, jobKey);
        }
    }

    private void newQuartzJobs(BaseMapper jobConfigurationMapper, JobKey jobKey) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        JobDetail job = JobBuilder.newJob(JobTypeEnum.getJobClass(jobConfigurationMapper.getJobType()))
                .withIdentity(jobKey)
                .withDescription("this is a job that push hotel price or feed job......!")
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
        quartzScheduler.scheduleJob(job, trigger);
    }
}
