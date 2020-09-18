package com.example.consigliaviaggi19.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerLogin;
import com.example.consigliaviaggi19.R;

public class Activity_recupero_password extends AppCompatActivity {

    private EditText campo_email;
    private ControllerLogin Controller = new ControllerLogin(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupero_password);
        campo_email = findViewById(R.id.editTextEmail);
    }

    public void inviaIsClicked(View view) {
        String email = campo_email.getText().toString();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Inserire mail", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Mail non valida", Toast.LENGTH_SHORT).show();
        } else {
            Controller.sendRecuperoPasswordEmail(email);
        }

    }

    public void annullaIsClicked(View view) {
        finish();
    }




}
