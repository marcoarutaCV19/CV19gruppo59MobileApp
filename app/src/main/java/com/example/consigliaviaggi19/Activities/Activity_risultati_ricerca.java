package com.example.consigliaviaggi19.Activities;


import android.os.Bundle;
import android.view.View;




import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.consigliaviaggi19.Controllers.ControllerRicerca;
import com.example.consigliaviaggi19.R;


public class Activity_risultati_ricerca extends AppCompatActivity {

    private RecyclerView risultati_ricerca;
    private ControllerRicerca Controller = new ControllerRicerca(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati_ricerca);

        risultati_ricerca = findViewById(R.id.strutture_ricercate);
        Controller.doRicerca(getIntent().getExtras(),risultati_ricerca);
    }



    protected void onStart(){
        super.onStart();
        Controller.showRisultati();
    }

    protected void onStop(){

        super.onStop();
        Controller.removeRisultati();
    }

    public void indietroIsClicked(View view) { finish(); }


    public void menuIsClicked(View view) { Controller.showMenu(); }

}
