package com.example.quartz.jobs.entity;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.configuration.helper.PushHotelJobConfigurationHelper;
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
    private PushHotelJobConfigurationHelper pushHotelJobConfigurationHelper;
    @Autowired
    private IJobManage jobManage;
    @Autowired
    private BaseTasksTemplate pushHotelTasks;
    @Autowired
    private BaseMapper baseMapper;

    public BaseMapper getBaseMapper() {
        return baseMapper;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        pushHotelTasks.executeTask(jobExecutionContext);

    }
}
