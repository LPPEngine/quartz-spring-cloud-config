package com.example.quartz.jobs.entity;

import com.example.quartz.configuration.manager.ConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationHelper;
import com.example.quartz.tasks.GenerateEvents;
import com.example.quartz.tasks.SongTextShow;
import org.quartz.*;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class Sing implements Job {

    private ConfigurationHelper configurationHelper;
    private GenerateEvents generateEvents;
    private JobConfigurationHelper jobConfigurationHelper;

    public JobConfigurationHelper getJobConfigurationHelper() {
        return jobConfigurationHelper;
    }

    public void setJobConfigurationHelper(JobConfigurationHelper jobConfigurationHelper) {
        this.jobConfigurationHelper = jobConfigurationHelper;
    }

    public GenerateEvents getGenerateEvents() {
        return generateEvents;
    }

    public void setGenerateEvents(GenerateEvents generateEvents) {
        this.generateEvents = generateEvents;
    }

    private SongTextShow songTextShow;
    private String singer;
    private String song;
//    @Autowired
//    private TestAutowired testAutowired;

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
        System.out.println("*******************" + jobExecutionContext.getJobDetail().getKey() + "************************************");
        System.out.println("*************** Sing by Jianglixin *****************");
        System.out.println(singer + " sing " + song);
//        songTextShow.print();
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        jobDataMap.put("singer",configurationHelper.getSinger());
        jobDataMap.put("song",configurationHelper.getSong());
//        for(Map.Entry entry : jobDataMap.entrySet()){
//            jobDataMap.put((String) entry.getKey(),configurationHelper.getSong());
//        }
//        testAutowired.note();
        System.out.println("********************************");

        System.out.println("********************do business work***********************");
        generateEvents.generateEvents();
        System.out.println("*******************************end*************************");
        jobConfigurationHelper.getList().forEach(e->System.out.println(e.getJobName()));
        System.out.println(jobConfigurationHelper.getList().get(0).getJobName());
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
