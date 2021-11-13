package com.hfad.healthstory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hfad.healthstory.firebase_classes.Date;
import com.hfad.healthstory.firebase_classes.Date_str;
import com.hfad.healthstory.firebase_classes.Doctor;
import com.hfad.healthstory.firebase_classes.Patient;
import com.hfad.healthstory.firebase_classes.UserAccount;

import java.util.ArrayList;

import other.NewUserSingltone;

public class CreateUserDataActivity extends AppCompatActivity {

    public static final String EXTRA_PATH = "path";
    String userPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_data);
        init();
    }

    private void init()
    {
        Intent intent = getIntent();
        userPath = intent.getStringExtra(EXTRA_PATH);
    }

    public void onCreateDataButtonClicked(View view)
    {
        UserAccount userAccount = null;
        if (userPath.equals(getString(R.string.doctors_table)))
            userAccount = new Doctor();
        else if (userPath.equals(getString(R.string.patients_table)))
            userAccount = new Patient();

        if (!collectInfo(userAccount))
            return;

        NewUserSingltone newUserSingltone = NewUserSingltone.getInstance();
        newUserSingltone.setUserAccount(userAccount);

        if (userPath.equals(getString(R.string.patients_table))) {
            Intent intent = new Intent(this, CreateUserStoryActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            finish();
        }
    }

    private Boolean collectInfo(UserAccount userAccount)
    {
        /// 1. Getting first name
        EditText first_nameView = (EditText)findViewById(R.id.first_name_edittext);
        String first_name = first_nameView.getText().toString();
        if (first_name.length() <= 0) {
            incorrectInputMessage(getString(R.string.not_defined_first_name));
            return false;
        }

        /// 2. Getting second name
        EditText second_nameView = (EditText)findViewById(R.id.second_name_edittext);
        String second_name = second_nameView.getText().toString();
        if (second_name.length() <= 0) {
            incorrectInputMessage(getString(R.string.not_defined_second_name));
            return false;
        }

        /// 3. Getting patronymic
        EditText patronymicView = (EditText)findViewById(R.id.patronymic_edittext);
        String patronymic = patronymicView.getText().toString();

        userAccount.setFirstName(first_name);
        userAccount.setSecondName(second_name);
        userAccount.setPatronymic(patronymic);

        /// 4. Getting birthday
        EditText birthdayView = (EditText)findViewById(R.id.dob_edittext);
        String birthday = birthdayView.getText().toString();
        if (birthday.length() <= 0) {
            incorrectInputMessage(getString(R.string.not_defined_dob));
            return false;
        }
        if (!checkDateFormat(birthday)) {
            incorrectInputMessage(getString(R.string.incorrect_dob));
            return false;
        }
        Date birthdayDate = new Date();
        birthdayDate.setDay(birthday.substring(0,2));
        birthdayDate.setMonth(birthday.substring(3,5));
        birthdayDate.setYear(birthday.substring(6,10));
        userAccount.setBirthday(birthdayDate);
//        userAccount.setBirthday(new Date_str(birthday));

        /// 5. Getting gender
        RadioGroup radioGroup = findViewById(R.id.gender_group);
        int id_button = radioGroup.getCheckedRadioButtonId();
        switch (id_button)
        {
            case R.id.gender_male_radio:
                userAccount.setGender(userAccount.GENDER_MALE);
                break;
            case R.id.gender_female_radio:
                userAccount.setGender(userAccount.GENDER_FEMALE);
                break;
            case R.id.gender_other_radio:
                userAccount.setGender(userAccount.GENDER_OTHER);
                break;
            default:
                incorrectInputMessage(getString(R.string.not_defined_gender));
                return false;
        }
        return true;
    }

    private void incorrectInputMessage(String message)
    {
        TextView textView = (TextView)findViewById(R.id.incorrect_input_texview_create_data);
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