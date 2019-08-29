package com.example.quartz.service;

import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
public class GenerateEvents {
    private List<String> hotelId;
    private String apw;
    private String los;

    public List<String> getHotelId() {
        return hotelId;
    }

    public void setHotelId(List<String> hotelId) {
        this.hotelId = hotelId;
    }

    public String getApw() {
        return apw;
    }

    public void setApw(String apw) {
        this.apw = apw;
    }

    public String getLos() {
        return los;
    }

    public void setLos(String los) {
        this.los = los;
    }
    public void generateEvents(){
        System.out.println("{ hotelId: " + hotelId + " apw: " + apw + " los: " + los + " }");
    }
}
