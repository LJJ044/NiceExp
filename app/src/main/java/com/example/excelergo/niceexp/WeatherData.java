package com.example.excelergo.niceexp;

public class WeatherData {
    String weather;
    String temp;
    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }


    public WeatherData(String weather, String temp) {
        this.weather = weather;
        this.temp = temp;
    }
}
