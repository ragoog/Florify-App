package com.example.florify.common;

public class Spinach extends Plant {

    public Spinach(String name, double soilMoisture, double temperature,
                 double humidity, double lightHours, double daysSinceWatering) {
        super(name, soilMoisture, temperature, humidity, lightHours, daysSinceWatering);
    }

    public Spinach(String name)
    {
        super(name);
    }
}