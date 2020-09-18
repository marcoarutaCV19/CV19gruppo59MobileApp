package com.example.consigliaviaggi19.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerMain;
import com.example.consigliaviaggi19.R;


public class Activity_menu extends AppCompatActivity {

    private ControllerMain Controller = new ControllerMain(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void logOutIsClicked(View view) {
        Controller.doLogOut();
    }

    public void riepilogoRecensioniIsClicked(View view) {
        Controller.showRecensioniPersonali();
    }

    public void impostazioniIsClicked(View view) {
        Controller.showImpostazioni();
    }

    public void indietroIsClicked(View view) {
        finish();
    }
}
