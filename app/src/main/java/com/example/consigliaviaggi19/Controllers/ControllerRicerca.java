package com.example.consigliaviaggi19.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.consigliaviaggi19.Activities.Activity_menu;
import com.example.consigliaviaggi19.Activities.Activity_menu_visitatore;
import com.example.consigliaviaggi19.Activities.Activity_visualizza_struttura;
import com.example.consigliaviaggi19.Activities.Activity_risultati_ricerca;
import com.example.consigliaviaggi19.Activities.Activity_main;
import com.example.consigliaviaggi19.Adapters.AdapterStrutture;
import com.example.consigliaviaggi19.Models.Strutture;
import com.example.consigliaviaggi19.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ControllerRicerca {
    private AppCompatActivity Context;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = database.collection("Strutture");
    private AdapterStrutture adapter;



    public ControllerRicerca(AppCompatActivity Activity){
        Context = Activity;
    }

    //Metodi per Activity_ricerca_avanzata

    public void setSpinners(Spinner citta, Spinner categoria, Spinner recensioni) {
        ArrayAdapter<CharSequence> adapter_città = ArrayAdapter.createFromResource(Context, R.array.Città, android.R.layout.simple_spinner_dropdown_item);
        citta.setAdapter(adapter_città);
        ArrayAdapter<CharSequence> adapter_categoria = ArrayAdapter.createFromResource(Context, R.array.Categoria, android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(adapter_categoria);
        ArrayAdapter<CharSequence> adapter_recensioni = ArrayAdapter.createFromResource(Context, R.array.Recensioni, android.R.layout.simple_spinner_dropdown_item);
        recensioni.setAdapter(adapter_recensioni);
    }


    public void nameSearch(final String nome){
        Intent activity_risultati_ricerca = new Intent(Context, Activity_risultati_ricerca.class);
        activity_risultati_ricerca.putExtra("Tipo ricerca", "PerNome");
        activity_risultati_ricerca.putExtra("nome", nome);
        Context.startActivity(activity_risultati_ricerca);
    }

    private void selectStruttura(String id_struttura) {
        Intent activity_visualizza_struttura = new Intent(Context, Activity_visualizza_struttura.class);
        activity_visualizza_struttura.putExtra("id",id_struttura);
        Context.startActivity(activity_visualizza_struttura);
    }

    public void showMenu() {
        final FirebaseUser utente = FirebaseAuth.getInstance().getCurrentUser();
        if (utente != null){
            Intent activity_menu = new Intent(Context, Activity_menu.class);
            Context.startActivity(activity_menu);
        }else{
            Intent activity_menu_visitatore = new Intent(Context, Activity_menu_visitatore.class);
            Context.startActivity(activity_menu_visitatore);
        }
    }

    public void showHome() {
        Intent activity_main = new Intent(Context, Activity_main.class);
        Context.startActivity(activity_main);
    }

    public void startRicercaAvanzata(String categoria, String citta, String recensioni) {


        Intent activity_risultati_ricerca = new Intent(Context, Activity_risultati_ricerca.class);
        activity_risultati_ricerca.putExtra("Tipo ricerca", "Avanzata");
        activity_risultati_ricerca.putExtra("Città", citta);
        activity_risultati_ricerca.putExtra("Categoria", categoria);
        activity_risultati_ricerca.putExtra("Recensioni", recensioni);
        Context.startActivity(activity_risultati_ricerca);
    }

    public void doRicerca(Bundle dati_ricevuti, RecyclerView risultati_ricerca){

        String tipo_ricerca = dati_ricevuti.getString("Tipo ricerca");

        if(tipo_ricerca.equals("Avanzata")) {

            doAdvancedSearch(dati_ricevuti.getString("Città"), dati_ricevuti.getString("Categoria")
                    , dati_ricevuti.getString("Recensioni"), risultati_ricerca);

        }else if(tipo_ricerca.equals("PerNome")){

            doNameSearch(dati_ricevuti.getString("nome"), risultati_ricerca);

        }else{
            doCategorySearch(dati_ricevuti.getString("Tipo Struttura"),risultati_ricerca);

        }


        adapter.setOnItemClickListner(new AdapterStrutture.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot docSnapshot, int position) {
                String id_struttura = docSnapshot.getId();
                selectStruttura(id_struttura);
            }
        });
    }

    private void doAdvancedSearch(String citta, String categoria, String recensioni, RecyclerView risultati_ricerca){

        Query risultato = constructQuery(citta,categoria,recensioni);

        FirestoreRecyclerOptions<Strutture> options = new FirestoreRecyclerOptions.Builder<Strutture>().setQuery(risultato,Strutture.class).build();
        adapter = new AdapterStrutture(options);


        risultati_ricerca.setHasFixedSize(true);
        risultati_ricerca.setLayoutManager(new LinearLayoutManager(Context));
        risultati_ricerca.setAdapter(adapter);

    }

    private void doNameSearch(String struttura, RecyclerView risultati_ricerca){
        int count = 0;
        StringBuilder name = new StringBuilder(struttura.toLowerCase());
        do{
            name.replace(count, count + 1, name.substring(count,count + 1).toUpperCase());
            count=name.indexOf(" ", count) + 1;
        }while(count > 0 && count < name.length());
        String struct = name.toString();
        String prefix = (String) struct.subSequence(0,name.length());
        Query strutture = notebookRef.orderBy("nome").startAt(prefix).endAt(prefix + "\uf8ff");
        FirestoreRecyclerOptions<Strutture> options = new FirestoreRecyclerOptions.Builder<Strutture>().setQuery(strutture,Strutture.class).build();
        adapter = new AdapterStrutture(options);

        adapter.setOnItemClickListner(new AdapterStrutture.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot docSnapshot, int position) {
                String id_struttura = docSnapshot.getId();
                selectStruttura(id_struttura);
            }
        });

        risultati_ricerca.setHasFixedSize(true);
        risultati_ricerca.setLayoutManager(new LinearLayoutManager(Context));
        risultati_ricerca.setAdapter(adapter);


    }

    private void doCategorySearch(String tipo_struttura, RecyclerView risultati_ricerca) {
        Query strutture = notebookRef.whereEqualTo("tipologia",tipo_struttura);
        FirestoreRecyclerOptions<Strutture> options = new FirestoreRecyclerOptions.Builder<Strutture>().setQuery(strutture,Strutture.class).build();
        adapter = new AdapterStrutture(options);


        risultati_ricerca.setHasFixedSize(true);
        risultati_ricerca.setLayoutManager(new LinearLayoutManager(Context));
        risultati_ricerca.setAdapter(adapter);


    }

    public void showRisultati() {
        adapter.startListening();
    }

    public void removeRisultati() {
        adapter.stopListening();
    }


    private Query constructQuery(String città, String categoria, String recensioni) {
        Query filtro_città;
        Query filtro_categoria;
        Query risultato;

        if(notFilteredError(città,categoria,recensioni)){
            return notebookRef.orderBy("valutazione", Query.Direction.ASCENDING);
        }

        if(!città.isEmpty()){
            filtro_città = notebookRef.whereEqualTo("citta",città);
            if(!categoria.isEmpty()){
                filtro_categoria = filtro_città.whereEqualTo("tipologia",convertCategory(categoria));
                if(!recensioni.isEmpty()){
                        risultato = filtro_categoria.whereGreaterThanOrEqualTo("valutazione", Float.parseFloat(recensioni)).whereLessThanOrEqualTo("valutazione", Float.parseFloat(recensioni)+0.9);  }else{
                    return filtro_categoria;
                }
            }else{
                if(!recensioni.isEmpty()){

                    risultato = filtro_città.whereGreaterThanOrEqualTo("valutazione", Float.parseFloat(recensioni)).whereLessThanOrEqualTo("valutazione", Float.parseFloat(recensioni)+0.9);   }else{
                    return filtro_città;
                }
            }
        }else{
            if(!categoria.isEmpty()){
                filtro_categoria = notebookRef.whereEqualTo("tipologia", convertCategory(categoria));
                if(!recensioni.isEmpty()){
                    risultato = filtro_categoria.whereGreaterThanOrEqualTo("valutazione", Float.parseFloat(recensioni)).whereLessThanOrEqualTo("valutazione", Float.parseFloat(recensioni)+0.9);       }else{
                    return filtro_categoria;
                }
            }else{
                return notebookRef.whereGreaterThanOrEqualTo("valutazione", Float.parseFloat(recensioni)).whereLessThanOrEqualTo("valutazione", Float.parseFloat(recensioni)+0.9);

            }
        }

        return risultato;
    }

    private boolean notFilteredError(String città, String categoria, String recensioni) {
        return città.isEmpty() && categoria.isEmpty() && recensioni.isEmpty();
    }

    private String convertCategory(String categoria){
        if(categoria.equals("Ristorante")){
            return "Ristorante";
        }else if(categoria.equals("Hotel")){
            return "Hotel";
        }else if (categoria.equals("Bar")){
            return "Bar";
        }else if (categoria.equals("Club")){
            return "Club";
        } else{
            return "Museo";
        }

    }
}