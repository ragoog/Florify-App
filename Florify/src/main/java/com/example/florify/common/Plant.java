package com.example.florify.common;

// Interface
interface PlantDetails {
    String plantDetails(Plant plant);
}
public class Plant implements PlantDetails {
    public String name;
    public double soilMoisture;
    public double temperature;
    public double humidity;
    public double lightHours;
    public double daysSinceWatering;

    public Plant(String name)
    {
        this.name = name;
    }

    public Plant(String name, double soilMoisture, double temperature,
                 double humidity, double lightHours, double daysSinceWatering) {
        this.name = name;
        this.soilMoisture = soilMoisture;
        this.temperature = temperature;
        this.humidity = humidity;
        this.lightHours = lightHours;
        this.daysSinceWatering = daysSinceWatering;
    }

    @Override
    public String plantDetails(Plant plant)
    {
        return "Name: " + name +
                "\nsoilMoisture: " + soilMoisture +
                "\ntemperature: " + temperature +
                "\nhumidity: " + humidity +
                "\nlightHours: " + lightHours +
                "\ndaysSinceWatering: " + daysSinceWatering;
    }
}
