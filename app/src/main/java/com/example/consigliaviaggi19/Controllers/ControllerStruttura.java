package com.example.consigliaviaggi19.Controllers;

import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consigliaviaggi19.Activities.Activity_login;
import com.example.consigliaviaggi19.Activities.Activity_menu;
import com.example.consigliaviaggi19.Activities.Activity_menu_visitatore;
import com.example.consigliaviaggi19.Activities.Activity_risultati_ricerca;
import com.example.consigliaviaggi19.Activities.Activity_visualizza_struttura;
import com.example.consigliaviaggi19.Activities.Activity_visualizza_recensioni_struttura;
import com.example.consigliaviaggi19.Adapters.AdapterRecensioniStruttura;
import com.example.consigliaviaggi19.Models.Recensioni;
import com.example.consigliaviaggi19.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ControllerStruttura {
    private AppCompatActivity Context;
    private String testo;
    private String idAutore;
    private String recensione_selezionata;
    private String id_struttura;
    private Query recensioni;
    private boolean flag = true;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference notebookRef = database.collection("Strutture");
    private AdapterRecensioniStruttura adapter;
    private double voti=0;
    private int count=0;
    private double totale=0;
    private double media=0;
    private Button segnala;

    public ControllerStruttura(String id,AppCompatActivity Activity){
        id_struttura = id;
        Context = Activity;
        final FirebaseUser utente = FirebaseAuth.getInstance().getCurrentUser();
        if (utente != null) {
            if (Context.getClass() == Activity_visualizza_struttura.class) {
                flag = alreadyWrittenReview(mAuth.getCurrentUser().getUid());
            }
        }

    }

    //Metodi per Activity_visualizza_struttura

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

    public void showStruttura(final TextView nome_struttura,final TextView descrizione_struttura,final ImageView immagine_struttura, final TextView valutazione_struttura) {
        final DocumentReference datiStruttura = notebookRef.document(id_struttura);

        datiStruttura.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        nome_struttura.setText(document.getString("nome"));
                        descrizione_struttura.setText(document.getString("descrizione"));
                        valutazione_struttura.setText(document.getDouble("valutazione").toString());
                        Picasso.get().load(document.getString("immagine")).into(immagine_struttura);
                    }

                }
            }
        });

    }


    public void showRecensioneStruttura() {

        Intent Recensioni_struttura = new Intent(Context, Activity_visualizza_recensioni_struttura.class);
        Recensioni_struttura.putExtra("id",id_struttura);
        Context.startActivity(Recensioni_struttura);
    }

    public void postaRecensione(String testo, final float voto_recensione) {

        final FirebaseUser utente = FirebaseAuth.getInstance().getCurrentUser();
        if (utente != null) {

            if (!flag) {
                Toast.makeText(Context, "Hai già recensito questa struttura!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkText(testo) && checkRange(voto_recensione)) {
                String user_id = mAuth.getCurrentUser().getUid();
                if (user_id.length() == 0) {
                    Toast.makeText(Context, "IdAutore non trovato!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> nuova_recensione = new HashMap<>();
                nuova_recensione.put("testo", testo);
                nuova_recensione.put("voto", voto_recensione);
                nuova_recensione.put("idAutore", user_id);
                nuova_recensione.put("struttura", id_struttura);


                database.collection("Recensione").add(nuova_recensione)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Context, "Recensione pubblicata!", Toast.LENGTH_SHORT).show();
                                flag = false;
                                Log.d("Recensione", "DocumentSnapshot added with ID: " + documentReference.getId());
                                database.collection("Recensione").whereEqualTo("struttura", id_struttura)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i("Recensioni", "Tutto ok");
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        String voto = String.valueOf(document.get("voto"));
                                                        voti = Double.parseDouble(voto);
                                                        totale += voti;
                                                        count++;


                                                    }
                                                    media = totale / count;
                                                    double avg = Math.round(media*10)/10.0;
                                                    notebookRef.document(id_struttura).update("valutazione", avg);

                                                }
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Context, "Errore", Toast.LENGTH_SHORT).show();
                                Log.w("Recensione", "Error adding document", e);
                            }
                        });

            }
        }else{
            Intent activity_login = new Intent(Context, Activity_login.class);
            Context.startActivity(activity_login);
        }


    }

    public boolean alreadyWrittenReview(final String user_id) {

        database.collection("Recensione").whereEqualTo("idAutore",user_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for( DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                        if(document.getString("struttura").equals(id_struttura)){
                            flag = false;
                            break;
                        }
                    }

                }
            }
        });
        return flag;
    }


    public void nameSearch(final String nome){
        Intent activity_risultati_ricerca = new Intent(Context, Activity_risultati_ricerca.class);
        activity_risultati_ricerca.putExtra("Tipo ricerca", "PerNome");
        activity_risultati_ricerca.putExtra("nome", nome);
        Context.startActivity(activity_risultati_ricerca);
    }


    //Metodi per Activity_visualizza_recensioni_struttura

    public void configureListaRecensioni(RecyclerView lista_recensioni) {

        recensioni = database.collection("Recensione").whereEqualTo("struttura",id_struttura);
        FirestoreRecyclerOptions<Recensioni> options = new FirestoreRecyclerOptions.Builder<Recensioni>().setQuery(recensioni,Recensioni.class).build();
        adapter = new AdapterRecensioniStruttura(options);
        lista_recensioni.setHasFixedSize(true);
        lista_recensioni.setLayoutManager(new LinearLayoutManager(Context));
        lista_recensioni.setAdapter(adapter);
        adapter.setOnItemClickListner(new AdapterRecensioniStruttura.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot docSnapshot, int position) {
                idAutore = docSnapshot.getString("idAutore");
                recensione_selezionata = docSnapshot.getId();
                testo = docSnapshot.getString("testo");
                segnala = ((Activity_visualizza_recensioni_struttura) Context).findViewById(R.id.buttonSegnala);
                segnala.setClickable(true);
                segnala.setEnabled(true);
            }
        });
    }

    public void segnalaRecensione(){

        final DocumentReference datiutente = database.collection("Utenti").document(idAutore);

        datiutente.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot){
            if (documentSnapshot.exists()) {
                final Map<String, Object> segnalazione = new HashMap<>();
                segnalazione.put("idAutore",idAutore);
                segnalazione.put("nickname", documentSnapshot.getString("nickname"));
                segnalazione.put("struttura", id_struttura);
                segnalazione.put("testo",testo);
                segnalazione.put("recensione",recensione_selezionata);
                database.collection("Segnalazioni").add(segnalazione).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Context, "Segnalazione Inviata", Toast.LENGTH_SHORT).show();
                        Log.d("Segnalazioni", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Context, "Errore", Toast.LENGTH_SHORT).show();
                        Log.w("Segnalazioni", "Error adding document", e);
                    }
                });

            }else{
                Toast.makeText(Context, "Errore", Toast.LENGTH_SHORT).show();
            }
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.d("Segnalazioni","segnalazione non invitata");
        }
    });

}
    public void showReviews() {
        adapter.startListening();
    }


    public void removeReviews() {
        adapter.stopListening();
    }



    public void orderReviews(RecyclerView lista_recensioni, String voto) {
        Query filtro = recensioni.whereLessThanOrEqualTo("voto",Integer.parseInt(voto)).whereGreaterThan("voto",Integer.parseInt(voto)-1);
        FirestoreRecyclerOptions<Recensioni> options = new FirestoreRecyclerOptions.Builder<Recensioni>().setQuery(filtro,Recensioni.class).build();
        adapter.stopListening();
        adapter = new AdapterRecensioniStruttura(options);
        adapter.setOnItemClickListner(new AdapterRecensioniStruttura.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot docSnapshot, int position) {
                idAutore = docSnapshot.getString("idAutore");
                recensione_selezionata = docSnapshot.getId();
                testo = docSnapshot.getString("testo");
                Toast.makeText(Context,"Recensione selezionata",Toast.LENGTH_SHORT).show();
            }
        });

        lista_recensioni.setAdapter(adapter);

        adapter.startListening();
    }


     public void setSpinner(Spinner filtro_voto) {
        ArrayAdapter<CharSequence> adapter_recensioni = ArrayAdapter.createFromResource(Context, R.array.Recensioni, android.R.layout.simple_spinner_dropdown_item);
        filtro_voto.setAdapter(adapter_recensioni);
    }

    //Metodi per input-checking

    private boolean checkRange(Float voto) {
        if(voto == 0.0){
            Toast.makeText(Context, "Inserire voto recensione", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkText(String testo) {
        if(testo.length() == 0){
            Toast.makeText(Context, "Inserire testo recensione", Toast.LENGTH_SHORT).show();
            return false;
        }else if(testo.length() < 15){
            Toast.makeText(Context, "La recensione deve essere di almeno 15 caratteri", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}