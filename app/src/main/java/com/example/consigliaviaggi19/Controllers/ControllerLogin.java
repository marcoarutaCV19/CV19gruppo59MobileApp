package com.example.consigliaviaggi19.Controllers;


import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.consigliaviaggi19.Activities.Activity_recupero_password;
import com.example.consigliaviaggi19.Activities.Activity_signup;
import com.example.consigliaviaggi19.Activities.Activity_main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


public class ControllerLogin {
    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private AppCompatActivity Context;


    public ControllerLogin(AppCompatActivity Activity){
        Context = Activity;
    }


    //Metodo per Activity_recupero_password

    public void sendRecuperoPasswordEmail(String email){
        myAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Context, "Email con link di recupero inviata!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Context, "Impossibile inviare email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //Metodi per Activity_login

    public void doLogIn(String email, String password) {

        myAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            autoLogin();
                        } else {
                            Toast.makeText(Context, "Autenticazione fallita", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void autoLogin() {

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        database.setFirestoreSettings(settings);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Intent activity_main = new Intent(Context, Activity_main.class);
            Context.startActivity(activity_main);

        }

    }


    public void showActivitySignUp(){
        Intent activity_signup = new Intent(Context, Activity_signup.class);
        Context.startActivity(activity_signup);

    }

    public void showActivityRecuperoPassword(){
        Intent activity_recupera_password = new Intent(Context, Activity_recupero_password.class);
        Context.startActivity(activity_recupera_password);
    }

    //Metodi per input-checking

    public boolean isValidPassword(String password) { return password.length() != 0; }

    public boolean isValidMail(String email) { return email.contains("@"); }


}
