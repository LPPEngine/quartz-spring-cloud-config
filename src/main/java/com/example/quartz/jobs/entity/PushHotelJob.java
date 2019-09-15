package com.example.quartz.jobs.entity;

import com.example.quartz.tasks.template.BaseTasksTemplate;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PushHotelJob extends QuartzJobBean {

    @Autowired
    private BaseTasksTemplate pushHotelTasks;


    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        pushHotelTasks.executeTask(jobExecutionContext);

    }
}
