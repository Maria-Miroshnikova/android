package com.hfad.healthstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.healthstory.firebase_classes.Date;
import com.hfad.healthstory.firebase_classes.Date_str;
import com.hfad.healthstory.firebase_classes.StoryImage;

import other.NewUserSingltone;

public class CreateUserStoryActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PICK_IMAGE = 1;
    private Uri uriImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_story);
    }

    public void onChooseFileButtonClicked(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_IMAGE);
    }

    /// asinc?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if(resultCode == RESULT_OK){
                    uriImage = data.getData();
                    ImageView imageView = (ImageView)findViewById(R.id.preview);
                    imageView.setImageURI(uriImage);
                }
                break;
        }
    }

    /// URI guardian - ASINC?
    public void onUploadButtonClicked(View view)
    {
        StoryImage storyImage = new StoryImage();

        if (!collectInfo(storyImage))
            return;
        if (uriImage == null)
        {
            incorrectInputMessage(getString(R.string.not_attached_file));
            return;
        }

        NewUserSingltone.getInstance().setStoryImage(storyImage, uriImage);
        setResult(RESULT_OK);
        finish();
    }

    /// TO DO: guardian for correct input
    private Boolean collectInfo(StoryImage storyImage)
    {
        /// 1. Getting date
        EditText dateEditor = (EditText)findViewById(R.id.date_edittext);
        String dateString = dateEditor.getText().toString();
        if (dateString.length() <= 0) {
            incorrectInputMessage(getString(R.string.not_defined_date));
            return false;
        }
        if (!checkDateFormat(dateString)) {
            incorrectInputMessage(getString(R.string.incorrect_date));
            return false;
        }
        Date date = new Date();
        date.setDay(dateString.substring(0,2));
        date.setMonth(dateString.substring(3,5));
        date.setYear(dateString.substring(6,10));
        storyImage.setDate(date);
//        storyImage.setDate(new Date_str(dateString));

        /// 2. Getting tegs for image
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkbox_snimok);
        if (checkBox.isChecked())
            storyImage.addTeg(storyImage.TYPE_SNIMOK);

        checkBox = (CheckBox)findViewById(R.id.checkbox_spravka);
        if (checkBox.isChecked())
            storyImage.addTeg(storyImage.TYPE_SPRAVKA);

        checkBox = (CheckBox)findViewById(R.id.checkbox_analiz);
        if (checkBox.isChecked())
            storyImage.addTeg(storyImage.TYPE_ANALIZ);

        checkBox = (CheckBox)findViewById(R.id.checkbox_visit);
        if (checkBox.isChecked())
            storyImage.addTeg(storyImage.TYPE_VISIT);

        checkBox = (CheckBox)findViewById(R.id.checkbox_other);
        if (checkBox.isChecked())
            storyImage.addTeg(storyImage.TYPE_OTHER);

        if (storyImage.getTegs().size() == 0) {
            incorrectInputMessage(getString(R.string.not_defined_tegs));
            return  false;
        }
        return true;
    }

    private void incorrectInputMessage(String message)
    {
        TextView textView = (TextView)findViewById(R.id.incorrect_input_texview_create_story);
        textView.setText(message);
    }

    private Boolean checkDateFormat(String date)
    {
        if (date.length() != 10)
            return false;
        if ((date.charAt(2) != '.') || (date.charAt(5) != '.'))
            return false;
        for (int i = 0; i < date.length(); ++i)
        {
            if ((i != 2) && (i != 5))
                if (!(((date.charAt(i) >= '1') && (date.charAt(i) <= '9')) || (date.charAt(i) == '0')))
                    return false;
        }
        return true;
    }
}