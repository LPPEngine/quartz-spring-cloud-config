package com.example.quartz.tasks.event;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class PushEventsTask implements Serializable {

    public void push(String event){
        System.out.println("push events:" + event + "into SQS");
    }
}
