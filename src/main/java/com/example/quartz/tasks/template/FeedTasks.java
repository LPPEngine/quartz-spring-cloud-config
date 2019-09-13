package com.example.quartz.tasks.template;

import com.example.quartz.configuration.helper.JobConfigurationMapper;
import com.example.quartz.tasks.event.GenerateFeedEventsTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedTasks extends BaseTasksTemplate {

    @Autowired
    GenerateFeedEventsTask generateFeedEventsTask;
    @Override
    void getHotelList() {

    }

    @Override
    String generateEvents(JobConfigurationMapper jobConfigurationMapper) {
        return generateFeedEventsTask.generateEvents(jobConfigurationMapper);
    }
}
