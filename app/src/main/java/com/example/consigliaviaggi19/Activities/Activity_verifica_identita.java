package com.example.consigliaviaggi19.Activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerMain;
import com.example.consigliaviaggi19.R;


public class Activity_verifica_identita extends AppCompatActivity {

    private EditText campo_password;
    private int flag;
    private ControllerMain Controller = new ControllerMain(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifica_identita);

        campo_password = findViewById(R.id.editTextPassword);
    }

    public void annullaIsClicked(View view) { finish(); }

    public void avantiIsClicked(View view) {

        String password = campo_password.getText().toString();
        if(!TextUtils.isEmpty(password)){

            flag = Controller.checkPassword(password);

        }else{
            Toast.makeText(this, "Autenticazione Fallita", Toast.LENGTH_SHORT).show();
            flag = 0;
        }

        if (flag == 1) {
            Controller.cancellazioneAccount();
        }

    }

}




