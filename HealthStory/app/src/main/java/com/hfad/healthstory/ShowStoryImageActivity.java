package com.hfad.healthstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hfad.healthstory.firebase_classes.StoryImage;

import java.util.ArrayList;

import other.GlideApp;
import other.StorySingltone;

public class ShowStoryImageActivity extends AppCompatActivity {

    public static final String EXTRA_START_POSITION = "startPosition";
    private int startPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_story_image);

        init();
        setContent();
    }

    private void init() {
        startPosition = (Integer) getIntent().getExtras().get(EXTRA_START_POSITION);
    }

    private void setContent()
    {
        /// 1. Getting image
        StoryImage storyImage = StorySingltone.getInstance().getImage(startPosition);

        ImageView imageView = findViewById(R.id.story_image_imageview);

        /// 2. Setting date
        TextView textView = findViewById(R.id.big_image_date_textview);
        textView.setText(storyImage.getDate().toString());

        /// 3. Setting image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference= storage.getReferenceFromUrl(storyImage.getPathInStorage());

        GlideApp.with(this).load(storageReference).fitCenter().into(imageView);

        /// 4. Setting tegs
        TextView tegs = findViewById(R.id.big_image_tegs_textview);
        String tegsString = new String();
        ArrayList<Integer> tegsArray = storyImage.getTegs();
        for (int i = 0; i < tegsArray.size(); ++i) {
            tegsString += getTegString(tegsArray.get(i));
            if (i != tegsArray.size() - 1)
                tegsString += ", ";
        }
        tegs.setText(tegsString);

      //  ZoomInImageViewAttacher mIvAttacter = new ZoomInImageViewAttacher();
      //  mIvAttacter.attachImageView(imageview);

        /// 5. Buttons visibility

        Button buttonNext = findViewById(R.id.button_next);
        Button buttonBack = findViewById(R.id.button_back);

        if (startPosition == (StorySingltone.getInstance().getCount() - 1))
            buttonNext.setVisibility(View.INVISIBLE);
        else
            buttonNext.setVisibility(View.VISIBLE);

        if (startPosition == 0)
            buttonBack.setVisibility(View.INVISIBLE);
        else
            buttonBack.setVisibility(View.VISIBLE);
    }

    public void onNextButtonClicked(View view)
    {
        /*Intent intent = new Intent(this, ShowStoryImageActivity.class);
        intent.putExtra(ShowStoryImageActivity.EXTRA_START_POSITION, startPosition + 1);
        startActivity(intent);*/
        startPosition += 1;
        setContent();
    }

    public void onBackButtonClicked(View view)
    {
        /*
        Intent intent = new Intent(this, ShowStoryImageActivity.class);
        intent.putExtra(ShowStoryImageActivity.EXTRA_START_POSITION, startPosition - 1);
        startActivity(intent);*/
        startPosition -= 1;
        setContent();
    }

    private String getTegString(int tegID)
    {
        switch (tegID)
        {
            case StoryImage.TYPE_SNIMOK:
                return getString(R.string.teg_snimok);
            case StoryImage.TYPE_SPRAVKA:
                return getString(R.string.teg_spravka);
            case StoryImage.TYPE_ANALIZ:
                return getString(R.string.teg_analiz);
            case StoryImage.TYPE_VISIT:
                return getString(R.string.teg_visit);
            case StoryImage.TYPE_OTHER:
                return getString(R.string.teg_other);
            default:
                return null;
        }
    }
}