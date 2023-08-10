package com.megaproject.medileaf;

import android.graphics.Bitmap;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;


public class ImageHelper {


    public TensorImage tensorImage;

    public ImageHelper(Bitmap image) {

        this.tensorImage = new TensorImage(DataType.FLOAT32);

        this.tensorImage.load(image);

        this.tensorImage = getImageProcessor().process(this.tensorImage);

    }

    public TensorImage getTensorImage() {
        return this.tensorImage;
    }

    private ImageProcessor getImageProcessor() {

        return new ImageProcessor.Builder()
                .add(new ResizeOp(150, 150, ResizeOp.ResizeMethod.BILINEAR))
                .build();
    }
}
