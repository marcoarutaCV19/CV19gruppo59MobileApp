package com.example.consigliaviaggi19.Controllers;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.consigliaviaggi19.Activities.Activity_visualizza_struttura;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ControllerMappa {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference Strutture = database.collection("Strutture");
    private FragmentActivity Context;

    public ControllerMappa(FragmentActivity Activity) {
        Context = Activity;
    }

    //Metodi relativi a Activity_mappa
    public void insertMarkerStrutture(final GoogleMap myMap) {
        Strutture.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot Struttura : task.getResult()){

                            Map<String,Object> dati = Struttura.getData();
                            Double lat = (Double) dati.get("latitudine");
                            System.out.println(lat);
                            Double lng = (Double) dati.get("longitudine");
                            System.out.println(lng);
                            String nome = (String) dati.get("nome");
                            System.out.println(nome);
                            LatLng coordinate_posizione_struttura = new LatLng(lat.doubleValue(),lng.doubleValue());
                            myMap.addMarker(new MarkerOptions().position(coordinate_posizione_struttura).title(nome)
                                    .snippet(descrizioneStruttura((String)dati.get("tipologia"),(String)dati.get("indirizzo"),(String)dati.get("citta"))))
                                    .setIcon(setColor((String)dati.get("tipologia")));
                        }
                    }
                }
            });

        myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                final String nome = marker.getTitle();
                Strutture.whereEqualTo("nome",nome).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                            selectStruttura(document.getId());
                        }
                    }
                });
            }
        });
    }

    private void selectStruttura(String id_struttura){
        Intent activity_visualizza_struttura = new Intent(Context, Activity_visualizza_struttura.class);
        activity_visualizza_struttura.putExtra("id",id_struttura);
        Context.startActivity(activity_visualizza_struttura);

    }

    private BitmapDescriptor setColor(String categoria) {
        if(categoria.equals("Ristorante")){
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }else if(categoria.equals("Hotel")){
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        }else if (categoria.equals("Bar")){
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        }else if (categoria.equals("Club")){
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        }else{
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
        }

    }

    private String descrizioneStruttura(String tipologia, String indirizzo, String citta) {
        return tipologia+","+indirizzo+","+citta;
    }

}
