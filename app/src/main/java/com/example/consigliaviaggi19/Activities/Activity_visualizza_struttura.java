package com.example.consigliaviaggi19.Activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consigliaviaggi19.Controllers.ControllerStruttura;
import com.example.consigliaviaggi19.R;


public class Activity_visualizza_struttura extends AppCompatActivity {

    private EditText campo_ricerca;
    private EditText testo_recensione;
    private TextView nome_struttura;
    private TextView descrizione_struttura;
    private TextView valutazione_struttura;
    private ImageView immagine_struttura;
    private RatingBar voto_recensione;
    private Button button_pubblica;
    private ControllerStruttura Controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_struttura);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        campo_ricerca = findViewById(R.id.campo_ricerca);
        testo_recensione= findViewById(R.id.editTextPersonalReview);
        voto_recensione = findViewById(R.id.ratingBar);
        immagine_struttura = findViewById(R.id.imageStruttura);
        nome_struttura = findViewById(R.id.textViewNomeStruttura);
        descrizione_struttura = findViewById(R.id.textViewDescrizione);
        valutazione_struttura = findViewById(R.id.textViewValutazione);
        button_pubblica = findViewById(R.id.buttonPubblica);

        Controller = new ControllerStruttura(getIntent().getExtras().getString("id"), this);
        Controller.showStruttura(nome_struttura,descrizione_struttura,immagine_struttura, valutazione_struttura);


    }





    public void showRecensioniStruttura(View view) { Controller.showRecensioneStruttura(); }

    public void pubblicaRecensione(View view) {
        Controller.postaRecensione(testo_recensione.getText().toString(), voto_recensione.getRating());


    }


    public void ricerca(View view) {
        if(!TextUtils.isEmpty(campo_ricerca.getText())){
            Controller.nameSearch(campo_ricerca.getText().toString());
        }else{
            Toast.makeText(this, "Inserire il nome di una struttura", Toast.LENGTH_SHORT).show();
        }
    }


    public void menuIsClicked(View view) { Controller.showMenu();}

    public void indietroIsClicked(View view) {finish();}
}
