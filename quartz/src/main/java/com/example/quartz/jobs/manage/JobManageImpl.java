package com.example.quartz.jobs.manage;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
public class JobManageImpl implements IJobManage {
    @Override
    public void add(Map map) {

    }

    @Override
    public void delete(String jobKey) {

    }

    @Override
    public void modify(String jobKey) {

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
}
