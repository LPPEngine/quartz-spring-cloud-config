package com.example.quartz.enums;

import com.example.quartz.jobs.entity.FeedJob;
import com.example.quartz.jobs.entity.PushHotelJob;

public enum  JobTypeEnum {

    PushHotelJob_TYPE_ENUM("PushHotelJob", PushHotelJob.class),
    FeedJob_TYPE_ENUM("FeedJob", FeedJob.class);
    private String jobType;
    private Class jobClass;

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void settClass(Class tClass) {
        this.jobClass = tClass;
    }

    public String getJobType() {
        return jobType;
    }

    public Class gettClass() {
        return jobClass;
    }

    JobTypeEnum(String jobType, Class jobClass) {
        this.jobType = jobType;
        this.jobClass = jobClass;
    }

    public static Class getJobClass(String jobType){
        for (JobTypeEnum jobTypeEnum : JobTypeEnum.values()){
            if(jobTypeEnum.getJobType().equals(jobType)){
                return jobTypeEnum.jobClass;
            }
        }
        return null;
    }
}
