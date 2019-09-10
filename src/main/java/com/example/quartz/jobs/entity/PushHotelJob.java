package com.example.quartz.jobs.entity;

import com.example.quartz.configuration.manager.ConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.event.GenerateEventsTask;
import com.example.quartz.tasks.SongTextShow;
import com.example.quartz.tasks.event.PushEventsTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PushHotelJob implements Job {

    private PushEventsTask pushEventsTask;
    private ConfigurationHelper configurationHelper;
    private GenerateEventsTask generateEventsTask;
//    private JobConfigurationMapper jobConfigurationMapper;
    private static JobConfigurationHelper jobConfigurationHelper;
    private SongTextShow songTextShow;
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


    public ConfigurationHelper getConfigurationHelper() {
        return configurationHelper;
    }

    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }

    public SongTextShow getSongTextShow() {
        return songTextShow;
    }

    public void setSongTextShow(SongTextShow songTextShow) {
        this.songTextShow = songTextShow;
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

//        jobConfigurationHelper.getJobConfigurationList().forEach(jobConfigurationMapper ->{
//            if(jobKey.toString().equals(jobConfigurationMapper.getJobGroup() + '.' + jobConfigurationMapper.getJobName())){
//                this.jobConfigurationMapper = jobConfigurationMapper;
//                jobDataMap.put("jobConfigurationMapper",jobConfigurationMapper);
//            }
//        });
        //get jobConfigurationMapper
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
