package com.example.consigliaviaggi19.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consigliaviaggi19.Controllers.ControllerMain;
import com.example.consigliaviaggi19.R;


public class Activity_riepilogo_recensioni extends AppCompatActivity {

    RecyclerView lista_recensioni;
    private ControllerMain Controller = new ControllerMain(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riepilogo_recensioni);
        lista_recensioni = findViewById(R.id.ListaRecensioni);
        Controller.showMyReviews(lista_recensioni);
    }

    protected void onStart(){
        super.onStart();
        Controller.showReviews();
    }

    protected void onStop(){

        super.onStop();
        Controller.removeReviews();
    }

    public void indietroIsClicked(View view) {
        finish();
    }



}
