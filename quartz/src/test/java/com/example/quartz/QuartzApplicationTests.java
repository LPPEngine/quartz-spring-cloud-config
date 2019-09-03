package com.example.quartz;

import com.example.quartz.configuration.manager.AllPropertiesHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzApplicationTests {
    @Autowired
    private AllPropertiesHelper allProperties;

    @Test
    public void contextLoads() {
    }
    @Test
    public void testGetAllProperties(){
        allProperties.getAllProperties();
    }

}
