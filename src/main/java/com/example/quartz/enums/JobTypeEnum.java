package com.example.quartz.enums;

import com.example.quartz.jobs.entity.FeedJob;
import com.example.quartz.jobs.entity.PushHotelJob;

public enum  JobTypeEnum {
    /**
     * Push Hotel price job
     */
    PushHotelJob_TYPE_ENUM("PushHotelJob", PushHotelJob.class),
    /**
     * generate feed file job
     */
    FeedJob_TYPE_ENUM("FeedJob", FeedJob.class);

    private String jobType;
    private Class jobClass;

    public Class getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class jobClass) {
        this.jobClass = jobClass;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobType() {
        return jobType;
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
