package com.example.quartz.tasks.template;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.event.PushEventsTask;
import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * this a template class about a job execute flow include
 * (1.get job configuration 2.getHotelList 3.generate events 4.push events 5 refresh job configuration of job period,delay)
 */
@Component
public abstract class BaseTasksTemplate {

    @Autowired
    private PushEventsTask pushEventsTask;
    @Autowired
    private IJobManage jobManage;

    abstract BaseMapper getJobConfigurationMapper(JobExecutionContext jobExecutionContext);

    abstract void getHotelList();

    abstract <E extends BaseMapper> String generateEvents(E jobConfigurationMapper);

    void pushEvents(String event){
        pushEventsTask.push(event);
    }

    void refreshJobConfiguration(JobExecutionContext jobExecutionContext, BaseMapper jobConfigurationMapper){
        CronTrigger cronTrigger  = (CronTrigger) jobExecutionContext.getTrigger();
        String period = cronTrigger.getCronExpression();
        //judge the trigger period whether has changed every the job execute
        if(!period.equals(jobConfigurationMapper.getPeriod())) {
            //lock (maybe a job just can run in a instance at a trigger so we maybe not lock this code)
            jobManage.modify(jobExecutionContext.getTrigger().getKey(), jobConfigurationMapper);
            //unlock
        }
    }

    public void executeTask(JobExecutionContext jobExecutionContext){
        //step 1:get job configuration from config service
        BaseMapper jobConfigurationMapper = getJobConfigurationMapper(jobExecutionContext);
        if (jobConfigurationMapper != null) {
            //step 2:get hotel list
            getHotelList();
            //step 3:generate events
            System.out.println("******************* JobKey:" + jobConfigurationMapper.getJobGroup() +'.'+ jobConfigurationMapper.getJobName() + "*****************************");
            System.out.println("*******************"+ jobConfigurationMapper.getJobType() +"***************************");
            String event = generateEvents(jobConfigurationMapper);
            //step 4:push events into SQS
            pushEvents(event);
            System.out.println("******************************end*******************************");
            System.out.println(jobConfigurationMapper.getJobName());
            System.out.println();
            System.out.println();
            //step 5:refresh job delay and period if have changed
            refreshJobConfiguration(jobExecutionContext, jobConfigurationMapper);
        }
    }

}
