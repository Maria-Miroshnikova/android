package com.learn.notebook_study_project.firebase_classes;

import java.util.ArrayList;

public class UserAccount {
    // как в базе будет представлен пустой список?
    private ArrayList<String> idNotesList;

    public UserAccount() { idNotesList = new ArrayList<>(); }
    public UserAccount(ArrayList<String> idNotestList) {
        this.idNotesList = new ArrayList<>(idNotestList);
    }

    public ArrayList<String> getIdNotesList() {
        return idNotesList;
    }

    public int size()
    {
        return idNotesList.size();
    }

    public void addNote(String noteId)
    {
        idNotesList.add(noteId);
    }
}
