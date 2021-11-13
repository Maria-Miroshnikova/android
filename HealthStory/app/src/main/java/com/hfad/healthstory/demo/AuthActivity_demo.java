package com.hfad.healthstory.demo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.healthstory.R;
import com.hfad.healthstory.models.User;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.healthstory.models.User;
import com.hfad.healthstory.models.User_comp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AuthActivity_demo extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference users;
    String userId;

    private ArrayAdapter<String> adapter;
    private List<String> listData;

    private List<User> listUsers;

    private static final String dbAddress = "https://healthstoryapp-default-rtdb.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_demo);
        init();

    /*    users.child("user_1").child("password").setValue("123");
        users.child("user_2").child("password").setValue("333");
        users.child("user_3").child("password").setValue("124");*/

        //       tryWriteToFB();
        getAllUsers();
        //      tryAddVEL();
        //       tryGetCLassWithFSVEL();

//        tryGetQueryByChild();
        tryFilterQuery();
        tryFilter();
    }

    private void init()
    {
        db = FirebaseDatabase.getInstance(dbAddress);
        users = db.getReference("users");
        userId = "";
        listData = new ArrayList<>();
        listUsers = new ArrayList<User>();
    }


    public void onSignInClicked(View view){


        testDBGetting();
        TextView textView = (TextView) findViewById(R.id.wrong_pass);
        textView.setText("");

        EditText authView = (EditText)findViewById(R.id.auth_edittext);
        String password = authView.getText().toString();
      /*  findUserByPassword(password);

        if (userId != "")
        {
            Intent intent = new Intent(this, PersMainPageActivity.class);
            intent.putExtra(PersMainPageActivity.EXTRA_USERID, (String) userId);
            intent.putExtra(PersMainPageActivity.EXTRA_DB, (String) dbAddress);
            startActivity(intent);
        }
        else
        {
            textView.setText("No such user!");
        }*/
    }

    /**
     * doesnot work after CEL?? after few attempts begins to work again??
     * sometimes do not crush!!
     * MAYBE the reason of crush is that I tap to button very quick??
     * Seems that it is true!
     */
    private void getAllUsers()
    {
        users.addListenerForSingleValueEvent ( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listData.size() > 0)
                    listData.clear();
                //          usersCount = dataSnapshot.getChildrenCount();
                getDataFromSnapshot(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void getDataFromSnapshot(DataSnapshot dbSnapshot)
    {
        for (DataSnapshot userSnapshot : dbSnapshot.getChildren())
        {
            listData.add(userSnapshot.getKey());
        }
    }

    private void findUserByPassword(String value)
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if (userSnapshot.child("password").getValue() == value) {
                        userId = userSnapshot.getKey();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        };

        users.addValueEventListener(valueEventListener);
    }

    /* Literally do not work sometimes!!
     *   if it does not work, getAllUsers does not work too
     * */
    private void tryWriteToFB()
    {
        users.child("user_1").child("password").setValue("123");
        //   users.child("user_5").child("password").setValue("123");
        //   users.push().child("password").setValue("xxx");
    }

    private void tryGoByHandsThroughFB()
    {
        getAllUsersHandy();
    }

    /**
     * does not work
     */
    private void tryAddVEL()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Log.d("BLEAT", "snap of whole db: count is " + snapshot.getChildrenCount());
                    listData.add((userSnapshot.getKey()) + "VEL");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
        users.addValueEventListener(valueEventListener);
        /**
         * too short time between operations. Should I use some waiting process?
         */
        //users.removeEventListener(valueEventListener);
    }

    /**
     * works by itself, not when all listeners are added in single function
     * writes in log, everything is fine
     */
    private void tryAddFSVEL()
    {
        db.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Log.d("BLEAT", "snap of whole db FORSINGLE: count is " + snapshot.getChildrenCount());
                    listData.add((userSnapshot.getKey()) + "FSVEL");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    /**
     * works but not as it was expected
     */
    private void tryAddCEL()
    {

        users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Log.d("BLEAT", "snap of CHILDEVENT db: count is " + snapshot.getChildrenCount());
                    listData.add((userSnapshot.getKey()) + "CEL");
                }

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void getAllUsersHandy()
    {
        listData.add(users.getKey());
        listData.add(users.child("user_1").getKey());
        listData.add(users.child("user_2").getKey());
        listData.add(users.child("user_3").getKey());
    }

    private void tryGetCLassWithFSVEL()
    {
        db.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    listUsers.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


    private void tryGetChildrenByListener()
    {
        getAllUsers();
    }

    private void testDBGetting()
    {
//        getAllUsers();
        assert(listData.size() != 0);
    }

    private void initComplexUsers()
    {
        String userID = users.child("comp_users").push().getKey();
        users.child("comp_users").child(userID).child("name").setValue("user_1");
        users.child("comp_users").child(userID).child("age").child("date").setValue("20.10.10");

        userID = users.child("comp_users").push().getKey();
        users.child("comp_users").child(userID).child("name").setValue("user_2");
        users.child("comp_users").child(userID).child("age").child("date").setValue("10.10.20");


        userID = users.child("comp_users").push().getKey();
        users.child("comp_users").child(userID).child("name").setValue("user_3");
        users.child("comp_users").child(userID).child("age").child("date").setValue("10.10.10");

    }

    /**
     * this thing someway firstly writes byAge and after writes byName. Asynchronus??
     */
    private void tryGetQueryByChild()
    {
        // initComplexUsers();

        Query queryByName = users.child("comp_users").orderByChild("name");
        Query queryByAge = users.child("comp_users").orderByChild("age/date");

        queryByName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    listData.add(userSnapshot.getValue(User_comp.class).getName() + userSnapshot.getValue(User_comp.class).getAge().getDate() + " orderByName");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        queryByAge.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    listData.add(userSnapshot.getValue(User_comp.class).getName() + userSnapshot.getValue(User_comp.class).getAge().getDate() + " orderByAge");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    /**
     * works as expected
     */
    private void tryFilterQuery()
    {
        Query queryWithName = users.child("comp_users").orderByChild("name").
                equalTo("user_3");
        Query queryWithYear = users.child("comp_users").orderByChild("age/date").
                startAt("10.10.20");
        queryWithName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    listData.add(userSnapshot.getValue(User_comp.class).getName() + userSnapshot.getValue(User_comp.class).getAge().getDate() + " orderByName");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        queryWithYear.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    listData.add(userSnapshot.getValue(User_comp.class).getName() + userSnapshot.getValue(User_comp.class).getAge().getDate() + " orderByAge");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    /**
     * works
     */
    private void tryFilter()
    {
        //       Query query =  users.child("comp_users").equalTo("10.10.10"); // NOT WORK: equalTo requires orderBy!
        //       Query query =  users.child("comp_users").equalTo("user_2");
        Query query =  users.child("comp_users").orderByChild("name").
                equalTo("user_2");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    listData.add(userSnapshot.getValue(User_comp.class).getName() + userSnapshot.getValue(User_comp.class).getAge().getDate() + " tryFilter");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}

/**
 * the most strange case: tryAddCEL + tryAddFSVEL:
 * - + - - + + - - - - - + - +
 *
 * ABOUT TOO FAST TAPPING TO BUTTON:
 * if I wate a bit, there are lines in Logcat about getting lines from FB
 * if I dont, there is no lines about that!
 *
 * is this about debugging?
 * should I wait?
 * what to expect from FB if it is REALTIME DB and it MUST work IMMEDIATELY?
 *
 * ATTENTION!!!!!!!!!!!!!!!!!!!!!!!!
 * there MUST be internet-connection on phone when I try to add something!!
 *
 * ASYNC makes: when write to one listData from different listeners, they do it in different order and mix!!!
 */