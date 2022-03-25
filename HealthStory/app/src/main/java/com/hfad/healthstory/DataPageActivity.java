package com.hfad.healthstory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.healthstory.firebase_classes.Doctor;
import com.hfad.healthstory.firebase_classes.Patient;
import com.hfad.healthstory.firebase_classes.StoryImage;
import com.hfad.healthstory.firebase_classes.User;
import com.hfad.healthstory.firebase_classes.UserAccount;

public class DataPageActivity extends AppCompatActivity {

    public static final String EXTRA_USERID = "userID";
    public static final String EXTRA_USER_PATH = "userPath";

    String userPath;
    String userID;

    DatabaseReference accounts;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_page);

        init();

        Query userWithID = accounts.orderByKey().equalTo(userID);
        userWithID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                {
                    Log.d("MyBug", "DatabaseError: DataPageActivity, userWithId FSlistener");
                }
                else {
                    for (DataSnapshot userSnapshot : snapshot.getChildren())
                        collectInfro(userSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("MyBug", "DatabaseError: DataPageActivity, userWithId FSlistener");
            }
        });
    }

    private void init()
    {
        userID = getIntent().getExtras().getString(EXTRA_USERID);
        userPath = getIntent().getExtras().getString(EXTRA_USER_PATH);
        database = FirebaseDatabase.getInstance(getString(R.string.db_address));
        accounts = database.getReference(userPath);
    }

    private void collectInfro(DataSnapshot userSnapshot)
    {
        UserAccount userAccount = userSnapshot.getValue(UserAccount.class);

     /*   if (userPath.equals(getString(R.string.patients_table))) {
            userAccount = userSnapshot.getValue(Patient.class);
        }
        else {
            userAccount = userSnapshot.getValue(Doctor.class);
        }*/

        TextView firstName = findViewById(R.id.user_first_name);
        firstName.setText(userAccount.getFirstName());//userSnapshot.child("firstName").getValue(String.class));

        TextView secondName = findViewById(R.id.user_second_name);
        secondName.setText(userAccount.getSecondName());//userSnapshot.child("secondName").getValue(String.class));

        TextView patronymic = findViewById(R.id.user_patronymic);
        patronymic.setText(userAccount.getPatronymic());//userSnapshot.child("patronymic").getValue(String.class));

        TextView birthday = findViewById(R.id.user_birthday);
        birthday.setText(userAccount.getBirthday().toString());//userSnapshot.child("firstName").getValue(String.class));

        TextView gender = findViewById(R.id.user_gender);
        gender.setText(getGenderString(userAccount.getGender()));
    }

    private String getGenderString(int gender)
    {
        switch (gender)
        {
            case UserAccount.GENDER_MALE:
                return getString(R.string.gendertype_male);
            case UserAccount.GENDER_FEMALE:
                return getString(R.string.gendertype_female);
            case UserAccount.GENDER_OTHER:
                return getString(R.string.gendertype_other);
            default:
                return null;
        }
    }
}

