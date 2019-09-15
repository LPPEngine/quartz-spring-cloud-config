package com.example.quartz.tasks.template;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.configuration.helper.JobConfigurationHelper;
import com.example.quartz.tasks.event.GenerateFeedEventsTask;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedTasks extends BaseTasksTemplate {

    @Autowired
    private JobConfigurationHelper jobConfigurationHelper;

    @Autowired
    GenerateFeedEventsTask generateFeedEventsTask;

    @Override
    BaseMapper getJobConfigurationMapper(JobExecutionContext jobExecutionContext) {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();

        //get jobConfigurationMapper firstly
        return jobConfigurationHelper.getFeedJobConfigurationList().stream()
                .filter(jobConfiguration -> jobConfiguration.getJobGroup().equals(jobKey.getGroup()) && jobConfiguration.getJobName().equals(jobKey.getName()))
                .findFirst()
                .orElse(null);
    }

    @Override
    void getHotelList() {

    }

    @Override
    String generateEvents(BaseMapper feedJobConfigurationMapper) {
        return generateFeedEventsTask.generateEvents(feedJobConfigurationMapper);
    }
}
