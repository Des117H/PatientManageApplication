package com.example.patientmanageapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.patientmanageapplication.patient.Patient;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button toAct2Button, toAct3Button;

    EditText fullNameEditText, dateOfBirthEditText, emailEditText, phoneEditText;

    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toAct2Button = findViewById(R.id.toAct2Btn);
        toAct3Button = findViewById(R.id.toAct3Btn);

        fullNameEditText = findViewById(R.id.fullNameEditText);
        dateOfBirthEditText = findViewById(R.id.dateOfBirthEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);

        toAct2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields()) {
                    Intent intentAct2 = new Intent(MainActivity.this, Activity2.class);
                    patient = new Patient(
                            fullNameEditText.getText().toString(),
                            dateOfBirthEditText.getText().toString(),
                            phoneEditText.getText().toString(),
                            emailEditText.getText().toString());

                    intentAct2.putExtra("full_name", patient.getFullName());
                    intentAct2.putExtra("date_of_birth", patient.getDateOfBirth());
                    intentAct2.putExtra("phone_number", patient.getPhoneNumber());
                    intentAct2.putExtra("email", patient.getEmail());

                    startActivity(intentAct2);
                }
            }
        });

        toAct3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAct3 = new Intent(MainActivity.this, Activity3.class);

                startActivity(intentAct3);
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

        ((EditText)findViewById(R.id.dateOfBirthEditText))
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
                dateOfBirthEditText.setText(dayOfMonth+ "-" + month + "-" +year);
            }
        });
    }

    private boolean checkAllFields() {
        boolean checked = true;
        if (fullNameEditText.length() == 0) {
            fullNameEditText.setError("Full name is required");
            checked = false;
        } else if (!isValidName(fullNameEditText.getText().toString())) {
            fullNameEditText.setError("Full name is not valid");
            checked = false;
        }

        String dateOfBirthStr = dateOfBirthEditText.getText().toString();
        if (dateOfBirthStr.length() == 0) {
            dateOfBirthEditText.setError("Age is required");
            checked = false;
        } else {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            try {
                Date date = dateFormat.parse(dateOfBirthStr);
            } catch (ParseException e) {
                e.printStackTrace();
                dateOfBirthEditText.setError("Attended date is not valid");
                checked = false;
            }
        }

        if (emailEditText.length() == 0) {
            emailEditText.setError("Email is required");
            checked = false;
        } else if (!isValidEmail(emailEditText.getText().toString())) {
            emailEditText.setError("Email is not valid");
            checked = false;
        }

        Pattern phonePattern = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        if (phoneEditText.length() == 0) {
            phoneEditText.setError("Phone number is required");
            checked = false;
        } else if (!isValidPhoneNumber(phoneEditText.getText().toString())) {
            phoneEditText.setError("Phone number is not valid");
            checked = false;
        }

        // after all validation return true.
        return checked;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

//    https://stackoverflow.com/questions/44421036/check-if-name-is-valid-with-proper-case-and-max-one-space
    public static boolean isValidName(String fullName)
    {
        String nameRegex = "[A-Z][a-z]+(?: [A-Z][a-z]*)*";

        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String str)
    {
//      10 digit phone number
        String phoneRegex = "0[0-9]{9}";

        return (str.matches(phoneRegex));
    }
}