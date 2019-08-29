package com.example.quartz.service;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SongTextShow implements Serializable {
    public void print(){
        System.out.println("We are the world , we are the children");
        System.out.println("We are the world , we are the children");
        System.out.println("We are the world , we are the children");
    }
}
