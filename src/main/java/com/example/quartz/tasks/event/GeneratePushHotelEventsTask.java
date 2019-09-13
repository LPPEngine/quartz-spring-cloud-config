package com.example.quartz.tasks.event;

import com.alibaba.fastjson.JSON;
import com.example.quartz.configuration.helper.JobConfigurationMapper;
import com.example.quartz.event.PushPriceEvent;
import org.springframework.stereotype.Service;

import java.io.Serializable;


/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Service
public class GeneratePushHotelEventsTask implements IGenerateEvents {

    @Override
    public String generateEvents(JobConfigurationMapper jobConfigurationMapper){
        PushPriceEvent pushPriceEvent = new PushPriceEvent();
        pushPriceEvent.setApw(jobConfigurationMapper.getApw());
        pushPriceEvent.setLos(jobConfigurationMapper.getLos());
        pushPriceEvent.setHotelId(jobConfigurationMapper.getHotelId());
        return JSON.toJSONString(pushPriceEvent);
    }

}
