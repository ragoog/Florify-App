package com.example.florify.machineLearningModels;

import org.jpmml.evaluator.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PMMLLoader {

    public static Evaluator loadRfModel(String resourcePath) throws Exception {
        InputStream is = PMMLLoader.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("PMML file not found: " + resourcePath);
        }

        Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                .load(is)
                .build();

        // Must call verify() to fully initialize evaluator
        evaluator.verify();

        return evaluator;
    }

    public static String rfModelPrediction(Evaluator model, Double soilMoisture, Double temperature, Double humidity,
                                         Double lightHours, Double daysSinceWatering) throws Exception
    {

        // Prepare input
        Map<String, Object> input = new HashMap<>();
        input.put("soil_moisture", soilMoisture);
        input.put("temperature", temperature);
        input.put("humidity", humidity);
        input.put("light_hours", lightHours);
        input.put("days_since_watering", daysSinceWatering);

        // Evaluate
        Map<String, ?> result = model.evaluate(input);

        // Get target field
        TargetField targetField = model.getTargetFields().get(0);
        String targetName = targetField.getName();

        // The prediction is usually inside a Computable object
        Object raw = result.get(targetName);
        Object predictionLabel;

        if(raw instanceof Computable computable){
            Object computed = computable.getResult();
            if(computed instanceof Map<?, ?> map){
                // For classification: pick the key with highest probability
                Map<String, Double> probs = (Map<String, Double>) map;
                predictionLabel = probs.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .get()
                        .getKey();
            } else {
                predictionLabel = computed; // regression model
            }
        } else {
            predictionLabel = raw;
        }

        return ("RANDOM FOREST MODEL PREDICTION: " + predictionLabel);
    }
}