package com.example.quartz.configuration.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
//@ConfigurationProperties
@Component
public class JobConfigurationMapper implements Serializable {
    @Value("jobName")
    private String jobName;
    @Value("jobGroup")
    private String jobGroup;
    @Value("apw")
    private List<String> apw;
    @Value("los")
    private List<String> los;
    @Value("hotelId")
    private List<String> hotelId;
    @Value("period")
    private String period;
    @Value("triggerName")
    private String triggerName;
    @Value("triggerGroup")
    private String triggerGroup;

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<String> getApw() {
        return apw;
    }

    public void setApw(List<String> apw) {
        this.apw = apw;
    }

    public List<String> getLos() {
        return los;
    }

    public void setLos(List<String> los) {
        this.los = los;
    }

    public List<String> getHotelId() {
        return hotelId;
    }

    public void setHotelId(List<String> hotelId) {
        this.hotelId = hotelId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
}
