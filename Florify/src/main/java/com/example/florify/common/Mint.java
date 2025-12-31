package com.example.florify.common;

public class Mint extends Plant {

    public Mint(String name, double soilMoisture, double temperature,
                 double humidity, double lightHours, double daysSinceWatering) {
        super(name, soilMoisture, temperature, humidity, lightHours, daysSinceWatering);
    }

    public Mint(String name)
    {
        super(name);
    }
}