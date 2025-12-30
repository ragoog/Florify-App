package com.example.florify.machineLearningModels;

import org.jpmml.evaluator.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PMMLLoader     // pmml stands for portable machine learning model
{

    // Singleton instance
    private static PMMLLoader instance;

    // Evaluator loaded once
    private Evaluator rfModel;

    // Private constructor
    private PMMLLoader(String resourcePath) throws Exception
    {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("PMML file not found: " + resourcePath);
        }

        rfModel = new LoadingModelEvaluatorBuilder()
                .load(is)
                .build();

        rfModel.verify(); // initialize evaluator
    }

    // Public method to get the singleton instance
    public static PMMLLoader getInstance(String resourcePath) throws Exception {
        if (instance == null) {
            instance = new PMMLLoader(resourcePath);
        }
        return instance;
    }

    // Get the loaded model
    public Evaluator getRfModel() {
        return rfModel;
    }
    // Prediction method
    public String rfModelPrediction(Double soilMoisture, Double temperature, Double humidity,
                                    Double lightHours, Double daysSinceWatering) throws Exception {

        Map<String, Object> input = new HashMap<>();
        input.put("soil_moisture", soilMoisture);
        input.put("temperature", temperature);
        input.put("humidity", humidity);
        input.put("light_hours", lightHours);
        input.put("days_since_watering", daysSinceWatering);

        Map<String, ?> result = rfModel.evaluate(input);

        TargetField targetField = rfModel.getTargetFields().get(0);
        String targetName = targetField.getName();

        Object raw = result.get(targetName);
        Object predictionLabel;

        if(raw instanceof Computable computable){
            Object computed = computable.getResult();
            if(computed instanceof Map<?, ?> map){
                Map<String, Double> probs = (Map<String, Double>) map;
                predictionLabel = probs.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .get()
                        .getKey();
            } else {
                predictionLabel = computed;
            }
        } else {
            predictionLabel = raw;
        }

        return ("RANDOM FOREST MODEL PREDICTION: " + predictionLabel);
    }
}
