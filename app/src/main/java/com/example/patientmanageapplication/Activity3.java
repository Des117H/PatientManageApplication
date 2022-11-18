package com.example.patientmanageapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        TextView fullNameTextView = findViewById(R.id.fullNameTextView),
                dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView),
                phoneNumberTextView= findViewById(R.id.phoneNumberTextView),
                emailTextView= findViewById(R.id.emailTextView),
                attendedDateTextView = findViewById(R.id.attendedDateTextView),
                doctorTextView = findViewById(R.id.doctorTextView),
                descriptionTextView = findViewById(R.id.descriptionTextView);

        String[] separatedData = readFile("data.txt").split("\\|");

        if (separatedData.length == 7) {
            fullNameTextView.setText(separatedData[0]);
            dateOfBirthTextView.setText(separatedData[1]);
            phoneNumberTextView.setText(separatedData[2]);
            emailTextView.setText(separatedData[3]);
            attendedDateTextView.setText(separatedData[4]);
            doctorTextView.setText(separatedData[5]);
            descriptionTextView.setText(separatedData[6]);
        }
    }

    public String readFile(String file) {
        String text = "";
        try {
            FileInputStream fileInputStream = openFileInput(file);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            text = new String(buffer);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Activity3.this, "Error reading file",
                    Toast.LENGTH_SHORT).show();
        }
        return text;
    }
}