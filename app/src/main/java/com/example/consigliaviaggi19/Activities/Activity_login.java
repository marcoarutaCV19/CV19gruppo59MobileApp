package com.example.consigliaviaggi19.Activities;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.consigliaviaggi19.Controllers.ControllerLogin;
import com.example.consigliaviaggi19.R;


public class Activity_login extends AppCompatActivity {

    private EditText campo_email;
    private EditText campo_password;
    private ControllerLogin Controller = new ControllerLogin(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        campo_email = findViewById(R.id.editTextEmail);
        campo_password = findViewById(R.id.editTextPassword);
        Controller.autoLogin();

    }


    @Override
    public void onStart() {
        super.onStart();
    }



    //Metodo relativo all'OnClick del bottone di Accesso
    public void accediIsClicked(View view) {

        String email=campo_email.getText().toString();
        String password=campo_password.getText().toString();

        if (!Controller.isValidMail(email)) {
            Toast.makeText(getApplicationContext(), "Inserire una mail valida", Toast.LENGTH_SHORT).show();
        } else if (!Controller.isValidPassword(password)) {
            Toast.makeText(getApplicationContext(), "La password deve avere almeno 5 caratteri", Toast.LENGTH_SHORT).show();
        } else {
            Controller.doLogIn(email,password);
        }


    }
    //Metodo relativo all'OnClick del bottone di Iscrizione
    public void iscrivitiIsClicked(View view) {
        Controller.showActivitySignUp();
    }

    //Metodo relativo all'OnClick della textView Recupera password
    public void recuperaPasswordIsClicked(View view) {
        Controller.showActivityRecuperoPassword();
    }



}
