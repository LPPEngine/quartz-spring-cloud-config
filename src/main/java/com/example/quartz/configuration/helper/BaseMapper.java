package com.example.quartz.configuration.helper;

import lombok.Data;

@Data
public abstract class BaseMapper {

    private String jobName;
    private String jobGroup;
    private String triggerName;
    private String triggerGroup;
    private String period;
    private String jobType;

}
