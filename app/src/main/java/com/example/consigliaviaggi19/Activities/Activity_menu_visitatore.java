package com.example.consigliaviaggi19.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerMain;
import com.example.consigliaviaggi19.R;


public class Activity_menu_visitatore extends AppCompatActivity {

    private ControllerMain Controller = new ControllerMain(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_visitatore);
    }

    public void logInIsClicked(View view) {
        Controller.doLogIn();
    }

    public void signupIsClicked(View view) {
        Controller.doSignUp();
    }

    public void indietroIsClicked(View view) {
        finish();
    }
}
