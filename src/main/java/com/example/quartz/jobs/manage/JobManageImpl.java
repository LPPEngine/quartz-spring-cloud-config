package com.example.quartz.jobs.manage;

import com.example.quartz.configuration.helper.BaseMapper;
import com.example.quartz.jobs.init.JobsFactory;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
public class JobManageImpl implements IJobManage, Serializable {

    @Resource
    private Scheduler quartzScheduler;

    @Autowired
    private JobsFactory jobsFactory;

    @Override
    public void add(List<BaseMapper> jobConfigurationMapperList) {
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
            int index = jobKey.indexOf('.');
            String name = jobKey.substring(index + 1);
            String group = jobKey.substring(0,index);
            JobKey deletedJobKey = JobKey.jobKey(name,group);
            //lock??
            if(quartzScheduler.checkExists(deletedJobKey)){
                quartzScheduler.deleteJob(deletedJobKey);
                System.out.println("delete " + jobKey + " successfully!");
            }
            //unlock??
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modify(TriggerKey triggerKey, BaseMapper jobConfigurationMapper) {
        try {
           Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                   .startNow()
                   .withSchedule(CronScheduleBuilder.cronSchedule(jobConfigurationMapper.getPeriod()))
                   .build();
            quartzScheduler.rescheduleJob(triggerKey,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void select(String jobKey) {

    }

    /**
     *     Todo: add a job/jobs or delete a job/jobs through detect configurations
     */
    @Override
    public <E extends BaseMapper> void jobsChange(List<E> jobConfigurationMapperList) {
        try {
            List<String> newJobKeyList = jobConfigurationMapperList.stream()
                    .map(e->e.getJobGroup() + '.' + e.getJobName())
                    .collect(Collectors.toList());
            Set<JobKey> currentJobKeySet = quartzScheduler.getJobKeys(GroupMatcher.groupContains(jobConfigurationMapperList.get(0).getJobGroup()));
            List<String> currentJobKeyList = currentJobKeySet.stream()
                    .map(Key::toString)
                    .collect(Collectors.toList());
            List<String> addedJobKeyList;
            List<String> deletedJobKeyList;
            //delete or add a new job/jobs
            addedJobKeyList = newJobKeyList.stream()
                    .filter(newJobKey-> !currentJobKeyList.contains(newJobKey))
                    .collect(Collectors.toList());

            deletedJobKeyList = currentJobKeyList.stream()
                    .filter(currentJobKey -> !newJobKeyList.contains(currentJobKey))
                    .collect(Collectors.toList());

            List<BaseMapper> addedPushHotelJobConfigurationMapperList = jobConfigurationMapperList.stream()
                    .filter(e->addedJobKeyList.contains(e.getJobGroup() + '.' + e.getJobName()))
                    .collect(Collectors.toList());
            //added jobs
            if (!CollectionUtils.isEmpty(addedJobKeyList)) {
                addedJobKeyList.forEach(newJob -> add(addedPushHotelJobConfigurationMapperList));
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
