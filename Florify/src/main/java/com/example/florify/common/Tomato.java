package com.example.florify.common;

public class Tomato extends Plant {
    public Tomato(String name, double soilMoisture, double temperature,
                 double humidity, double lightHours, double daysSinceWatering) {
        super(name, soilMoisture, temperature, humidity, lightHours, daysSinceWatering);
    }

    public Tomato(String name)
    {
        super(name);
    }
}