package com.hfad.healthstory.firebase_classes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;
import com.hfad.healthstory.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

public class StoryImage {

    public static final int TYPE_SNIMOK = 1;
    public static final int TYPE_SPRAVKA = 2;
    public static final int TYPE_ANALIZ = 3;
    public static final int TYPE_VISIT = 4;
    public static final int TYPE_OTHER = 5;

   //
    String pathInStorage;
    Date date;
   // Date_str date;
   // String doctorId;
    ArrayList<Integer> tegs;

    public StoryImage() {
        this.tegs = new ArrayList<Integer>();
    }

    public StoryImage(DataSnapshot src)
    {
        tegs = new ArrayList<Integer>();
        //date = new Date(src.child("date"));
        date = src.child("date").getValue(Date.class);
     //   date = new Date_str(src.child("date").getValue(String.class));
        pathInStorage = src.child("pathInStorage").getValue(String.class);
        for (DataSnapshot tegSnapshot : src.child("tegs").getChildren())
        {
            tegs.add(Integer.parseInt(tegSnapshot.getValue(String.class)));
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }

   /* public void setDate(Date_str date) {
       this.date = date;
   }*/

   /* public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }*/

    public void setPathInStorage(String pathInStorage) {
        this.pathInStorage = pathInStorage;
    }

    public void setTegs(ArrayList<Integer> tegs) {
        this.tegs = tegs;
    }

    public Date getDate() {
        return date;
    }

   /* public Date_str getDate() {
        return date;
    }*/

    /*public String getDoctorId() {
        return doctorId;
    }*/

    public String getPathInStorage() {
        return pathInStorage;
    }

    public ArrayList<Integer> getTegs() {
        return tegs;
    }

    public void addTeg(int teg)
    {
        tegs.add(teg);
    }

}
