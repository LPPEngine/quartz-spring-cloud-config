package com.example.quartz.tasks.event;

import com.alibaba.fastjson.JSON;
import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.configuration.helper.PushHotelJobConfigurationMapper;
import com.example.quartz.event.PushPriceEvent;
import org.springframework.stereotype.Service;


/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Service
public class GeneratePushHotelEventsTask implements IGenerateEvents {

    @Override
    public String generateEvents(BaseMapper pushHotelJobConfigurationMapper){
        PushPriceEvent pushPriceEvent = new PushPriceEvent();
        pushPriceEvent.setApw(((PushHotelJobConfigurationMapper)pushHotelJobConfigurationMapper).getApw());
        pushPriceEvent.setLos(((PushHotelJobConfigurationMapper)pushHotelJobConfigurationMapper).getLos());
        pushPriceEvent.setHotelId(((PushHotelJobConfigurationMapper)pushHotelJobConfigurationMapper).getHotelId());
        return JSON.toJSONString(pushPriceEvent);
    }

}
