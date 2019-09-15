package com.example.quartz.configuration.helper;

import java.util.List;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */

public class PushHotelJobConfigurationMapper extends BaseMapper {
    private List<String> apw;
    private List<String> los;
    private List<String> hotelId;

    public List<String> getApw() {
        return apw;
    }

    public void setApw(List<String> apw) {
        this.apw = apw;
    }

    public List<String> getLos() {
        return los;
    }

    public void setLos(List<String> los) {
        this.los = los;
    }

    public List<String> getHotelId() {
        return hotelId;
    }

    public void setHotelId(List<String> hotelId) {
        this.hotelId = hotelId;
    }

}
