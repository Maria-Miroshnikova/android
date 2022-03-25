package com.hfad.healthstory;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.widget.Toolbar;

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
import com.hfad.healthstory.firebase_classes.Date;
import com.hfad.healthstory.firebase_classes.StoryImage;

import other.DialogueBuilder;
import other.NewUserSingltone;
import other.StoryImageAdapter;
import other.StorySingltone;

public class StoryActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_BY_DATE_TYPE = "orderByDateType";
    public static final String EXTRA_TEG_STRING = "tegString";
    public static final String EXTRA_USER_ID = "userID";
    public static final String EXTRA_USER_PATH = "userPath";

    private Boolean orderByMostNewPagesFilter;
    private List<Integer> requiredTegsArray;
    private String userID;
    private String userPath;
    private DatabaseReference stories;

    private Activity activity = this;
    private NewUserSingltone newUserSingltone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        getStoryResults(); /// default
    }

    private void init()
    {
        userID = getIntent().getExtras().getString(EXTRA_USER_ID);

        userPath = getIntent().getExtras().getString(EXTRA_USER_PATH);
        if (userPath.equals(getString(R.string.patients_table)))
        {
            Button addButton = findViewById(R.id.add_to_story_button);
            addButton.setVisibility(View.INVISIBLE);
        }
        //userID = "-MoN694rF2S-2FK9AAml";

        toDefaultFilter();
        stories = FirebaseDatabase.getInstance(getString(R.string.db_address)).getReference(getString(R.string.stories_table));
    }

    private void toDefaultFilter()
    {
        orderByMostNewPagesFilter = true;
        /// Default full explore
        requiredTegsArray = new ArrayList<Integer>();
        for (int i = 1; i <= 5; ++i)
            requiredTegsArray.add(i);
    }

    private void collectFilters(Intent data)
    {
        requiredTegsArray.clear();
        String tegs = data.getStringExtra(EXTRA_TEG_STRING);
        for (int i = 0; i < tegs.length(); ++i)
        {
            requiredTegsArray.add(tegs.charAt(i) - '0');
        }

        if ((int)data.getExtras().get(EXTRA_ORDER_BY_DATE_TYPE) == FilterActivity.ORDER_BY_DATE_OLD)
            orderByMostNewPagesFilter = false;
        else
            orderByMostNewPagesFilter = true;
    }

    public void onFiltersButtonClicked(View view)
    {
        Intent intent = new Intent(this, FilterActivity.class);
        startFetchFilters.launch(intent);
    }

    ActivityResultLauncher<Intent> startFetchFilters =
            registerForActivityResult(  new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                collectFilters(result.getData());
                                getStoryResults();
                            }
                            else {
                            //    default? make default filter?
                            }
                        }
                    });

    private void getStoryResults()
    {
        Query dateFilteredImages = stories.child(userID).orderByChild("date/year");
        ArrayList<StoryImage> filteredImages = new ArrayList<StoryImage>();

        dateFilteredImages.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot storySnapshot) {
                if (storySnapshot.getValue() == null)
                {
                    // empty story;
                    //emptyResult();
                }
                else {

                    /*ArrayList<StoryImage> query = new ArrayList<StoryImage>();
                    for (DataSnapshot imageSnapshot : storySnapshot.getChildren())
                    {
                        query.add(new StoryImage(imageSnapshot));
                    }*/

                    collectImagesByFilter(storySnapshot, filteredImages);
                    showResults(filteredImages);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("MyBug", "DatabaseError: StoryActivity, dateFilteredImages FSlistener");
            }
        });

    }

    private void collectImagesByFilter(DataSnapshot storySnapshot, ArrayList<StoryImage> filteredImages)
    {
        /// Through whole story
        for (DataSnapshot imageSnapshot : storySnapshot.getChildren())
        {
            /// Through all tegs of particular image
            for (DataSnapshot tegSnapshot : imageSnapshot.child(getString(R.string.stories_path_to_tegs_child)).getChildren())
            {
                if (filterIsRequired(Integer.parseInt(tegSnapshot.getValue(String.class))))
                {
                    StoryImage storyImage = new StoryImage(imageSnapshot);

                    /// Add image to filtered array
                    int position = findPositionInSortedByDateArray(filteredImages, storyImage.getDate());
                    if (position == filteredImages.size())
                        filteredImages.add(storyImage);
                    else
                        filteredImages.add(position, storyImage);
                    break;
                }
            }
        }
    }

    private Boolean filterIsRequired(int teg)
    {
        return requiredTegsArray.contains(teg);
    }

    private int findPositionInSortedByDateArray(ArrayList<StoryImage> array, Date date)
    {
        int i;

        if (orderByMostNewPagesFilter)
        {
            for (i = 0; i < array.size(); ++i)
            {
                if ((date.isLater(array.get(i).getDate())))
                    return i;
            }
        }
        else
        {
            for (i = 0; i < array.size(); ++i)
            {
                if (!(date.isLater(array.get(i).getDate())))
                    return i;
            }
        }

        /*for (i = 0; i < array.size(); ++i)
        {
            if (!(date.isLater(array.get(i).getDate())) && !orderByMostNewPagesFilter)
                return i;
        }*/
        return i;
    }

    private void showResults(ArrayList<StoryImage> images)
    {
        StoryImageAdapter adapter = new StoryImageAdapter(this, images);
        ListView listView = (ListView)findViewById(R.id.story_images_listview);
        listView.setAdapter(adapter);

        // когда очищать??
        StorySingltone.getInstance().clear();
        StorySingltone.getInstance().setImagesArray(images);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentToShowStoryImage(position);
            }
        });

     /*   if (images.size() == 0) {
            emptyResult();
            return;
        }
        else
            notEmptyResult();*/
    }

  /*  private void emptyResult()
    {
        TextView textView = findViewById(R.id.empty_list_textview);
        textView.setVisibility(View.VISIBLE);
    }

    private void notEmptyResult()
    {
        TextView textView = findViewById(R.id.empty_list_textview);
        textView.setVisibility(View.INVISIBLE);
    }*/

    private void intentToShowStoryImage(int position)
    {
        Intent intent = new Intent(this, ShowStoryImageActivity.class);
        intent.putExtra(ShowStoryImageActivity.EXTRA_START_POSITION, position);
        startActivity(intent);
    }

    public void onAddButtonClicked(View view)
    {
        Intent intent = new Intent(this, CreateUserStoryActivity.class);
        newUserSingltone = NewUserSingltone.getInstance();
        startAddToStory.launch(intent);
    }

    ActivityResultLauncher<Intent> startAddToStory =
            registerForActivityResult(  new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                if (newUserSingltone.getStoryImage() != null) {
                                    StorageReference storageReference = uploadImageToStorage(newUserSingltone.getUri(), userID);
                                    newUserSingltone.getStoryImage().setPathInStorage(storageReference.toString());
                                    uploadImageToDB(newUserSingltone.getStoryImage(), userID);

                                    // or singletone will destruct himself?
                                    newUserSingltone.clear();
                                    DialogueBuilder.show(DialogueBuilder.IS_ADDED_TO_STORY, activity);
                                }
                                else {
                                    DialogueBuilder.show(DialogueBuilder.NOT_ADDED_TO_STORY, activity);
                                }
                            }
                            else {
                                DialogueBuilder.show(DialogueBuilder.NOT_ADDED_TO_STORY, activity);
                            }
                        }
                    });

    private void uploadImageToDB(StoryImage storyImage, String userId)
    {
        DatabaseReference stories = FirebaseDatabase.getInstance(getString(R.string.db_address)).getReference(getString(R.string.stories_table));
        String imageId = stories.child(userId).push().getKey();
        DatabaseReference image = stories.child(userId).child(imageId);
        image.child("pathInStorage").setValue(storyImage.getPathInStorage().toString());

        image.child("date").child("day").setValue(storyImage.getDate().getDay());
        image.child("date").child("month").setValue(storyImage.getDate().getMonth());
        image.child("date").child("year").setValue(storyImage.getDate().getYear());
      //  image.child("date").setValue(storyImage.getDate().getDate());

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
}