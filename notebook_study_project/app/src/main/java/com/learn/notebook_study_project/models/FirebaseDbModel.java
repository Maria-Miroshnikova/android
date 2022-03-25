package com.learn.notebook_study_project.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.learn.notebook_study_project.firebase_classes.Note;
import com.learn.notebook_study_project.firebase_classes.User;
import com.learn.notebook_study_project.firebase_classes.UserAccount;

public class FirebaseDbModel {

    private FirebaseDatabase database;
    private DatabaseReference root;
    private DatabaseReference users;
    private DatabaseReference userAccounts;
    private DatabaseReference notes;

    private String db_address = "https://notebook-study-project-default-rtdb.firebaseio.com/";
    private String users_address = "users";
    private String user_accounts_address = "user_accounts";
    private String notes_address = "notes";

    public FirebaseDbModel()
    {
        database = FirebaseDatabase.getInstance("https://notebook-study-project-default-rtdb.firebaseio.com/");
        root = database.getReference();
        root.child("testtt").setValue("lel");
        //users = root.child(users_address);
        users = database.getReference(users_address);
        userAccounts = database.getReference(user_accounts_address);
        notes = database.getReference(notes_address);
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getUsers() {
        return users;
    }

    public DatabaseReference getUserAccounts() {
        return userAccounts;
    }

    public DatabaseReference getNotes() {
        return notes;
    }

    public DatabaseReference getRoot() {
        return root;
    }

    public User addUser(String login)
    {
        String newUserId = users.push().getKey();
        String newUserAccountId = addUserAccount();
        User user = new User(login, newUserAccountId);
        users.child(newUserId).setValue(user);
        return user;
    }

    private String addUserAccount()
    {
        return userAccounts.push().getKey();
    }

    public void addNote(UserAccount userAccount, Note note)
    {
        String newNoteId = notes.push().getKey();
        notes.child(newNoteId).setValue(note);
        userAccount.addNote(newNoteId);
    }
}
