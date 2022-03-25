package com.hfad.healthstory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.healthstory.firebase_classes.User;

public class AuthActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        init();
    }

    private void init() {
        database = FirebaseDatabase.getInstance(getString(R.string.db_address));
        users = database.getReference(getString(R.string.users_table));
       // initUsers();
    }

  /*  private void initUsers()
    {
        users.push().child("password").setValue("123");
        //users.push().child("password").setValue("2222");
    }*/

    public void onSignInClicked(View view) {
        EditText authView = (EditText) findViewById(R.id.auth_edittext);
        String password = authView.getText().toString();

        Query userWithPassword = users.orderByChild("password")
                .equalTo(password);
        userWithPassword.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                    dialogNoUser();
                else {
                    for (DataSnapshot userSnapshot : snapshot.getChildren())
                        intentToPersMainPageActivity(userSnapshot.getKey(), userSnapshot.getValue(User.class).getPathToUserData());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("MyBug", "DatabaseError: AuthActivity, userWithPassword FSlistener");
            }
        });
    }

    private void dialogNoUser()     /// можно будет строительно диалогов в отдельный клас засунуть
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_auth_no_user_title);
        builder.setMessage(R.string.dialog_auth_no_user_message);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Отпускает диалоговое окно
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void intentToPersMainPageActivity(String userID, String userTypePath) {
        Intent intent = new Intent(this, PersMainPageActivity.class);
        intent.putExtra(PersMainPageActivity.EXTRA_USERID, (String) userID);
        intent.putExtra(PersMainPageActivity.EXTRA_USERTYPEPATH, (String) userTypePath);
        startActivity(intent);
    }
}