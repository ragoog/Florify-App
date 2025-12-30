package com.example.florify.machineLearningModels;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat32;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TFLoader {

    private static TFLoader instance;
    private SavedModelBundle model;
    private boolean isModelLoaded = false;

    // Class labels for the 12 plant disease classes
    private static final String[] CLASS_LABELS = {
            "Potato - Early Blight",
            "Potato - Healthy",
            "Potato - Late Blight",
            "Tomato - Bacterial Spot",
            "Tomato - Early Blight",
            "Tomato - Late Blight",
            "Tomato - Leaf Mold",
            "Tomato - Septoria Leaf Spot",
            "Tomato - Spider Mites",
            "Tomato - Target Spot",
            "Tomato - Mosaic Virus",
            "Tomato - Healthy"
    };

    // Private constructor for singleton
    private TFLoader() {}

    public static synchronized TFLoader getInstance() {
        if (instance == null) {
            instance = new TFLoader();
        }
        return instance;
    }

    /**
     * Load the CNN model
     */
    public void loadCNNModel(String modelPath) {
        if (isModelLoaded) {
            System.out.println("Model already loaded");
            return;
        }

        try {
            model = SavedModelBundle.load(modelPath, "serve");
            isModelLoaded = true;
            System.out.println("Model loaded successfully from: " + modelPath);
        } catch (Exception e) {
            System.err.println("Failed to load model: " + e.getMessage());
            throw new RuntimeException("Model loading failed", e);
        }
    }

    /**
     * Check if model is loaded
     */
    public boolean isModelLoaded() {
        return isModelLoaded;
    }

    /**
     * Preprocess image file for model input
     */
    private float[][][][] preprocessImage(String imagePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        if (originalImage == null) {
            throw new IOException("Failed to read image from: " + imagePath);
        }

        // Resize to 224x224
        BufferedImage resizedImage = new BufferedImage(224, 224, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage.getScaledInstance(224, 224, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        // Convert to float array
        float[][][][] input = new float[1][224][224][3];

        for (int h = 0; h < 224; h++) {
            for (int w = 0; w < 224; w++) {
                int rgb = resizedImage.getRGB(w, h);
                input[0][h][w][0] = (float) ((rgb >> 16) & 0xFF); // Red
                input[0][h][w][1] = (float) ((rgb >> 8) & 0xFF); // Green
                input[0][h][w][2] = (float) (rgb & 0xFF); // Blue
            }
        }

        return input;
    }

    /**
     * Predict plant disease from image file
     * Returns ONLY the formatted result string
     */
    public String predictFromImage(String imagePath) {
        if (!isModelLoaded) {
            return "Error: Model not loaded. Call loadCNNModel() first.";
        }

        try {
            // Preprocess the image
            float[][][][] inputData = preprocessImage(imagePath);

            // Get prediction
            Map<String, Object> result = predict(inputData);
            int predictedClass = (int) result.get("label");
            float confidence = (float) result.get("confidence");

            // Format and return the result
            String diseaseName = getLabelName(predictedClass);
            String confidenceStr = String.format("%.2f%%", confidence * 100);

            return String.format("Disease: %s\nConfidence: %s", diseaseName, confidenceStr);

        } catch (IOException e) {
            return "Error: Failed to read image - " + e.getMessage();
        } catch (Exception e) {
            return "Error: Prediction failed - " + e.getMessage();
        }
    }

    /**
     * Internal prediction method
     */
    private Map<String, Object> predict(float[][][][] inputData) {
        Map<String, Object> prediction = new HashMap<>();

        int batch = inputData.length;
        int height = inputData[0].length;
        int width = inputData[0][0].length;
        int channels = inputData[0][0][0].length;

        try (TFloat32 inputTensor = TFloat32.tensorOf(Shape.of(batch, height, width, channels))) {

            // Transfer data to tensor
            for (int b = 0; b < batch; b++) {
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        for (int c = 0; c < channels; c++) {
                            inputTensor.setFloat(inputData[b][h][w][c], b, h, w, c);
                        }
                    }
                }
            }

            // Run inference
            try (TFloat32 outputTensor = (TFloat32) model.session()
                    .runner()
                    .feed("serving_default_efficientnetb1_input:0", inputTensor)
                    .fetch("StatefulPartitionedCall:0")
                    .run()
                    .get(0)) {

                long numClasses = outputTensor.shape().size(1);

                // Find max probability
                int maxIdx = 0;
                float maxProb = outputTensor.getFloat(0, 0);

                for (int i = 0; i < numClasses; i++) {
                    float prob = outputTensor.getFloat(0, i);
                    if (prob > maxProb) {
                        maxProb = prob;
                        maxIdx = i;
                    }
                }

                prediction.put("label", maxIdx);
                prediction.put("confidence", maxProb);
            }

        } catch (Exception e) {
            throw new RuntimeException("Prediction failed", e);
        }

        return prediction;
    }

    /**
     * Get disease name from class index
     */
    private String getLabelName(int labelIndex) {
        if (labelIndex >= 0 && labelIndex < CLASS_LABELS.length) {
            return CLASS_LABELS[labelIndex];
        }
        return "Unknown (Class " + labelIndex + ")";
    }

    /**
     * Close the model (call when app shuts down)
     */
    public void close() {
        if (model != null) {
            model.close();
            model = null;
            isModelLoaded = false;
        }
    }
}