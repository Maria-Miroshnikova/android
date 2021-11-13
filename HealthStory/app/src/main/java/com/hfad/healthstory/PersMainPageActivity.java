package com.hfad.healthstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.healthstory.firebase_classes.UserAccount;
import com.hfad.healthstory.models.User;

public class PersMainPageActivity extends AppCompatActivity {

    public static final String EXTRA_USERID = "userId";
    public static final String EXTRA_USERTYPEPATH = "userTypePath";

    FirebaseDatabase database;
    DatabaseReference accounts;

    String userID;
    String userTypePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers_main_page);

        init();

        Query userWithID = accounts.orderByKey().equalTo(userID);
        userWithID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                {
                    Log.d("MyBug", "DatabaseError: PersMainPageActivity, userWithId FSlistener");
                }
                else {
                    for (DataSnapshot userSnapshot : snapshot.getChildren())
                        setName(userSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("MyBug", "DatabaseError: PersMainPageActivity, userWithId FSlistener");
            }
        });
    }

    private void init()
    {
        database = FirebaseDatabase.getInstance(getString(R.string.db_address));
        Intent intent = getIntent();
        userID = intent.getStringExtra(EXTRA_USERID);
        userTypePath = intent.getStringExtra(EXTRA_USERTYPEPATH);
        accounts = database.getReference(userTypePath);
    }

    private void setName(DataSnapshot userSnapshot)
    {
        UserAccount userAccount = userSnapshot.getValue(UserAccount.class);
        String name = "";

        String patronymic = userAccount.getPatronymic();
        if (patronymic.length() != 0)
            patronymic = patronymic.substring(0, 1) + ". ";

        if (userTypePath.equals(getString(R.string.patients_table)))
        {
            name += userAccount.getFirstName() + " "
                    + patronymic + userAccount.getSecondName();
        }
        else
        {
            name += "Dr. " + userAccount.getFirstName().charAt(0) + ". "
                    + patronymic + userAccount.getSecondName();
        }

        TextView nameView = findViewById(R.id.account_name_textview);
        nameView.setText(name);
    }

    public void onDataButtonClicked(View view)
    {
        Intent intent = new Intent(this, DataPageActivity.class);
        intent.putExtra(DataPageActivity.EXTRA_USERID, userID);
        intent.putExtra(DataPageActivity.EXTRA_USER_PATH, userTypePath);
        startActivity(intent);
    }

    public void onOptionsButtonClicked(View view)
    {
        Intent intent = new Intent(this, OptionsPageActivity.class);
        intent.putExtra(OptionsPageActivity.EXTRA_USERID, (String) userID);
        intent.putExtra(OptionsPageActivity.EXTRA_USERTYPEPATH, (String) userTypePath);
        startActivity(intent);
    }
}