package com.example.android.swasthya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class heartDoctorActivity extends AppCompatActivity {

    RecyclerView RecyclerviewHeartDoctor;
    heart_adapter heart_adapter;
    ArrayList<heart_doctor> heart_doctorArrayList;
    FirebaseFirestore firebaseDB;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_doctor);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data... ");
        progressDialog.show();

        RecyclerviewHeartDoctor = findViewById(R.id.heartDoctorActivityRcL);
        RecyclerviewHeartDoctor.setHasFixedSize(true);
        RecyclerviewHeartDoctor.setLayoutManager(new LinearLayoutManager(this));


        firebaseDB = FirebaseFirestore.getInstance();
        heart_doctorArrayList = new ArrayList<heart_doctor>();
        heart_adapter = new heart_adapter(heartDoctorActivity.this, heart_doctorArrayList);

        RecyclerviewHeartDoctor.setAdapter(heart_adapter);

        EventChangeListner();

    }

    private void EventChangeListner() {
        firebaseDB.collection("DoctorInfo").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {

                        if(error != null){

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                heart_doctorArrayList.add(dc.getDocument().toObject(heart_doctor.class));
                            }

                            heart_adapter.notifyDataSetChanged();

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }


                        }

                    }
                });
    }
}