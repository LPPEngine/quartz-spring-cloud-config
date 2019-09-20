package com.example.quartz.tasks.template;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.configuration.helper.JobConfigurationHelper;
import com.example.quartz.tasks.event.GeneratePushHotelEventsTask;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PushHotelTasks extends BaseTasksTemplate {

    @Autowired
    private JobConfigurationHelper jobConfigurationHelper;

    @Autowired
    private GeneratePushHotelEventsTask generatePushHotelEventsTask;

    @Override
    BaseMapper getJobConfigurationMapper(JobExecutionContext jobExecutionContext) {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();

        //get jobConfigurationMapper firstly
        return jobConfigurationHelper.getPushHotelJobConfigurationList().stream()
                .filter(jobConfiguration -> jobConfiguration.getJobGroup().equals(jobKey.getGroup()) && jobConfiguration.getJobName().equals(jobKey.getName()))
                .findFirst()
                .orElse(null);
    }

    @Override
    void getHotelList() {

    }

    @Override
    String generateEvents(BaseMapper pushHotelJobConfigurationMapper) {
        return generatePushHotelEventsTask.generateEvents(pushHotelJobConfigurationMapper);
    }
}
