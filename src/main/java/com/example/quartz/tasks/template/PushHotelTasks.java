package com.example.quartz.tasks.template;

import com.example.quartz.configuration.helper.JobConfigurationMapper;
import com.example.quartz.tasks.event.GeneratePushHotelEventsTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PushHotelTasks extends BaseTasksTemplate {

    @Autowired
    private GeneratePushHotelEventsTask generatePushHotelEventsTask;
    @Override
    void getHotelList() {

    }

    @Override
    String generateEvents(JobConfigurationMapper jobConfigurationMapper) {
        return generatePushHotelEventsTask.generateEvents(jobConfigurationMapper);
    }
}
