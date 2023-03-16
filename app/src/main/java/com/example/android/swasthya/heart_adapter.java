package com.example.android.swasthya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import androidx.annotation.*;

import java.util.ArrayList;

public class heart_adapter extends RecyclerView.Adapter<heart_adapter.heart_viewHolder> {

    Context context;
    ArrayList<heart_doctor> heart_doctorsArrayList;

    public heart_adapter(Context context, ArrayList<heart_doctor> heart_doctorsArrayList) {
        this.context = context;
        this.heart_doctorsArrayList = heart_doctorsArrayList;
    }

    //    @NonNull
    @Override
    public heart_adapter.heart_viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.heart_doctor_rcl_item, parent, false);
        return new heart_viewHolder(v);
    }

    @Override
    public void onBindViewHolder(heart_adapter.heart_viewHolder holder, int position) {

        heart_doctor heart_doctor = heart_doctorsArrayList.get(position);
        holder.doctorName.setText(heart_doctor.name);
        holder.hospitalName.setText(heart_doctor.location);
        holder.doctorSpeciality.setText(heart_doctor.speciality);

    }

    @Override
    public int getItemCount() {
        return heart_doctorsArrayList.size();
    }

    public static class heart_viewHolder extends RecyclerView.ViewHolder{

        TextView doctorSpeciality,doctorName, hospitalName;

        public heart_viewHolder(View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.doctorName);
            doctorSpeciality = itemView.findViewById(R.id.doctorSpeciality);
            hospitalName = itemView.findViewById(R.id.hospitalName);


        }
    }

}
