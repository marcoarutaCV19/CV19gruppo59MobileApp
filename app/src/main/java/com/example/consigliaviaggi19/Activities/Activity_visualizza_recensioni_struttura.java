package com.example.consigliaviaggi19.Activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consigliaviaggi19.Controllers.ControllerStruttura;
import com.example.consigliaviaggi19.R;


public class Activity_visualizza_recensioni_struttura extends AppCompatActivity {

    private Spinner filtro_voto;
    private EditText campo_ricerca;
    private RecyclerView lista_recensioni;
    private ControllerStruttura Controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_recensioni_struttura);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        filtro_voto = findViewById(R.id.spinnerVoto);
        campo_ricerca = findViewById(R.id.campo_ricerca);
        lista_recensioni = findViewById(R.id.ListaRecensioni);

        Controller = new ControllerStruttura(getIntent().getExtras().getString("id"),this);
        Controller.setSpinner(filtro_voto);
        Controller.configureListaRecensioni(lista_recensioni);

    }


    protected void onStart(){
        super.onStart();
        Controller.showReviews();
    }

    protected void onStop(){

        super.onStop();
        Controller.removeReviews();
    }


    public void ricerca(View view) {
        if(!TextUtils.isEmpty(campo_ricerca.getText())){
            Controller.nameSearch(campo_ricerca.getText().toString());
        }else{
            Toast.makeText(this, "Inserire il nome di una struttura", Toast.LENGTH_SHORT).show();
        }
    }

    public void ordinaIsClicked(View view) {
        String voto = filtro_voto.getSelectedItem().toString();
        if(voto.length() == 0){
            Toast.makeText(this, "Inserire il filtro dei voti", Toast.LENGTH_SHORT).show();
            return;
        }

        Controller.orderReviews(lista_recensioni,voto);


    }


    public void segnalaIsClicked(View view){
        Controller.segnalaRecensione();
    }

    public void indietroIsClicked (View view) {finish();}

    public void menuIsClicked(View view) {
        Controller.showMenu();
    }
}

