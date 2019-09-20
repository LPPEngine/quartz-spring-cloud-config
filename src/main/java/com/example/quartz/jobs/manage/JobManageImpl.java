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
    public void delete(List<JobKey> jobKeyList) {
        try {
            //lock??
            if(quartzScheduler.deleteJobs(jobKeyList)){
                jobKeyList.forEach(jobKey -> System.out.println("delete " + jobKey + " successfully!"));
            }else {
                jobKeyList.forEach(jobKey -> System.out.println("delete " + jobKey + " fail!"));
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
    public <E extends BaseMapper> void jobsChange(List<E> jobConfigurationMapperList,String jobGroup) {
        try {
            List<String> newJobKeyList = jobConfigurationMapperList.stream()
                    .map(e->e.getJobGroup() + '.' + e.getJobName())
                    .collect(Collectors.toList());
            Set<JobKey> currentJobKeySet = quartzScheduler.getJobKeys(GroupMatcher.groupContains(jobGroup));
            List<String> currentJobKeyList = currentJobKeySet.stream()
                    .map(Key::toString)
                    .collect(Collectors.toList());
            List<BaseMapper> addedJobConfigurationMapperList;
            List<JobKey> deletedJobKeyList;
            List<String> deletedJobKeyStringList;

            /*delete or add a new job/jobs */
            addedJobConfigurationMapperList = jobConfigurationMapperList.stream()
                    .filter(jobConfigurationMapper -> !currentJobKeyList.contains(jobConfigurationMapper.getJobGroup() + '.' + jobConfigurationMapper.getJobName()))
                    .collect(Collectors.toList());

            deletedJobKeyStringList = currentJobKeyList.stream()
                    .filter(currentJobKey -> !newJobKeyList.contains(currentJobKey))
                    .collect(Collectors.toList());

            deletedJobKeyList = deletedJobKeyStringList.stream()
                    .map(deletedJobKeyString -> JobKey.jobKey(deletedJobKeyString.substring(deletedJobKeyString.indexOf('.') + 1),deletedJobKeyString.substring(0,deletedJobKeyString.indexOf('.'))))
                    .collect(Collectors.toList());
            //added jobs
            if (!CollectionUtils.isEmpty(addedJobConfigurationMapperList)) {
                add(addedJobConfigurationMapperList);
            }
            //delete jobs
            if(!CollectionUtils.isEmpty(deletedJobKeyList)){
                delete(deletedJobKeyList);
            }
            /*delete or add a new job/jobs */

        } catch (SchedulerException e) {
            e.printStackTrace();
            System.out.println("add or delete jobs exception");
        }
    }
}
