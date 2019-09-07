package com.example.quartz.tasks.event;

import com.alibaba.fastjson.JSON;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.event.PushPriceEvent;
import org.springframework.stereotype.Service;


/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Service
public class GenerateEventsTask {

    public String generateEvents(JobConfigurationMapper jobConfigurationMapper){
        PushPriceEvent pushPriceEvent = new PushPriceEvent();
        pushPriceEvent.setApw(jobConfigurationMapper.getApw());
        pushPriceEvent.setLos(jobConfigurationMapper.getLos());
        pushPriceEvent.setHotelId(jobConfigurationMapper.getHotelId());
        return JSON.toJSONString(pushPriceEvent);
    }

}
