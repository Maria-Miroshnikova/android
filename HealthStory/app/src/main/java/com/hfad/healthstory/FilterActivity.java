package com.hfad.healthstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hfad.healthstory.firebase_classes.StoryImage;

import java.lang.reflect.Array;

public class FilterActivity extends AppCompatActivity {

    public static final int ORDER_BY_DATE_NEW = 1;
    public static final int ORDER_BY_DATE_OLD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void onOkButtonClicked(View view)
    {
        Intent data = new Intent();

        if (!collectInfo(data))
            return;

        setResult(RESULT_OK, data);
        finish();
    }

    private boolean collectInfo(Intent data)
    {
        /// 1. Getting order by date type
        RadioGroup radioGroup = findViewById(R.id.filter_date_radiogroup);
        int id_button = radioGroup.getCheckedRadioButtonId();
        switch (id_button)
        {
            case R.id.filter_date_up_radio:
                data.putExtra(StoryActivity.EXTRA_ORDER_BY_DATE_TYPE, ORDER_BY_DATE_NEW);
                break;
            case R.id.filter_date_down_radio:
                data.putExtra(StoryActivity.EXTRA_ORDER_BY_DATE_TYPE, ORDER_BY_DATE_OLD);
                break;
        }

        /// 2. Getting tegs
        String tegs = new String();
        CheckBox checkBox = (CheckBox)findViewById(R.id.filter_teg_snimok_checkbox);
        if (checkBox.isChecked())
            tegs += StoryImage.TYPE_SNIMOK;

        checkBox = (CheckBox)findViewById(R.id.filter_teg_spravka_checkbox);
        if (checkBox.isChecked())
            tegs += (StoryImage.TYPE_SPRAVKA);

        checkBox = (CheckBox)findViewById(R.id.filter_teg_analiz_checkbox);
        if (checkBox.isChecked())
            tegs += (StoryImage.TYPE_ANALIZ);

        checkBox = (CheckBox)findViewById(R.id.filter_teg_visit_checkbox);
        if (checkBox.isChecked())
            tegs += (StoryImage.TYPE_VISIT);

        checkBox = (CheckBox)findViewById(R.id.filter_teg_other_checkbox);
        if (checkBox.isChecked())
            tegs += (StoryImage.TYPE_OTHER);

        if (tegs.length() == 0) {
            incorrectInputMessage(getString(R.string.not_defined_tegs_filter));
            return  false;
        }

        data.putExtra(StoryActivity.EXTRA_TEG_STRING, tegs);
        return true;
    }

    private void incorrectInputMessage(String message)
    {
        TextView textView = (TextView)findViewById(R.id.incorrect_input_texview_filter);
        textView.setText(message);
    }
}