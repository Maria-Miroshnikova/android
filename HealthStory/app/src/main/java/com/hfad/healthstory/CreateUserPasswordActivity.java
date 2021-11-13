package com.hfad.healthstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.healthstory.firebase_classes.User;

import other.NewUserSingltone;

public class CreateUserPasswordActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_password);
        init();
    }

    private void init()
    {
        db = FirebaseDatabase.getInstance(getString(R.string.db_address));
        users = db.getReference(getString(R.string.users_table));
    }

    public void onCreateUserButtonClicked(View view)
    {
        User newUser = new User();
        if (!collectInfo(newUser))
            return;

        checkUniquePassword(newUser.getPassword(), new Callback() {
            @Override
            public void onCallback(Boolean result) {
                if (!result)
                    notUniquePassword();
                else
                    finishingActivity(newUser);
            }
        });
    }

    private void finishingActivity(User user)
    {
        NewUserSingltone newUserSingltone = NewUserSingltone.getInstance();
        newUserSingltone.setUser(user);

        Intent intent = new Intent(this, CreateUserDataActivity.class);
        intent.putExtra(CreateUserDataActivity.EXTRA_PATH, user.getPathToUserData());
        startActivity(intent);

        setResult(RESULT_OK);
        finish();
    }

    private Boolean collectInfo(User user)
    {
        /// 1. Getting password
        EditText passwordView = (EditText)findViewById(R.id.new_user_password_edittext);
        String password = passwordView.getText().toString();
        if (password.length() <= 0) {
            notDefinedPassword();
            return false;
        }
        user.setPassword(password);

        /// 2. Getting type of user
        RadioGroup radioGroup = findViewById(R.id.user_type_group);
        int id_button = radioGroup.getCheckedRadioButtonId();
        switch (id_button)
        {
            case R.id.patient_type_radio:
                user.setPathToUserData(getString(R.string.patients_table));
                break;
            case R.id.doctor_type_radio:
                user.setPathToUserData(getString(R.string.doctors_table));
                break;
            default:
                notDefinedUserType();
                return false;
        }
        return true;
    }

    private void notDefinedUserType()
    {
        TextView textView = (TextView)findViewById(R.id.incorrect_input_texview_create_password);
        textView.setText(getString(R.string.not_defined_user_type));
    }

    private void notDefinedPassword()
    {
        TextView textView = (TextView)findViewById(R.id.incorrect_input_texview_create_password);
        textView.setText(getString(R.string.not_defined_password));
    }

    private void notUniquePassword()
    {
        TextView textView = (TextView)findViewById(R.id.incorrect_input_texview_create_password);
        textView.setText(getString(R.string.not_unique_password));
    }

    public interface Callback {
        void onCallback(Boolean result);
    }

    private void checkUniquePassword(String password, final Callback callback)
    {
        Query userWithPassword = users.orderByChild("password")
                .equalTo(password);
        userWithPassword.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                    callback.onCallback(true);
                else
                    callback.onCallback(false);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("MyBug", "DatabaseError: CreateUserPasswordActivity, userWithPassword FSlistener");
            }
        });
    }

}