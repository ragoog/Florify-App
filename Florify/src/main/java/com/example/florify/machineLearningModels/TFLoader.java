package com.example.florify.machineLearningModels;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.ndarray.buffer.DataBuffers;
import org.tensorflow.ndarray.buffer.FloatDataBuffer;
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

    private SavedModelBundle model;

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

    public void loadCNNModel(String modelPath) {
        try {
            model = SavedModelBundle.load(modelPath, "serve");
            System.out.println("Model loaded successfully from: " + modelPath);
        } catch (Exception e) {
            System.err.println("Failed to load model: " + e.getMessage());
            throw new RuntimeException("Model loading failed", e);
        }
    }

    /**
     * Preprocess image file for model input
     * @param imagePath path to the image file
     * @return preprocessed image data [1][224][224][3]
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

        // Convert to float array with EfficientNet preprocessing [-1, 1]
        float[][][][] input = new float[1][224][224][3];

        for (int h = 0; h < 224; h++) {
            for (int w = 0; w < 224; w++) {
                int rgb = resizedImage.getRGB(w, h);
                // EfficientNet preprocessing: (pixel / 127.5) - 1.0
                input[0][h][w][0] = (((rgb >> 16) & 0xFF) / 127.5f) - 1.0f; // Red
                input[0][h][w][1] = (((rgb >> 8) & 0xFF) / 127.5f) - 1.0f;  // Green
                input[0][h][w][2] = ((rgb & 0xFF) / 127.5f) - 1.0f;         // Blue
            }
        }

        return input;
    }

    /**
     * Predict plant disease from image file
     * @param imagePath path to the plant image
     * @return prediction result as a formatted string
     */
    public String predictFromImage(String imagePath) {
        if (model == null) {
            return "Error: Model not loaded. Call loadCNNModel() first.";
        }

        try {
            // Preprocess the image
            float[][][][] inputData = preprocessImage(imagePath);

            // Get prediction
            Map<String, Object> result = predict(inputData);
            int predictedClass = (int) result.get("label");
            float confidence = (float) result.get("confidence");

            // Format the result
            String diseaseName = getLabelName(predictedClass);
            String confidenceStr = String.format("%.2f%%", confidence * 100);

            return String.format("Disease: %s | Confidence: %s", diseaseName, confidenceStr);

        } catch (IOException e) {
            return "Error: Failed to read image - " + e.getMessage();
        } catch (Exception e) {
            return "Error: Prediction failed - " + e.getMessage();
        }
    }

    /**
     * Internal prediction method - FIXED VERSION
     */
    private Map<String, Object> predict(float[][][][] inputData) {
        Map<String, Object> prediction = new HashMap<>();

        int batch = inputData.length;
        int height = inputData[0].length;
        int width = inputData[0][0].length;
        int channels = inputData[0][0][0].length;

        // Create tensor with proper shape
        try (TFloat32 inputTensor = TFloat32.tensorOf(Shape.of(batch, height, width, channels))) {

            // CRITICAL FIX: Use the NdArray API to write data directly
            // This properly transfers your data to the tensor
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

                System.out.println("\n=== All Class Probabilities ===");
                for (int i = 0; i < numClasses; i++) {
                    float prob = outputTensor.getFloat(0, i);
                    System.out.printf("%-35s: %6.2f%%\n", CLASS_LABELS[i], prob * 100);

                    if (prob > maxProb) {
                        maxProb = prob;
                        maxIdx = i;
                    }
                }
                System.out.println("================================\n");

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

    public void close() {
        if (model != null) {
            model.close();
            model = null;
        }
    }

    public void printModelSignature() {
        if (model == null) {
            System.out.println("Model not loaded");
            return;
        }

        System.out.println("\n=== Model Signature Details ===");
        model.metaGraphDef().getSignatureDefMap().forEach((signatureKey, signatureDef) -> {
            System.out.println("\nSignature: " + signatureKey);

            System.out.println("  Inputs:");
            signatureDef.getInputsMap().forEach((inputKey, tensorInfo) -> {
                System.out.println("    Key: " + inputKey);
                System.out.println("    Name: " + tensorInfo.getName());
                System.out.println("    Dtype: " + tensorInfo.getDtype());
                System.out.println("    Shape: " + tensorInfo.getTensorShape());
            });

            System.out.println("  Outputs:");
            signatureDef.getOutputsMap().forEach((outputKey, tensorInfo) -> {
                System.out.println("    Key: " + outputKey);
                System.out.println("    Name: " + tensorInfo.getName());
                System.out.println("    Dtype: " + tensorInfo.getDtype());
                System.out.println("    Shape: " + tensorInfo.getTensorShape());
            });
        });
        System.out.println("=================================\n");
    }
}


// Test class
class TestCNN {
    public static void main(String[] args) {
        TFLoader loader = new TFLoader();

        try {
            String modelPath = "C:\\Users\\omaro\\Florify-App\\Florify\\src\\main\\resources\\plant_disease_detector";
            loader.loadCNNModel(modelPath);

            // Test with an image file
            String imagePath = "C:\\Users\\omaro\\OneDrive\\Desktop\\Study\\Java Project\\New folder\\Plant_disease\\Some images to test with manually\\Septoria-Leaf-Spot-Tomato-Diseases.jpg";

            // Get prediction as string
            String result = loader.predictFromImage(imagePath);
            System.out.println("\nPrediction Result:");
            System.out.println(result);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            loader.close();
        }
    }
}