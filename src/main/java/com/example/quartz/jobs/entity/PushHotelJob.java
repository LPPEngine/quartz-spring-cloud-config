package com.example.quartz.jobs.entity;

import com.example.quartz.configuration.manager.JobConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.event.GenerateEventsTask;
import com.example.quartz.tasks.event.PushEventsTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PushHotelJob extends QuartzJobBean {
    @Autowired
    private PushEventsTask pushEventsTask;
    @Autowired
    private GenerateEventsTask generateEventsTask;
    @Autowired
    private JobConfigurationHelper jobConfigurationHelper;
    @Autowired
    private IJobManage jobManage;

    public JobConfigurationHelper getJobConfigurationHelper() {
        return jobConfigurationHelper;
    }

    public void setJobConfigurationHelper(JobConfigurationHelper jobConfigurationHelper) {
        this.jobConfigurationHelper = jobConfigurationHelper;
    }

    public IJobManage getJobManage() {
        return jobManage;
    }

    public void setJobManage(IJobManage jobManage) {
        this.jobManage = jobManage;
    }

    public PushEventsTask getPushEventsTask() {
        return pushEventsTask;
    }

    public void setPushEventsTask(PushEventsTask pushEventsTask) {
        this.pushEventsTask = pushEventsTask;
    }


    public GenerateEventsTask getGenerateEventsTask() {
        return generateEventsTask;
    }

    public void setGenerateEventsTask(GenerateEventsTask generateEventsTask) {
        this.generateEventsTask = generateEventsTask;
    }


    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        //get jobConfigurationMapper firstly
        JobConfigurationMapper jobConfigurationMapper = jobConfigurationHelper.getJobConfigurationList().stream()
                .filter(jobConfiguration -> jobConfiguration.getJobGroup().equals(jobKey.getGroup()) && jobConfiguration.getJobName().equals(jobKey.getName()))
                .findFirst()
                .orElse(null);

        if (jobConfigurationMapper != null) {
            System.out.println("******************* JobKey:" + jobKey + "*****************************");
            System.out.println("******************* PushHotelJob ***************************");
            String event = generateEventsTask.generateEvents(jobConfigurationMapper);
            pushEventsTask.push(event);
            System.out.println("******************************end*******************************");
            System.out.println(jobConfigurationMapper.getJobName());
            System.out.println();
            System.out.println();

            CronTrigger  cronTrigger  = (CronTrigger) jobExecutionContext.getTrigger();
            String period = cronTrigger.getCronExpression();
            //judge the trigger period whether has changed
            if(!period.equals(jobConfigurationMapper.getPeriod())) {
                jobManage.modify(jobExecutionContext.getTrigger().getKey(), jobConfigurationMapper);
            }
        }
    }
}
