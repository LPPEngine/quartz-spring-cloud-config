package com.example.quartz.tasks.event;

import org.springframework.stereotype.Service;

@Service
public class PushEventsTask {

    public void push(String event){
        System.out.println("push events:" + event + "into SQS");
    }
}
