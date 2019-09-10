package com.example.quartz.jobs.manage;

import com.example.quartz.configuration.manager.JobConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.jobs.init.JobsFactory;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class JobManageImpl implements IJobManage {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private JobConfigurationHelper jobConfigurationHelper;
    @Autowired
    private JobsFactory jobsFactory;

    @Override
    public void add(Map map) {

    }

    @Override
    public void add(List<JobConfigurationMapper> jobConfigurationMapperList) {
        try {
            jobsFactory.addJobs(jobConfigurationMapperList);
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
     */
    @Override
    public void jobsChange() {
        List<JobConfigurationMapper> jobConfigurationMapperList = jobConfigurationHelper.getJobConfigurationList();
        try {
//            jobConfigurationMapperList.forEach(jobConfigurationMapper -> {
//                try {
//                    JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobConfigurationMapper.getJobName(),jobConfigurationMapper.getJobGroup()));
//                    if(jobDetail != null){
//                        jobDetail.getJobDataMap().put("jobConfigurationMapper",jobConfigurationMapper);
//                    }
//                    System.out.println(jobDetail);
//                } catch (SchedulerException e) {
//                    e.printStackTrace();
//                }
//            });
            List<String> newJobKeyList = jobConfigurationMapperList.stream().map(e->e.getJobGroup() + '.' + e.getJobName()).collect(Collectors.toList());
            Set<JobKey> currentJobKeySet = scheduler.getJobKeys(GroupMatcher.groupContains("jobGroup"));
            List<String> currentJobKeyList = currentJobKeySet.stream().map(Key::toString).collect(Collectors.toList());
            List<String> addedJobKeyList;
            List<String> deletedJobKeyList;
            //delete or add a new job/jobs
            addedJobKeyList = newJobKeyList.stream().filter(newJobKey-> !currentJobKeyList.contains(newJobKey)).collect(Collectors.toList());

            deletedJobKeyList = currentJobKeyList.stream().filter(currentJobKey -> !newJobKeyList.contains(currentJobKey)).collect(Collectors.toList());

            List<JobConfigurationMapper> addedJobConfigurationMapperList = jobConfigurationMapperList.stream().filter(e->addedJobKeyList.contains(e.getJobGroup() + '.' + e.getJobName())).collect(Collectors.toList());
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
