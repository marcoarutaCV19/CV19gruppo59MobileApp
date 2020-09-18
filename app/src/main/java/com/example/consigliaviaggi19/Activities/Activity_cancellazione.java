package com.example.consigliaviaggi19.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerMain;
import com.example.consigliaviaggi19.R;



public class Activity_cancellazione extends AppCompatActivity{

    private ControllerMain Controller = new ControllerMain(this);
    private EditText motivazione;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellazione);

        motivazione = findViewById(R.id.editTextMotivazione);
    }

    public void annullaIsClicked(View view) {
        finish();
    }



    public void avantiIsClicked(View view) {
        Controller.sendCancellationRequest(motivazione.getText().toString());
    }


}