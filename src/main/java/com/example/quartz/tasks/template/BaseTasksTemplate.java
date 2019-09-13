package com.example.quartz.tasks.template;

import com.example.quartz.configuration.helper.JobConfigurationHelper;
import com.example.quartz.configuration.helper.JobConfigurationMapper;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.event.PushEventsTask;
import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * this a template class about a job execute flow include
 * (1.get job configuration 2.getHotelList 3.generate events 4.push events 5 refresh job configuration of job period,delay)
 */
@Component
public abstract class BaseTasksTemplate {
    @Autowired
    private JobConfigurationHelper jobConfigurationHelper;
    @Autowired
    private PushEventsTask pushEventsTask;
    @Autowired
    private IJobManage jobManage;

    JobConfigurationMapper getJobConfigurationMapper(JobExecutionContext jobExecutionContext){

        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();

        //get jobConfigurationMapper firstly
        return jobConfigurationHelper.getJobConfigurationList().stream()
                .filter(jobConfiguration -> jobConfiguration.getJobGroup().equals(jobKey.getGroup()) && jobConfiguration.getJobName().equals(jobKey.getName()))
                .findFirst()
                .orElse(null);
    }

    abstract void getHotelList();

    abstract String generateEvents(JobConfigurationMapper jobConfigurationMapper);

    void pushEvents(String event){
        pushEventsTask.push(event);
    }

    void refreshJobConfiguration(JobExecutionContext jobExecutionContext,JobConfigurationMapper jobConfigurationMapper){
        CronTrigger cronTrigger  = (CronTrigger) jobExecutionContext.getTrigger();
        String period = cronTrigger.getCronExpression();
        //judge the trigger period whether has changed every the job execute
        if(!period.equals(jobConfigurationMapper.getPeriod())) {
            jobManage.modify(jobExecutionContext.getTrigger().getKey(), jobConfigurationMapper);
        }
    }

    public void executeTask(JobExecutionContext jobExecutionContext){
        JobConfigurationMapper jobConfigurationMapper = getJobConfigurationMapper(jobExecutionContext);
        getHotelList();
        System.out.println("******************* JobKey:" + jobConfigurationMapper.getJobGroup() +'.'+ jobConfigurationMapper.getJobName() + "*****************************");
        System.out.println("*******************"+jobConfigurationMapper.getJobType() +"***************************");
        String event = generateEvents(jobConfigurationMapper);
        pushEvents(event);
        System.out.println("******************************end*******************************");
        System.out.println(jobConfigurationMapper.getJobName());
        System.out.println();
        System.out.println();
        refreshJobConfiguration(jobExecutionContext,jobConfigurationMapper);
    }

}
