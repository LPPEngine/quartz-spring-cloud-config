package com.example.quartz.jobs.entity;

import com.example.quartz.configuration.manager.JobConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.event.GenerateEventsTask;
import com.example.quartz.tasks.event.PushEventsTask;
import org.quartz.*;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PushHotelJob implements Job {

    private PushEventsTask pushEventsTask;
    private GenerateEventsTask generateEventsTask;
    private static JobConfigurationHelper jobConfigurationHelper;
    private String singer;
    private String song;
    private IJobManage jobManage;

    public JobConfigurationHelper getJobConfigurationHelper() {
        return jobConfigurationHelper;
    }

    public void setJobConfigurationHelper(JobConfigurationHelper jobConfigurationHelper) {
        PushHotelJob.jobConfigurationHelper = jobConfigurationHelper;
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


//    public JobConfigurationMapper getJobConfigurationMapper() {
//        return jobConfigurationMapper;
//    }
//
//    public void setJobConfigurationMapper(JobConfigurationMapper jobConfigurationMapper) {
//        this.jobConfigurationMapper = jobConfigurationMapper;
//    }

    public GenerateEventsTask getGenerateEventsTask() {
        return generateEventsTask;
    }

    public void setGenerateEventsTask(GenerateEventsTask generateEventsTask) {
        this.generateEventsTask = generateEventsTask;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
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
