package com.example.quartz.configuration.manager;

import com.example.quartz.jobs.init.InitialJobs;
import com.example.quartz.jobs.manage.IJobManage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@ConfigurationProperties
@Component
public class AllPropertiesHelper implements InitializingBean {
    @Autowired
    InitialJobs initialJobs;
    @Autowired
    IJobManage jobManage;
    private List<String> modifyJobKeyList;
    private List<String> deleteJobKeyList;
    private List<String> addJobKeyList;

    public List<String> getModifyJobKeyList() {
        return modifyJobKeyList;
    }

    public void setModifyJobKeyList(List<String> modifyJobKeyList) {
        this.modifyJobKeyList = modifyJobKeyList;
    }

    public List<String> getDeleteJobKeyList() {
        return deleteJobKeyList;
    }

    public void setDeleteJobKeyList(List<String> deleteJobKeyList) {
        this.deleteJobKeyList = deleteJobKeyList;
    }

    public List<String> getAddJobKeyList() {
        return addJobKeyList;
    }

    public void setAddJobKeyList(List<String> addJobKeyList) {
        this.addJobKeyList = addJobKeyList;
    }

    /**
     * the map save current properties on the github
     */
    private static Map currentPropertiesMap;
    /**
     * the map save previous properties after modify the properties on the github
     */
    private static Map<Object, Object> previousPropertiesMap = new HashMap<>();

    private RestTemplate restTemplate = new RestTemplate();

//    public MutablePropertySources getAllProperties(){
//        MutablePropertySources sources = ((ConfigurableEnvironment)environment).getPropertySources();
//        return sources;
//    }

    public Map getAllProperties(){
        currentPropertiesMap = restTemplate.getForObject("http://localhost:7003/LPPEngine/jobs/master", Map.class);
        ArrayList arrayList = (currentPropertiesMap != null ? (ArrayList) currentPropertiesMap.get("propertySources") : null);
        return (Map) ((Map) arrayList.get(0)).get("source");
    }
    /**
     *     Todo: when configuration change , will do something
     */
    @EventListener(EnvironmentChangeEvent.class)
    public void refreshProperties(){
        // must clear,maybe some key of properties on the github would delete
        previousPropertiesMap.clear();
        previousPropertiesMap.putAll(currentPropertiesMap);
        getAllProperties();
        jobManage.jobsChange(currentPropertiesMap,previousPropertiesMap,modifyJobKeyList,addJobKeyList,deleteJobKeyList);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        initialJobs.initialAllJobs(getAllProperties());
    }
}
