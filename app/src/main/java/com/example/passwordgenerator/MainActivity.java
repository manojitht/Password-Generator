package com.example.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Creating the variables ....
    Pinview pin;
    Button Generator, clear, saveToFile;
    EditText description1, description2, box1, box2, box3, box4;
    TextView textViewDate;
    String gText, g1Text, b1Text, b2Text, b3Text, b4Text, pText, tText, lText;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewDate = findViewById(R.id.timeAndDate); // Declaring the variables with correct ID.
        description1 = findViewById(R.id.textPassword);
        description2 = findViewById(R.id.textPassword1);
        box1 = findViewById(R.id.num1);
        box2 = findViewById(R.id.num2);
        box3 = findViewById(R.id.num3);
        box4 = findViewById(R.id.num4);
        Generator = findViewById(R.id.generator);
        clear = findViewById(R.id.clear);
        saveToFile = findViewById(R.id.saveFile);
        //---------------------------------------------------------------//
        Random random = new Random();  // Generating the random number...
        final int number = random.nextInt(99999999);//
        //--------------------------------------------------------------//
        Calendar calendar = Calendar.getInstance();  // Creating the Date and time format ...
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE -- dd-MMM-yyyy -- hh:mm a");
        final String dateTime = simpleDateFormat.format(calendar.getTime());
        textViewDate.setText(dateTime);//
        //-------------------------------------------------------------//
        Generator.setOnClickListener(new View.OnClickListener() { // generates the random number while clicking on btn..
            @Override
            public void onClick(View view) { // Button that generates the random method on to the pin view.

                pin = findViewById(R.id.pinview);
                pin.setPinBackgroundRes(R.drawable.sample_background);
                pin.setInputType(Pinview.InputType.NUMBER);
                pin.setValue(String.valueOf(number));
            }
        });
        //-----------------------------------------------------------//
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // clears the all fields.
                pin.setValue("00000000");
                description1.setText("");
                description2.setText("");
                box1.setText("");
                box2.setText("");
                box3.setText("");
                box4.setText("");
            }
        });

        saveToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gText = description1.getText().toString();
                g1Text = description2.getText().toString();
                b1Text = box1.getText().toString();
                b2Text = box2.getText().toString();
                b3Text = box3.getText().toString();
                b4Text = box4.getText().toString();
                pText = pin.getValue();
                tText = textViewDate.getText().toString();

                lText = gText + ": " + pText + "\n" + g1Text + ": " + b1Text + b2Text + b3Text + b4Text + "\n" + tText;

                if (lText.isEmpty()){
                    Toast.makeText(MainActivity.this, "Some fields are missing please fill it!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_CODE);
                        }
                        else {
                            saveToTxtFile(lText);
                        }
                    }
                    else {
                        saveToTxtFile(lText);
                    }
                }
            }
        });
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    saveToTxtFile(lText);
                }
                else {
                    Toast.makeText(this, "Storage permission is required.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveToTxtFile(String lText) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        try {
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/My Files");
            dir.mkdirs();
            String fileName = gText+ "_" + timeStamp + ".txt";

            File file = new File(dir, fileName);

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(lText);
            bw.close();

            Toast.makeText(this, gText + " file is saved to "  + dir, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}