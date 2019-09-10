package com.example.quartz.configuration.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */

public class JobConfigurationMapper implements Serializable {
    private String jobName;
    private String jobGroup;
    private List<String> apw;
    private List<String> los;
    private List<String> hotelId;
    private String period;
    private String triggerName;
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
