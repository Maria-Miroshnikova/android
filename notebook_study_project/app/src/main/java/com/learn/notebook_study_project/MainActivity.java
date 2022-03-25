package com.learn.notebook_study_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.learn.notebook_study_project.firebase_classes.User;
import com.learn.notebook_study_project.models.FirebaseDbModel;

public class MainActivity extends AppCompatActivity {

    public static NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        FirebaseDbModel db = new FirebaseDbModel();
 //      String key = db.getUsers().child("001").getKey();
 //      String key2 = db.getUsers().child("002").getKey();
//       String newkey = db.getUsers().push().getKey();

        db.getUsers().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    User user = userSnapshot.getValue(User.class);
                    if (user.getLogin() == "admin")
                    {
                        assert(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("bad db", "loadPost:onCancelled", error.toException());
            }
        });

        String login_test = "000test";
        User user = db.addUser(login_test);
        assert (!TextUtils.isEmpty(user.getIdUserAccount()));


        //  navController = navHostFragment.getNavController();
        //  App app = App.getInstance();
    }
}