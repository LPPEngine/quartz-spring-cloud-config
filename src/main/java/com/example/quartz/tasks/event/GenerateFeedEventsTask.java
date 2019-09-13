package com.example.quartz.tasks.event;

import com.alibaba.fastjson.JSON;
import com.example.quartz.configuration.helper.JobConfigurationMapper;
import com.example.quartz.event.FeedEvent;
import org.springframework.stereotype.Service;

@Service
public class GenerateFeedEventsTask implements IGenerateEvents {
    @Override
    public String generateEvents(JobConfigurationMapper jobConfigurationMapper) {
        FeedEvent feedEvent = new FeedEvent();
        feedEvent.setPartner("Google");
        feedEvent.setBrand("HCOM");
        feedEvent.setPos("US-EN");
        return JSON.toJSONString(feedEvent);
    }
}
