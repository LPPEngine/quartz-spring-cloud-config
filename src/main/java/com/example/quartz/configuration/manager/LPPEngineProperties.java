package com.example.quartz.configuration.manager;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@ConfigurationProperties
@Component
public class LPPEngineProperties {

    private String from;
    private String singer;
    private String song;
    private String apw;
    private String los;
    private List<String> hotelId;
    private String partner;
    private String delay;
    private String period;

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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

    public List<String> getHotelId() {
        return hotelId;
    }

    public void setHotelId(List<String> hotelId) {
        this.hotelId = hotelId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    /**
     *     Todo: when configuration change , will do something
     */
    @EventListener(EnvironmentChangeEvent.class)
    public void configChanged(){
        System.out.println("config has been changed");
    }

}
