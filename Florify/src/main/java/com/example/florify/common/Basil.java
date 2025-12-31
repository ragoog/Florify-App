package com.example.florify.common;

public class Basil extends Plant {

    public Basil(String name, double soilMoisture, double temperature,
                 double humidity, double lightHours, double daysSinceWatering) {
        super(name, soilMoisture, temperature, humidity, lightHours, daysSinceWatering);
    }

    public Basil(String name)
    {
        super(name);
    }
}