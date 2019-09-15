package com.example.quartz.tasks.event;

import com.alibaba.fastjson.JSON;
import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.configuration.helper.FeedJobConfigurationMapper;
import com.example.quartz.event.FeedEvent;
import org.springframework.stereotype.Service;

@Service
public class GenerateFeedEventsTask implements IGenerateEvents {
    @Override
    public String generateEvents(BaseMapper feedJobConfigurationMapper) {
        FeedEvent feedEvent = new FeedEvent();
        feedEvent.setPartner(((FeedJobConfigurationMapper)feedJobConfigurationMapper).getPartner());
        feedEvent.setBrand(((FeedJobConfigurationMapper)feedJobConfigurationMapper).getBrand());
        feedEvent.setPos(((FeedJobConfigurationMapper)feedJobConfigurationMapper).getPos());
        return JSON.toJSONString(feedEvent);
    }
}
