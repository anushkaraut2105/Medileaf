package com.megaproject.medileaf;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String message_of_day = Utility.getMessage();
        TextView textMessage = findViewById(R.id.text_message);
        textMessage.setText(message_of_day);

        FloatingActionButton actionCapture = findViewById(R.id.action_capture);
        FloatingActionButton actionUpload = findViewById(R.id.action_upload);

        Intent resultActivity = new Intent(getApplicationContext(), ResultActivity.class);


        ActivityResultLauncher<Intent> captureOrUpload = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                assert data != null;
                try {

                    this.image = (Bitmap) data.getExtras().get("data");
                }
                catch(NullPointerException ex) {

                    this.image = getFromUri(data.getData(), this);
                }

                String leafName = classifyImage(this);

                resultActivity.putExtra("leafName", leafName);
                startActivity(resultActivity);

            } else {
                Toast.makeText(this, "Cannot get image at this moment", Toast.LENGTH_LONG).show();
                this.image = null;
            }
        });



        actionCapture.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            captureOrUpload.launch(intent);
        });


        actionUpload.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);


            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            captureOrUpload.launch(intent);
        });
    }

    private Bitmap getFromUri(Uri uri, Activity activity) {

        try {

            return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + " while loading the image from gallery");
        }
        return null;
    }

    private String classifyImage(Activity activity) {
        String result = "";

        ImageHelper imageHelper = new ImageHelper(this.image);
        try {
            ClassificationHelper classificationHelper = new ClassificationHelper(activity, imageHelper.getTensorImage());
            result = classificationHelper.classify();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage() + " -- Exception in classifyImage() method");
        }
        return result;
    }
}