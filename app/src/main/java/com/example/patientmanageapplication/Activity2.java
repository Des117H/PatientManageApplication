package com.example.patientmanageapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.patientmanageapplication.patient.Patient;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Activity2 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String fullName, phoneNumber, email, dateOfBirth;
    EditText attendedDateEditText, descriptionEditText;
    TextView welcomeTextView;
    Spinner doctorSpinner;
    Button toMainActBtn;
    Patient patient2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Bundle extras = getIntent().getExtras();

        patient2 = new Patient(
                extras.getString("full_name"),
                extras.getString("date_of_birth"),
                extras.getString("phone_number"),
                extras.getString("email"));

        welcomeTextView = findViewById(R.id.welcomeStrTextView);
        attendedDateEditText = findViewById(R.id.attendedDateEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        doctorSpinner = findViewById(R.id.doctorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.doctor, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        doctorSpinner.setAdapter(adapter);

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Date dayOfBirth;
        try {
            dayOfBirth = dateFormat.parse(this.dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String welcomeStr = "Welcome " + fullName + " to RMIT's Health Care.\nYou can send request for help below.";

        welcomeTextView.setText(welcomeStr);

        doctorSpinner.getSelectedItem().toString();

        toMainActBtn = findViewById(R.id.sendBtn);

        toMainActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields()) {
                    String data = String.join("|", fullName, dateOfBirth, phoneNumber,
                            email, attendedDateEditText.getText().toString(),
                            doctorSpinner.getSelectedItem().toString(),
                            descriptionEditText.getText().toString());

                    saveFile("data.txt", data);

                    Intent intent = new Intent(Activity2.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void datePicker(View view){
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
        setDate(cal);
    }
    public void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        ((EditText)findViewById(R.id.attendedDateEditText))
                .setText(dateFormat.format(calendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onDateClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.show();
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                attendedDateEditText.setText(dayOfMonth+ "-" + month + "-" +year);
            }
        });
    }

    private boolean checkAllFields() {
        boolean checked = true;

        String attendedDateStr = attendedDateEditText.getText().toString();
        if (attendedDateStr.length() == 0) {
            attendedDateEditText.setError("Age is required");
            checked = false;
        } else {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            try {
                Date date = dateFormat.parse(attendedDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                attendedDateEditText.setError("Attended date is not valid");
                checked = false;
            }
        }

        if (descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setError("Description is required");
            checked = false;
        }

        // after all validation return true.
        return checked;
    }

    public void saveFile(String file, String text){
        try{
            FileOutputStream fileOutputStream = openFileOutput(file,
                    Context.MODE_PRIVATE);
            fileOutputStream.write(text.getBytes());
            fileOutputStream.close();
            Toast.makeText(Activity2.this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Activity2.this, "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }
}