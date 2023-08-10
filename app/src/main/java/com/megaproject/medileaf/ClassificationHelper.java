package com.megaproject.medileaf;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;



public class ClassificationHelper {


    private final TensorImage image;

    private final Interpreter model;

    private final Activity activity;

    public ClassificationHelper(Activity activity, TensorImage image) throws IOException {

        this.activity = activity;
        this.image = image;

        String modelPath = "medinet.tflite";


        try (AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
             FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {


            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declareLength = fileDescriptor.getDeclaredLength();


            this.model = new Interpreter(fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declareLength));

        } catch (IOException e) {
            throw new IOException(e.getMessage() + " -- occurred when loading the model in ClassificationHelper constructor");
        }

    }

    public String classify() {

        TensorBuffer probabilityBuffer = TensorBuffer.createFixedSize(new int[]{1, 30}, DataType.FLOAT32);

        model.run(this.image.getBuffer(), probabilityBuffer.getBuffer());

        Map<String, Float> results = getLabels(probabilityBuffer);

        return this.bestResult(results);
    }
    private Map<String, Float> getLabels(TensorBuffer probabilityBuffer) {

        List<String> associatedAxisLabels = null;

        try {
            String labelsPath = "labels.txt";

            associatedAxisLabels = FileUtil.loadLabels(this.activity, labelsPath);
        } catch (IOException e) {
            Log.e("tfliteSupport", "Error reading label file", e);
        }


        TensorProcessor probabilityProcessor = new TensorProcessor.Builder().add(new NormalizeOp(0, 255)).build();

        Map<String, Float> floatMap = null;
        if (null != associatedAxisLabels) {
            TensorLabel labels = new TensorLabel(associatedAxisLabels, probabilityProcessor.process(probabilityBuffer));

            floatMap = labels.getMapWithFloatValue();
        }
        return floatMap;
    }
    private String bestResult(Map<String, Float> results) {

        double currentMax = 0.0;

        String maxLabel = "";

        for (Map.Entry<String, Float> mapElement : results.entrySet()) {
            Float prob = mapElement.getValue();
            if (prob > currentMax) {
                currentMax = prob;
                maxLabel = mapElement.getKey();
            }
        }

        return maxLabel;
    }
}
