package com.hfad.healthstory;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hfad.healthstory.firebase_classes.StoryImage;
import com.hfad.healthstory.firebase_classes.User;
import com.hfad.healthstory.firebase_classes.UserAccount;

import java.util.UUID;

import other.DialogueBuilder;
import other.NewUserSingltone;
import other.OptionsDoctorFragment;
import other.OptionsPatientFragment;

public class OptionsPageActivity extends AppCompatActivity {

    public static final String EXTRA_USERID = "userId";
    public static final String EXTRA_USERTYPEPATH = "userTypePath";
    private NewUserSingltone newUserSingltone;
    private Activity activity = this;

    String userID;
    String userPath;
    DatabaseReference users;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_page);

        init();
        fillFragment();
    }

    private void init()
    {
        userID = getIntent().getExtras().getString(EXTRA_USERID);
        userPath = getIntent().getExtras().getString(EXTRA_USERTYPEPATH);

        database = FirebaseDatabase.getInstance(getString(R.string.db_address));
        users = database.getReference(getString(R.string.users_table));
    }

    private void fillFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (userPath.equals(getString(R.string.patients_table)))
        {
            OptionsPatientFragment patientFragment = new OptionsPatientFragment();
            fragmentTransaction.add(R.id.user_options_fragmentview, patientFragment);
        }
        else
        {
            OptionsDoctorFragment doctorFragment = new OptionsDoctorFragment();
            fragmentTransaction.add(R.id.user_options_fragmentview, doctorFragment);
        }

        fragmentTransaction.commit();
    }

    public void onCreateUserOptionClicked(View view)
    {
        Intent intent = new Intent(this, CreateUserPasswordActivity.class);
        newUserSingltone = NewUserSingltone.getInstance();
        startCreateUser.launch(intent);
    }

    ActivityResultLauncher<Intent> startCreateUser =
            registerForActivityResult(  new ActivityResultContracts.StartActivityForResult(),
                                        new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (newUserSingltone.isComplete(getApplicationContext())) {
                            addUserToFB(newUserSingltone);
                            if (newUserSingltone.getUser().getPathToUserData().equals(getString(R.string.doctors_table)))
                                DialogueBuilder.show(DialogueBuilder.IS_CREATED_NEW_USER_doctor, activity);
                            else
                                DialogueBuilder.show(DialogueBuilder.IS_CREATED_NEW_USER_patient, activity);

                            // or singletone will destruct himself?
                            newUserSingltone.clear();
                        }
                        else {
                            DialogueBuilder.show(DialogueBuilder.NOT_CREATED_USER, activity);
                        }
                    }
                    else {
                        DialogueBuilder.show(DialogueBuilder.NOT_CREATED_USER, activity);
                    }
                }
            });

    private void addUserToFB(NewUserSingltone newUserSingltone)
    {
        String userId = addUserToDB(newUserSingltone.getUser());
        String userPath = newUserSingltone.getUser().getPathToUserData();
        addDataToDB(newUserSingltone.getUserAccount(), userPath,  userId);
        if (userPath.equals(getString(R.string.patients_table)))
        {
            StorageReference storageReference = uploadImageToStorage(newUserSingltone.getUri(), userId);
            newUserSingltone.getStoryImage().setPathInStorage(storageReference.toString());
            uploadImageToDB(newUserSingltone.getStoryImage(), userId);
        }
    }


    private String addUserToDB(User user)
    {
        DatabaseReference users = FirebaseDatabase.getInstance(getString(R.string.db_address)).getReference(getString(R.string.users_table));
        String userId = users.push().getKey();
        users.child(userId).child("password").setValue(user.getPassword());
        users.child(userId).child("pathToUserData").setValue(user.getPathToUserData());
        return userId;
    }

    private void addDataToDB(UserAccount userAccount, String userPath, String userId)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance(getString(R.string.db_address)).getReference(userPath);
        userRef.child(userId).child("firstName").setValue(userAccount.getFirstName());
        userRef.child(userId).child("secondName").setValue(userAccount.getSecondName());
        userRef.child(userId).child("patronymic").setValue(userAccount.getPatronymic());

        userRef.child(userId).child("birthday").child("day").setValue(userAccount.getBirthday().getDay());
        userRef.child(userId).child("birthday").child("month").setValue(userAccount.getBirthday().getMonth());
        userRef.child(userId).child("birthday").child("year").setValue(userAccount.getBirthday().getYear());
        //userRef.child(userId).child("birthday").setValue(userAccount.getBirthday().getDate());

        userRef.child(userId).child("gender").setValue(userAccount.getGender());
    }

    private void uploadImageToDB(StoryImage storyImage, String userId)
    {
        DatabaseReference stories = FirebaseDatabase.getInstance(getString(R.string.db_address)).getReference(getString(R.string.stories_table));
        String imageId = stories.child(userId).push().getKey();
        DatabaseReference image = stories.child(userId).child(imageId);
        image.child("pathInStorage").setValue(storyImage.getPathInStorage().toString());

        image.child("date").child("day").setValue(storyImage.getDate().getDay());
        image.child("date").child("month").setValue(storyImage.getDate().getMonth());
        image.child("date").child("year").setValue(storyImage.getDate().getYear());
       // image.child("date").setValue(storyImage.getDate().getDate());

        for(int teg : storyImage.getTegs())
        {
            image.child("tegs").push().setValue(Integer.toString(teg));
        }
    }

    private StorageReference uploadImageToStorage(Uri imageUri, String userId)
    {
        /// 1. Form path to image in storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(getString(R.string.storage_stories_dir));
        String imageId= UUID.randomUUID().toString() + ".jpg"; /// if not jpg?
       // StorageReference pathToUploadRef = storageReference.child(userId + "/" + imageUri.getLastPathSegment());
        StorageReference pathToUploadRef = storageReference.child(userId + "/" + imageId);

        UploadTask uploadTask = pathToUploadRef.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle unsuccessful uploads
                Log.d("myDebug", "Upload to storage is failed!");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        return  pathToUploadRef;
    }

    public void onStoryButtonClicked(View view)
    {
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra(StoryActivity.EXTRA_USER_ID, userID);
        intent.putExtra(StoryActivity.EXTRA_USER_PATH, userPath);
        startActivity(intent);
    }

    public void onShowStoryButtonClicked(View view)
    {
        EditText editor = findViewById(R.id.show_user_story_edittext);
        String password = editor.getText().toString();

        Query userWithPassword = users.orderByChild("password")
                .equalTo(password);
        userWithPassword.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                    DialogueBuilder.show(DialogueBuilder.NO_USER_WITH_PASSWORD, activity);
                else {
                    for (DataSnapshot userSnapshot : snapshot.getChildren())
                        if (userSnapshot.child("pathToUserData").getValue(String.class).equals(getString(R.string.doctors_table)))
                            DialogueBuilder.show(DialogueBuilder.REJECTED_DOCTOR_STORY, activity);
                        else
                            intentToStoryActivity(userSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("MyBug", "DatabaseError: OptionsPageActivity, userWithPassword FSlistener");
            }
        });
    }

    private void intentToStoryActivity(String patientId)
    {
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra(StoryActivity.EXTRA_USER_ID, patientId);
        intent.putExtra(StoryActivity.EXTRA_USER_PATH, userPath);
        startActivity(intent);
    }

}