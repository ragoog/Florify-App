package com.example.florify.common;

import com.example.florify.common.Post;
import java.util.ArrayList;

//Interfaces
interface DiseaseAnalysis {
    String diseaseType();
}

interface WateringCheck {
    int waterAmount();
}

// Users class
public class User implements DiseaseAnalysis, WateringCheck {

    public String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public ArrayList<Plant> plants = new ArrayList<>();
    public ArrayList<Post> posts = new ArrayList<>();

    // Add a plant
    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }
    @Override
    public String diseaseType() {
        // Simple heuristic example:
        for (Plant p : plants) {
            if (p.humidity > 80 && p.soilMoisture > 70) {
                return "Fungal Risk";
            }
        }
        return "Healthy";
    }

    @Override
    public int waterAmount() {
        // Sum up recommendations from ML model or simple rule
        int total = 0;
        for (Plant p : plants) {
            if (p.soilMoisture < 40) total += 1;
        }
        return total;
    }
}