package com.example.android.swasthya;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LinearLayout heartDoctorLL;
    FirebaseFirestore firebaseFirestoredb;

    String userID;
    String locationAdress;

    ProgressDialog progressDialog;
    LocationManager locationManager;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    private final static int locationREQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        heartDoctorLL = findViewById(R.id.heart_ll_main_activity);
        firebaseFirestoredb = FirebaseFirestore.getInstance();
        if(ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION } , 100);

        }else{
            getCurrentLocation();
        }
//        firebaseAuth = FirebaseAuth.getInstance();
//        userID = firebaseAuth.getCurrentUser().getUid().toString();
////        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
////        getLastLoaction();
//
        heartDoctorLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Fetching data... ");
                progressDialog.show();
                locationAdress = "-43.5565 -51.351";
                String name = userID;
                String requestTime = "10101";
                String speciality = "heart";

                Map<String,Object> request = new HashMap<>();

                request.put("location", locationAdress);
                request.put("name", name);
                request.put("requestTime", requestTime);
                request.put("speciality", speciality);

                firebaseFirestoredb.collection("Requests")
                        .add(request)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(MainActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, heartDoctorActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "Error: Request not sent", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }



//    private void getLastLoaction() {
//
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
//
//            fusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if(location != null){
//
//                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                                List<Address> addresses = null;
//                                try {
//                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                                    latitude = addresses.get(0).getLatitude();
//                                    longitude = addresses.get(0).getLongitude();
//                                    locationAdress = latitude + "" + longitude;
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
//
//        }
//        else{
//            askPermission();
//        }
//    }
//
//    private void askPermission() {
//
//        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == locationREQUEST_CODE) {
//
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getLastLoaction();
//            }
//            else{
//                Toast.makeText(MainActivity.this, "Please grant the required permission", Toast.LENGTH_SHORT).show();
//            }
//
//        }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

//
    @Override
    public void onLocationChanged(@NonNull Location location) {


            Toast.makeText(this , location.getLatitude()+" "+location.getLatitude(),Toast.LENGTH_SHORT).show();


    }
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 500 , 0 , MainActivity.this);
        }else{

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));


        }



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == 100) {
getCurrentLocation();
        }
    }
}