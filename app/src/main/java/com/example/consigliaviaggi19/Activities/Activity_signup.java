package com.example.consigliaviaggi19.Activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerSignup;
import com.example.consigliaviaggi19.R;

public class Activity_signup extends AppCompatActivity{

    private EditText campo_nome;
    private EditText campo_cognome;
    private EditText campo_email;
    private EditText campo_password;
    private EditText campo_ripeti_password;
    private EditText campo_username;
    private ControllerSignup Controller = new ControllerSignup(this);

    @Override
    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        campo_nome = findViewById(R.id.editTextNome);
        campo_cognome = findViewById(R.id.editTextCognome);
        campo_email = findViewById(R.id.editTextEmail);
        campo_password = findViewById(R.id.editTextPassword);
        campo_password.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        campo_ripeti_password = findViewById(R.id.editTextPassword2);
        campo_username = findViewById(R.id.editTextUsername);

    }

    //Metodo relativo all'OnClick del bottone Conferma
    public void iscrivitiIsClicked(View view) {
        String nome = campo_nome.getText().toString();
        String cognome = campo_cognome.getText().toString();
        String email = campo_email.getText().toString();
        String password = campo_password.getText().toString();
        String ripeti_password = campo_ripeti_password.getText().toString();
        String username = campo_username.getText().toString();
        CheckBox whippedCheckBox = findViewById(R.id.checkBox);
        boolean flag = whippedCheckBox.isChecked();
        boolean bool;

        if (!Controller.isValidName(nome)) {
            Toast.makeText(getApplicationContext(), "Inserire nome", Toast.LENGTH_SHORT).show();
        } else if (!Controller.isValidName(cognome)) {
            Toast.makeText(getApplicationContext(), "Inserire cognome", Toast.LENGTH_SHORT).show();
        } else if (!Controller.isValidMail(email)) {
            Toast.makeText(getApplicationContext(), "Inserire una mail valida", Toast.LENGTH_SHORT).show();
        } else if (!Controller.isValidPassword(password, ripeti_password)) {
            Toast.makeText(getApplicationContext(), "Password non coincidente o inferiore a 5 caratteri", Toast.LENGTH_SHORT).show();
        } else if (!Controller.isValidName(username)){
            Toast.makeText(getApplicationContext(), "Inserire username", Toast.LENGTH_SHORT).show();
        }else{
            if (flag){
                bool = Controller.createUser(email, password,nome,cognome,username,1);
            }else{
                bool = Controller.createUser(email, password,nome,cognome,username,0);
            }
            if (bool == true){
                    System.out.println("Testing Instance true: signup");
            }else{
                System.out.println("Testing Instance false: signup");
            }
        }

    }

    //Metodo relativo all'OnClick della textView Annulla
    public void annullaIsClicked(View view) {
        finish();
    }


}