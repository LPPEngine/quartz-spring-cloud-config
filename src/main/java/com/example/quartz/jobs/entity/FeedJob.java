package com.example.quartz.jobs.entity;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.tasks.template.BaseTasksTemplate;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class FeedJob extends QuartzJobBean {
    @Autowired
    private BaseTasksTemplate feedTasks;

    private BaseMapper baseMapper;

    public BaseMapper getBaseMapper() {
        return baseMapper;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        feedTasks.executeTask(jobExecutionContext);
    }
}
