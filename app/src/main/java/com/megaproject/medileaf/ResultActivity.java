package com.megaproject.medileaf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResultActivity extends AppCompatActivity {
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        JsonReader reader;
        Intent intent = getIntent();

        String leafName = intent.getExtras().getString("leafName");

        try {

            InputStream in = getApplicationContext().getAssets().open("data.json");
            reader = new JsonReader(new InputStreamReader(in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DataHelper dataHelper;
        try {
            dataHelper = new DataHelper(reader, leafName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Leaf leaf = dataHelper.leaf;

        String resourceName = leafName.replace("\\s+-", "").toLowerCase();
        resourceName = resourceName.replace("-", "").toLowerCase();

        @SuppressLint("DiscouragedApi")
        int resId = getResources().getIdentifier(resourceName, "drawable", getApplicationInfo().packageName);

        ImageView imageMedicine = findViewById(R.id.image_medicine);

        imageMedicine.setImageDrawable(ResourcesCompat.getDrawable(getResources(), resId, getTheme()));

        TextView textTitle = findViewById(R.id.text_title);
        TextView textScientific = findViewById(R.id.text_scientific);
        TextView textOrigin = findViewById(R.id.text_origin);
        TextView textFeature = findViewById(R.id.text_feature);
        TextView textDesc = findViewById(R.id.text_desc);

        textTitle.setText(leaf.getLeafName());
        textScientific.setText(leaf.getScientificName());
        textOrigin.setText(leaf.getOrigin());
        textFeature.setText(leaf.getFeature());
        String desc = leaf.getDescription() + " " + leaf.getUsage();
        textDesc.setText(desc);
    }
}