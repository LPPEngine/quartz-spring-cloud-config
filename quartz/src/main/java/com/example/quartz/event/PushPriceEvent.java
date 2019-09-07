package com.example.quartz.event;

import lombok.Data;

import java.util.List;

@Data
public class PushPriceEvent {

    private String partnerName = "Google";
    private String brandName = "Ecom";
    private List<String> hotelId;
    private List<String> apw;
    private List<String> los;
}
