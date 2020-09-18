package com.example.consigliaviaggi19.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.consigliaviaggi19.Controllers.ControllerMain;
import com.example.consigliaviaggi19.Controllers.ControllerRicerca;
import com.example.consigliaviaggi19.R;

public class Activity_main extends AppCompatActivity {

    private EditText campo_ricerca;
    private RecyclerView lista_strutture;
    private ControllerMain Controller = new ControllerMain(this);
    private ControllerRicerca Controlla = new ControllerRicerca(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        campo_ricerca = findViewById(R.id.campo_ricerca);
        lista_strutture = findViewById(R.id.Strutture);
        Controller.showStrutture(lista_strutture);



    }


    protected void onStart(){
        super.onStart();
        Controller.showList();
    }

    protected void onStop(){
        super.onStop();
        Controller.removeList();
    }

    public void ricerca(View view) {
        if(!TextUtils.isEmpty(campo_ricerca.getText())){
            String nome = campo_ricerca.getText().toString();
            if (nome == "Hotel" || nome == "hotel" || nome == "albergo" || nome == "alberghi" || nome == "Albergo" || nome == "Alberghi" || nome =="b&b") {
                Controlla.startRicercaAvanzata("Hotel", null,null);
            }else if (nome == "Ristorante" || nome == "ristorante" || nome == "Ristoranti" || nome == "ristoranti"){
                Controller.categorySearch("Ristorante");
            }else if (nome == "Club" || nome == "locale" || nome == "locali" || nome == "discoteche" || nome == "discoteca"){
                Controller.categorySearch("Club");
            }else if (nome == "Bar" || nome == "bar" || nome == "lounge bar" || nome == "cafe"){
                Controller.categorySearch("Bar");
            }else if (nome == "Museo" || nome == "museo" || nome == "Musei" || nome == "musei"){
                Controller.categorySearch("Museo");
            }else {
                Controlla.nameSearch(nome);
            }
        }else{
            Toast.makeText(this, "Inserire il nome di una struttura", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo relativo all'OnClick su mappa
    public void mapIsClicked(View view) {
        Controller.viewMappa();
    }

    public void menuIsClicked(View view) { Controller.showMenu(); }

    public void ricercaAvanzataIsClicked(View view) { Controller.advancedSearch(); }

}
