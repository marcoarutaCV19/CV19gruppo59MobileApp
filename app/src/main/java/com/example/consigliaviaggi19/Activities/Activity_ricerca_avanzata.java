package com.example.consigliaviaggi19.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerRicerca;
import com.example.consigliaviaggi19.R;


public class Activity_ricerca_avanzata extends AppCompatActivity {

    private EditText campo_ricerca;
    private Spinner seleziona_città;
    private Spinner seleziona_categoria;
    private Spinner seleziona_recensioni;
    private ControllerRicerca Controller= new ControllerRicerca(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_avanzata);

        campo_ricerca = findViewById(R.id.campo_ricerca);
        seleziona_città = findViewById(R.id.spinnercitta);
        seleziona_categoria = findViewById(R.id.spinnercategoria);
        seleziona_recensioni = findViewById(R.id.spinnerrecensioni);
        Controller.setSpinners(seleziona_città,seleziona_categoria,seleziona_recensioni);



    }



    public void ricerca(View view) {
        if(!TextUtils.isEmpty(campo_ricerca.getText())){
            Controller.nameSearch(campo_ricerca.getText().toString());
        }else{
            Toast.makeText(this, "Inserire il nome di una struttura!", Toast.LENGTH_SHORT).show();
        }
    }



    public void cercaIsClicked(View view) {

        Controller.startRicercaAvanzata(seleziona_categoria.getSelectedItem().toString(),
                seleziona_città.getSelectedItem().toString(),seleziona_recensioni.getSelectedItem().toString());
    }

    public void menuIsClicked(View view) { Controller.showMenu(); }



    public void homeIsClicked(View view) { Controller.showHome();}
}

