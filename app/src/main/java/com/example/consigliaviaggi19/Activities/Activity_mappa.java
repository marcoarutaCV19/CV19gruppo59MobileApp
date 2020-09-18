package com.example.consigliaviaggi19.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.consigliaviaggi19.Controllers.ControllerMappa;
import com.example.consigliaviaggi19.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Activity_mappa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private LocationManager user_position_handler;
    private LocationListener update_user_position;
    private ControllerMappa Controller = new ControllerMappa(this);


    //Verifica che l'utente accetti la richiesta da parte del dispositivo di conoscere la sua posizione attuale
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                user_position_handler.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, update_user_position);
            }else{
                finish();
            }

        }else{
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_mappa);
        //Ottengo il SupportMapFragment e vengo notificato una volta che la mappa è pronta per l'uso
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    /**
     * Manipola la mappa una volta che è disponibile
     * Questa callback è attivata quando la mappa è pronta per l'utilizzo.
     * In questo sarà possibile muovere la mappa e quindi settare un marker che per default setteremo
     * presso Castel dell'Ovo com'è indicato sul footage della mappa all'interno dell'activity_main.
     * Per rendere funzionante ciò si è usufruito dei Google Play Services implementati all'iterno delle
     * dependencies del build.gradle al livello applicativo.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        user_position_handler = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        update_user_position = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng posizione_attuale_utente = new LatLng(location.getLatitude(),location.getLongitude());
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(posizione_attuale_utente).title("Eccoti!"));
                myMap.moveCamera(CameraUpdateFactory.newLatLng(posizione_attuale_utente));
                Controller.insertMarkerStrutture(myMap);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //Check per verifica permessi posizione attuale utente
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            user_position_handler.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, update_user_position);

            Location ultima_posizione_utente = user_position_handler.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng coordinate_ultima_posizione;
            if (ultima_posizione_utente != null) {
                coordinate_ultima_posizione = new LatLng(ultima_posizione_utente.getLatitude(),ultima_posizione_utente.getLongitude());
            }else{
                coordinate_ultima_posizione = new LatLng(40.8283197,14.2465101);
            }
            myMap.clear();
            myMap.addMarker(new MarkerOptions().position(coordinate_ultima_posizione).title("Castel dell'Ovo"));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate_ultima_posizione,10));
            Controller.insertMarkerStrutture(myMap);

        }


    }



}
