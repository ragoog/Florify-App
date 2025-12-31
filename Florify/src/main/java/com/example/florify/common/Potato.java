package com.example.florify.common;

public class Potato extends Plant {

    public Potato(String name, double soilMoisture, double temperature,
                 double humidity, double lightHours, double daysSinceWatering) {
        super(name, soilMoisture, temperature, humidity, lightHours, daysSinceWatering);
    }

    public Potato(String name)
    {
        super(name);
    }

}