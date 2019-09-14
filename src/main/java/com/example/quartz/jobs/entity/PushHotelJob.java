package com.example.quartz.jobs.entity;

import com.example.quartz.configuration.helper.JobConfigurationHelper;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.event.GeneratePushHotelEventsTask;
import com.example.quartz.tasks.event.PushEventsTask;
import com.example.quartz.tasks.template.BaseTasksTemplate;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PushHotelJob extends QuartzJobBean {
    @Autowired
    private PushEventsTask pushEventsTask;
    @Autowired
    private GeneratePushHotelEventsTask generatePushHotelEventsTask;
    @Autowired
    private JobConfigurationHelper jobConfigurationHelper;
    @Autowired
    private IJobManage jobManage;
    @Autowired
    private BaseTasksTemplate pushHotelTasks;

    public JobConfigurationHelper getJobConfigurationHelper() {
        return jobConfigurationHelper;
    }

    public void setJobConfigurationHelper(JobConfigurationHelper jobConfigurationHelper) {
        this.jobConfigurationHelper = jobConfigurationHelper;
    }

    public IJobManage getJobManage() {
        return jobManage;
    }

    public void setJobManage(IJobManage jobManage) {
        this.jobManage = jobManage;
    }

    public PushEventsTask getPushEventsTask() {
        return pushEventsTask;
    }

    public void setPushEventsTask(PushEventsTask pushEventsTask) {
        this.pushEventsTask = pushEventsTask;
    }


    public GeneratePushHotelEventsTask getGeneratePushHotelEventsTask() {
        return generatePushHotelEventsTask;
    }

    public void setGeneratePushHotelEventsTask(GeneratePushHotelEventsTask generatePushHotelEventsTask) {
        this.generatePushHotelEventsTask = generatePushHotelEventsTask;
    }


    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        pushHotelTasks.executeTask(jobExecutionContext);

    }
}
