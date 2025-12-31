package com.example.florify.common;

public class Aloe extends Plant {

    public Aloe(String name, double soilMoisture, double temperature,
                 double humidity, double lightHours, double daysSinceWatering) {
        super(name, soilMoisture, temperature, humidity, lightHours, daysSinceWatering);
    }

    public Aloe(String name)
    {
        super(name);
    }
}