package com.example.quartz.jobs.manage;

import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.jobs.init.InitialJobs;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
public class JobManageImpl implements IJobManage {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private InitialJobs initialJobs;

    @Override
    public void add(Map map) {

    }

    @Override
    public void add(List<JobConfigurationMapper> jobConfigurationMapperList) {
        try {
            initialJobs.initialAllJobs(jobConfigurationMapperList);
        } catch (SchedulerException e) {
            e.printStackTrace();
            jobConfigurationMapperList.forEach(jobConfigurationMapper -> System.out.println("add jobs" + jobConfigurationMapper.getJobName() + jobConfigurationMapper.getJobGroup() + "exception"));
        }
    }

    @Override
    public void delete(String jobKey) {
        try {
            JobKey deletedJobKey = JobKey.jobKey(jobKey);
            if(scheduler.checkExists(deletedJobKey)){
                scheduler.deleteJob(deletedJobKey);
                System.out.println("delete " + jobKey + " successfully!");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modify(TriggerKey triggerKey,JobConfigurationMapper jobConfigurationMapper) {
        try {
           Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                   .startNow()
                   .withSchedule(CronScheduleBuilder.cronSchedule(jobConfigurationMapper.getPeriod()))
                   .build();
           scheduler.rescheduleJob(triggerKey,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void select(String jobKey) {

    }

    /**
     * refresh jobs configurations or add a new job(jobs)
     * @param currentPropertiesMap
     * @param previousPropertiesMap
     * @param modifyJobKeyList
     * @param addJobKeyList
     * @param deleteJobKeyList
     */
    @Override
    public void jobsChange(Map currentPropertiesMap, Map previousPropertiesMap, List<String> modifyJobKeyList,List<String> addJobKeyList,List<String> deleteJobKeyList){
        if(!CollectionUtils.isEmpty(modifyJobKeyList)){
            System.out.println("modify jobs configurations!");
        }
        if(!CollectionUtils.isEmpty(addJobKeyList)){
            System.out.println("add new jobs!");
        }
        if(!CollectionUtils.isEmpty(deleteJobKeyList)){
            System.out.println("delete jobs!");
        }

    }

    /**
     * when add a job/jobs or delete a job/jobs
     * @param jobConfigurationMapperList
     */
    @Override
    public void jobsChange(List<JobConfigurationMapper> jobConfigurationMapperList) {
        try {
            List<String> newJobKeyList = jobConfigurationMapperList.stream().map(e->e.getJobName() + e.getJobGroup()).collect(Collectors.toList());
            List<JobExecutionContext> jobExecutionContextList = scheduler.getCurrentlyExecutingJobs();
            List<String> currentJobKeyList = jobExecutionContextList.stream().map(jobExecutionContext -> jobExecutionContext.getJobDetail().getKey().getName()).collect(Collectors.toList());
            List<String> addedJobKeyList;
            List<String> deletedJobKeyList;
            //delete or add a new job/jobs
            addedJobKeyList = newJobKeyList.stream().filter(newJobKey-> !currentJobKeyList.contains(newJobKey)).collect(Collectors.toList());

            deletedJobKeyList = currentJobKeyList.stream().filter(currentJobKey -> !newJobKeyList.contains(currentJobKey)).collect(Collectors.toList());

            List<JobConfigurationMapper> addedJobConfigurationMapperList = jobConfigurationMapperList.stream().filter(e->addedJobKeyList.contains(e.getJobName()+e.getJobGroup())).collect(Collectors.toList());
            //added jobs
            if (!CollectionUtils.isEmpty(addedJobKeyList)) {
                addedJobKeyList.forEach(newJob -> add(addedJobConfigurationMapperList));
            }
            //delete jobs
            if(!CollectionUtils.isEmpty(deletedJobKeyList)){
                deletedJobKeyList.forEach(this::delete);
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
            System.out.println("add or delete jobs exception");
        }
    }
}
