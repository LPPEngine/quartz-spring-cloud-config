package com.example.quartz.event;

import lombok.Data;

@Data
public class FeedEvent {
    private String partner;
    private String brand;
    private String pos;
}
