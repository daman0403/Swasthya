package com.example.android.swasthya;

public class heart_doctor {

    String location, name, speciality;

    public heart_doctor(){

    }

    public heart_doctor(String location, String name, String speciality) {
        this.location = location;
        this.name = name;
        this.speciality = speciality;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
