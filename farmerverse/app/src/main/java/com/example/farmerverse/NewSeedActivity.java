package com.example.farmerverse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmerverse.entities.Seed;
import com.google.gson.Gson;

public class NewSeedActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.seedlistsql.REPLY";
    private EditText seedName;
    private EditText spaceBetween;
    private EditText quantity;
    private EditText growthTime;
    private EditText weightPerSeed;
    private double spaceBetweenDouble;
    private double quantityDouble;
    private int growthTimeInt;
    private double weightPerSeedDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_seed);

        seedName = findViewById(R.id.editTxtSeedName);
        spaceBetween = findViewById(R.id.editTxtSpaceBetween);
        quantity = findViewById(R.id.editTxtQuantity);
        growthTime = findViewById(R.id.editTxtGrowthTime);
        weightPerSeed = findViewById(R.id.editTxtWeightPerSeed);

        final Button submitBtn = findViewById(R.id.btnSubmit);
        final Button cancelBtn = findViewById(R.id.btnCancel);

        Gson gson = new Gson();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFormValid()){
                    Intent replyIntent = new Intent();
                    Seed seed = new Seed(seedName.getText().toString(), spaceBetweenDouble, quantityDouble, growthTimeInt, weightPerSeedDouble);
                    replyIntent.putExtra(EXTRA_REPLY, gson.toJson(seed));
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
                else{
                    Toast.makeText(NewSeedActivity.this, "Make sure the form is filled in correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isFormValid(){
        try{
            spaceBetweenDouble = Double.parseDouble(spaceBetween.getText().toString());
            quantityDouble = Double.parseDouble(quantity.getText().toString());
            growthTimeInt = Integer.parseInt(growthTime.getText().toString());
            weightPerSeedDouble = Double.parseDouble(weightPerSeed.getText().toString());
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}