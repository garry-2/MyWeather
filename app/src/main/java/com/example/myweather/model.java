package com.example.myweather;

public class model {
    String temp;
    String time;
    String wind_speed;
    String img_url;

    public model(String temp, String time, String wind_speed, String img_url) {
        this.temp = temp;
        this.time = time;
        this.wind_speed = wind_speed;
        this.img_url = img_url;
    }
}
