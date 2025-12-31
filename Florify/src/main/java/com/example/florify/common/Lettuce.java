package com.example.florify.common;

public class Lettuce extends Plant {
    public Lettuce(String name, double soilMoisture, double temperature,
                 double humidity, double lightHours, double daysSinceWatering) {
        super(name, soilMoisture, temperature, humidity, lightHours, daysSinceWatering);
    }

    public Lettuce(String name)
    {
        super(name);
    }
}