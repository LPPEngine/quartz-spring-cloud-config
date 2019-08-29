package com.example.quartz.jobs;

import com.example.quartz.config.manager.ConfigurationHelper;
import com.example.quartz.service.GenerateEvents;
import com.example.quartz.service.SongTextShow;
import org.quartz.*;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class Sing implements Job {

    private ConfigurationHelper configurationHelper;
    private GenerateEvents generateEvents;

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
        System.out.println("***************Sing*****************");
        System.out.println(singer + " sing " + song);
        songTextShow.print();
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
    }
}
