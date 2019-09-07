package com.example.quartz.jobs.entity;

import com.example.quartz.configuration.manager.ConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
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

    public PushEventsTask getPushEventsTask() {
        return pushEventsTask;
    }

    public void setPushEventsTask(PushEventsTask pushEventsTask) {
        this.pushEventsTask = pushEventsTask;
    }

    private ConfigurationHelper configurationHelper;
    private GenerateEventsTask generateEventsTask;
    private JobConfigurationMapper jobConfigurationMapper;

    public JobConfigurationMapper getJobConfigurationMapper() {
        return jobConfigurationMapper;
    }

    public void setJobConfigurationMapper(JobConfigurationMapper jobConfigurationMapper) {
        this.jobConfigurationMapper = jobConfigurationMapper;
    }

    public GenerateEventsTask getGenerateEventsTask() {
        return generateEventsTask;
    }

    public void setGenerateEventsTask(GenerateEventsTask generateEventsTask) {
        this.generateEventsTask = generateEventsTask;
    }

    private SongTextShow songTextShow;
    private String singer;
    private String song;

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
        System.out.println("******************* JobKey:" + jobExecutionContext.getJobDetail().getKey() + "*****************************");
        System.out.println("******************* PushHotelJob ***************************");
//        System.out.println(singer + " sing " + song);
//        songTextShow.print();
//        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

//        jobDataMap.put("singer",configurationHelper.getSinger());
//        jobDataMap.put("song",configurationHelper.getSong());
//        for(Map.Entry entry : jobDataMap.entrySet()){
//            jobDataMap.put((String) entry.getKey(),configurationHelper.getSong());
//        }
//        testAutowired.note();

        String event = generateEventsTask.generateEvents(jobConfigurationMapper);
        pushEventsTask.push(event);
        System.out.println("******************************end*******************************");
        System.out.println(jobConfigurationMapper.getJobName());
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
