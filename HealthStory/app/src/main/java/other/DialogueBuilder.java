package other;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.hfad.healthstory.R;

public class DialogueBuilder {
    public static final int NO_USER_WITH_PASSWORD = 1;
    public static final int IS_CREATED_NEW_USER_patient = 2;
    public static final int IS_CREATED_NEW_USER_doctor = 3;
    public static final int NOT_CREATED_USER = 4;
    public static final int IS_ADDED_TO_STORY = 5;
    public static final int NOT_ADDED_TO_STORY = 6;
    public static final int REJECTED_DOCTOR_STORY = 7;


    public static void show(int id_dialog, Activity activity)
    {
        switch (id_dialog)
        {
            case NO_USER_WITH_PASSWORD: {
                    dialogOk(activity, activity.getString(R.string.dialog_auth_no_user_title),
                            activity.getString(R.string.dialog_auth_no_user_message));
                }
                break;
            case IS_CREATED_NEW_USER_patient: {
                dialogOk(activity, activity.getString(R.string.dialog_user_created_title),
                        activity.getString(R.string.dialog_patient_created_message));
            }
                break;
            case IS_CREATED_NEW_USER_doctor: {
                dialogOk(activity, activity.getString(R.string.dialog_user_created_title),
                        activity.getString(R.string.dialog_doctor_created_message));
            }
                break;
            case NOT_CREATED_USER: {
                dialogOk(activity, activity.getString(R.string.dialog_not_create_user_title),
                        activity.getString(R.string.dialog_not_create_user_message));
            }
                break;
            case IS_ADDED_TO_STORY: {
                dialogOk(activity, activity.getString(R.string.dialog_is_added_to_story_title),
                        activity.getString(R.string.dialog_is_added_to_story_message));
            }
                break;
            case NOT_ADDED_TO_STORY: {
                dialogOk(activity, activity.getString(R.string.dialog_not_added_to_story_title),
                        activity.getString(R.string.dialog_not_added_to_story_message));
            }
            case REJECTED_DOCTOR_STORY: {
                dialogOk(activity, activity.getString(R.string.dialog_reject_doctor_story_title),
                        activity.getString(R.string.dialog_reject_doctor_story_message));
            }
                break;
        }
    }

    private static void dialogOk(Activity activity, String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
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


}
