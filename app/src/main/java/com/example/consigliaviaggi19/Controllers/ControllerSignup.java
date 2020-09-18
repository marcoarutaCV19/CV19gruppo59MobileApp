package com.example.consigliaviaggi19.Controllers;


import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.consigliaviaggi19.Activities.Activity_main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ControllerSignup {
    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private AppCompatActivity Context;
    boolean bool;

    public ControllerSignup(AppCompatActivity Activity){
        Context = Activity;
    }

    //Metodi per Activity_signup

    public boolean createUser(final String email, final String password, final String nome, final String cognome, final String nickname, final int check) {
        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Context, "Iscrizione avvenuta con successo", Toast.LENGTH_SHORT).show();
                    bool = true;
                    if (check == 1){
                        final String username = nome+" "+cognome;
                        addFirestore(username, nome, cognome);
                    }else{
                        addFirestore(nickname, nome, cognome);
                    }
                    Intent main_activity = new Intent(Context, Activity_main.class);
                    Context.finish();
                    Context.startActivity(main_activity);

                } else {
                    Toast.makeText(Context, "Iscrizione fallita", Toast.LENGTH_SHORT).show();
                    bool = false;
                }
            }
        });
    return  bool;

    }

    private void addFirestore(String nickname, String nome, String cognome) {

        FirebaseUser id_utente = myAuth.getCurrentUser();

        Map<String, Object> new_user = new HashMap<>();
        new_user.put("nome", nome);
        new_user.put("cognome", cognome);

        new_user.put("nickname", nickname);
        new_user.put("idUtente", id_utente.getUid());

        database.collection("Utenti").document(id_utente.getUid()).set(new_user);

    }


    //Metodi per input-checking

    public boolean isValidName(String name) { return name.length() > 2; }

    public boolean isValidMail(String email) { return email.contains("@"); }

    public boolean isValidPassword(String password, String ripeti_password) { return password.equals(ripeti_password) && password.length() > 4; }


}


