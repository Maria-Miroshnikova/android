package com.learn.notebook_study_project;

import org.junit.Test;

import static org.junit.Assert.*;

import android.text.TextUtils;

import com.learn.notebook_study_project.firebase_classes.User;
import com.learn.notebook_study_project.models.FirebaseDbModel;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addUser()
    {
        String login_test = "000test";
        FirebaseDbModel db = new FirebaseDbModel();
        User user = db.addUser(login_test);
        assert (!TextUtils.isEmpty(user.getIdUserAccount()));

        /*DatabaseReference userAccounts =  App.getInstance().getDatabase().getUserAccounts();

        userAccounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userAccountSnapshot : snapshot.getChildren())
                {
                    if (userAccountSnapshot.getKey() == user.getIdUserAccount())
                    {
                        UserAccount userAccount = userAccountSnapshot.getValue(UserAccount.class);
                        // assert (empty list)
                    }
                }
                // not found
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //    verify(userAccounts).addListenerForSingleValueEvent(callbackCaptor.capture());
        //    ValueEventListener callback = callbackCaptor.getValue();*/

    }
}