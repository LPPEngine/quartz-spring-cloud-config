package com.example.quartz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
public class TestAutowired {
    public void note(){
        System.out.println("I can autowired");
    }
}
