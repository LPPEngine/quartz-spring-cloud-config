package com.example.quartz.jobs.manage;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
public interface IJobManage {
    void add(Map map);

    void delete(String jobKey);

    void modify(String jobKey);

    void select(String jobKey);

    void jobsChange(Map currentPropertiesMap, Map previousPropertiesMap, List<String> modifyJobKeyList, List<String> addJobKeyList, List<String> deleteJobKeyList);
}
