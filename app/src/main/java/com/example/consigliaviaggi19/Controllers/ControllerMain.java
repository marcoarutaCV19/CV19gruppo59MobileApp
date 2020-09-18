package com.example.consigliaviaggi19.Controllers;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.consigliaviaggi19.Activities.Activity_cancellazione;
import com.example.consigliaviaggi19.Activities.Activity_login;
import com.example.consigliaviaggi19.Activities.Activity_mappa;
import com.example.consigliaviaggi19.Activities.Activity_menu;
import com.example.consigliaviaggi19.Activities.Activity_impostazioni;
import com.example.consigliaviaggi19.Activities.Activity_menu_visitatore;
import com.example.consigliaviaggi19.Activities.Activity_ricerca_avanzata;
import com.example.consigliaviaggi19.Activities.Activity_signup;
import com.example.consigliaviaggi19.Activities.Activity_verifica_identita;
import com.example.consigliaviaggi19.Activities.Activity_visualizza_struttura;
import com.example.consigliaviaggi19.Activities.Activity_risultati_ricerca;
import com.example.consigliaviaggi19.Activities.Activity_riepilogo_recensioni;
import com.example.consigliaviaggi19.Adapters.AdapterRecensioni;
import com.example.consigliaviaggi19.Adapters.AdapterStrutture;
import com.example.consigliaviaggi19.Models.Recensioni;
import com.example.consigliaviaggi19.Models.Strutture;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class ControllerMain {
    private AppCompatActivity Context;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = database.collection("Strutture");
    private int flag = 0;
    private AdapterStrutture adapter;
    private AdapterRecensioni adapter_recensioni;

    public ControllerMain(AppCompatActivity Activity){
        Context = Activity;
    }

    //Metodi relativi a Activity_Impostazioni

    public void autofill(final EditText campo_nome, final EditText campo_cognome, final EditText campo_username){
        final FirebaseUser utente = FirebaseAuth.getInstance().getCurrentUser();
        final String id_utente= utente.getUid();
        final DocumentReference datiUtente = notebookRef.document(id_utente);

        datiUtente.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){

                        campo_nome.setHint(document.getString("nome"));
                        campo_cognome.setHint(document.getString("descrizione"));
                        campo_username.setHint(document.getString("nickname"));

                    }

                }
            }
        });

    }

    //Metodi relativi a Activity_menu

    public void doLogOut(){
        Intent login =new Intent(Context, Activity_login.class);
        FirebaseAuth.getInstance().signOut();
        Context.finish();
        Context.startActivity(login);
    }


    public void advancedSearch(){
        Intent intent_ricerca = new Intent(Context, Activity_ricerca_avanzata.class);
        Context.startActivity(intent_ricerca);
    }

    public void showRecensioniPersonali(){
        Intent intent_recensioni = new Intent(Context, Activity_riepilogo_recensioni.class);
        Context.startActivity(intent_recensioni);

    }

    public void showImpostazioni(){
        Intent intent_impostazioni = new Intent(Context, Activity_impostazioni.class);
        Context.startActivity(intent_impostazioni);

    }


    //Metodo relativi a Activity_menu_visitatore

    public void doLogIn(){
        Intent activity_login =new Intent(Context, Activity_login.class);
        Context.startActivity(activity_login);
    }

    public void doSignUp(){
        Intent activity_signup =new Intent(Context, Activity_signup.class);
        Context.startActivity(activity_signup);
    }

    //Metodi relativi a Activity_main

    public void viewMappa(){
        Intent Mappa = new Intent(Context, Activity_mappa.class);
        Context.startActivity(Mappa);
    }

    public void categorySearch(String tipo_struttura){
        Intent Ricerca = new Intent(Context, Activity_risultati_ricerca.class);
        Ricerca.putExtra("Tipo Struttura",tipo_struttura);
        Ricerca.putExtra("Tipo ricerca","Category button");
        Context.startActivity(Ricerca);

    }


    public void showMenu(){

        final FirebaseUser utente = FirebaseAuth.getInstance().getCurrentUser();
        if (utente != null){
            Intent activity_menu = new Intent(Context, Activity_menu.class);
            Context.startActivity(activity_menu);
        }else{
            Intent activity_menu_visitatore = new Intent(Context, Activity_menu_visitatore.class);
            Context.startActivity(activity_menu_visitatore);
        }

    }

    public void showStrutture(RecyclerView lista_strutture){
        Query ordinamento = notebookRef.orderBy("valutazione",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Strutture> options = new FirestoreRecyclerOptions.Builder<Strutture>().setQuery(ordinamento,Strutture.class).build();
        adapter = new AdapterStrutture(options);

        adapter.setOnItemClickListner(new AdapterStrutture.OnItemClickListner(){
            @Override
            public void onItemClick(DocumentSnapshot docSnapshot, int position) {
                String id_struttura = docSnapshot.getId();
                selectStruttura(id_struttura);
            }
        });

        lista_strutture.setHasFixedSize(false);
        lista_strutture.setLayoutManager(new LinearLayoutManager(Context));
        lista_strutture.setAdapter(adapter);

    }

    private void selectStruttura(String id_struttura) {
        Intent activity_visualizza_struttura = new Intent(Context, Activity_visualizza_struttura.class);
        activity_visualizza_struttura.putExtra("id",id_struttura);
        Context.startActivity(activity_visualizza_struttura);
    }

    public void showList(){
        adapter.startListening();
    }

    public void removeList(){
        adapter.stopListening();
    }

    //Metodi relativi a Activity_riepilogo_recensioni

    public void showMyReviews(RecyclerView lista_recensioni){
        CollectionReference Recensioni = database.collection("Recensione");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser utente = mAuth.getCurrentUser();
        Query recensioni_utente = Recensioni.whereEqualTo("idAutore",utente.getUid());
        FirestoreRecyclerOptions<Recensioni> options= new FirestoreRecyclerOptions.Builder<Recensioni>().setQuery(recensioni_utente, Recensioni.class).build();

        adapter_recensioni = new AdapterRecensioni(options);

        lista_recensioni.setHasFixedSize(true);
        lista_recensioni.setLayoutManager(new LinearLayoutManager(Context));
        lista_recensioni.setAdapter(adapter_recensioni);
    }

    public void showReviews(){
        adapter_recensioni.startListening();
    }

    public void removeReviews(){
        adapter_recensioni.stopListening();
    }

    //Metodi relativi a Activity_impostazioni

    public void verificaAccount() {
        Intent activity_verifica_identita = new Intent(Context, Activity_verifica_identita.class);
        Context.startActivity(activity_verifica_identita);
    }

    public void modifyPassword(final String old_password,final String new_password){
        FirebaseUser utente = FirebaseAuth.getInstance().getCurrentUser();
        WriteBatch batch = database.batch();
        AuthCredential credenziali_di_accesso = EmailAuthProvider.getCredential(utente.getEmail(), old_password);

        utente.reauthenticate(credenziali_di_accesso).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("password", "Password updated");
                                Toast.makeText(Context, "Password aggiornata",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("password", "Error password not updated");
                                Toast.makeText(Context, "Impossibile aggiornare",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Context,"Password non corretta",
                            Toast.LENGTH_SHORT).show();
                    Log.d("password", "Error auth failed");
                }

            }
        });
        batch.commit();

    }


    //Metodi relativi a Activity_verifica_identita
    public int checkPassword(final String password) {

        FirebaseUser utente = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential dati_accesso = EmailAuthProvider.getCredential(utente.getEmail(), password);
        utente.reauthenticate(dati_accesso).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    flag = 1;
                    System.out.println("SUCCESSO");
                } else {
                    flag = 0;
                    Toast.makeText(Context,"Password non corretta", Toast.LENGTH_SHORT).show();
                    Log.d("password", "Error auth failed");
                }

            }
        });
        return flag;
    }

    public void cancellazioneAccount(){
        Intent cancellazione = new Intent(Context, Activity_cancellazione.class);
        Context.startActivity(cancellazione);

    }


    //Metodo relativo a Activity_cancellazione

    public void sendCancellationRequest(final String motivazione) {
        if(motivazione.isEmpty()){
            Toast.makeText(Context, "Inserire una motivazione", Toast.LENGTH_SHORT).show();
        }else if(motivazione.length() < 5){
            Toast.makeText(Context, "La motivazione deve essere di almeno 5 caratteri", Toast.LENGTH_SHORT).show();
        }
        final FirebaseUser utente = FirebaseAuth.getInstance().getCurrentUser();
        final String id_utente= utente.getUid();
        final DocumentReference datiutente = database.collection("Utenti").document(id_utente);

        datiutente.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                if (documentSnapshot.exists()) {
                    String  nickname= documentSnapshot.getString("nickname");
                    final Map<String, Object> obj = new HashMap<>();
                    obj.put("motivazione", motivazione);
                    obj.put("email", utente.getEmail());
                    obj.put("nickname", nickname);
                    obj.put("idUtente",id_utente);

                    database.collection("Cancellazioni")
                            .add(obj)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Context, "Richiesta Inviata", Toast.LENGTH_SHORT).show();
                                    Log.d("Cancellazioni", "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Context, "Errore", Toast.LENGTH_SHORT).show();
                                    Log.w("Cancellazioni", "Error adding document", e);
                                }
                            });
                }
            }
        });
    }


}

