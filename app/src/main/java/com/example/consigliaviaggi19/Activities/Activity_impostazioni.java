package com.example.consigliaviaggi19.Activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerMain;
import com.example.consigliaviaggi19.R;

public class Activity_impostazioni extends AppCompatActivity {

    private  EditText campo_nome;
    private EditText campo_cognome;
    private EditText campo_email;
    private EditText campo_username;
    private EditText campo_vecchia_password;
    private EditText campo_nuova_password;
    private EditText campo_ripeti_password;
    private ControllerMain Controller = new ControllerMain(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        campo_nome = findViewById(R.id.editTextNome);
        campo_cognome = findViewById(R.id.editTextCognome);
        campo_email = findViewById(R.id.editTextEmail);
        campo_username = findViewById(R.id.editTextUsername);
        campo_vecchia_password = findViewById(R.id.editTextOldPassword);
        campo_nuova_password = findViewById(R.id.editTextNewPassword);
        campo_ripeti_password = findViewById(R.id.editTextNewPassword2);
        Controller.autofill(campo_nome, campo_cognome, campo_username);
    }

    public void annullaIsClicked(View view) {
        finish();
    }

    public void salvaIsClicked(View view) {

        String old_password = campo_vecchia_password.getText().toString();
        if(!TextUtils.isEmpty(old_password)){
            String new_password = campo_nuova_password.getText().toString();
            if(!TextUtils.isEmpty(new_password)){
                String ripeti_new_password = campo_ripeti_password.getText().toString();
                if(!TextUtils.isEmpty(ripeti_new_password)){
                    if(ripeti_new_password.equals(new_password)){
                        Controller.modifyPassword(old_password,new_password);
                    }else
                        Toast.makeText(this, "Le password non coincidono", Toast.LENGTH_SHORT).show();
                }
            }
        }



    }

    public void cancellaAccountIsClicked(View view) { Controller.verificaAccount(); }
}
