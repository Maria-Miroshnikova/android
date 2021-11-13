package other;

import android.content.Context;
import android.net.Uri;

import com.hfad.healthstory.R;
import com.hfad.healthstory.firebase_classes.StoryImage;
import com.hfad.healthstory.firebase_classes.User;
import com.hfad.healthstory.firebase_classes.UserAccount;

public class NewUserSingltone {

    private static NewUserSingltone INSTANCE;

    private User user;
    private UserAccount userAccount;
    private StoryImage storyImage;
    private Uri uri;

    public void setUser(User user)
    {
        this.user = user;
    }
    public void setUserAccount(UserAccount userAccount)
    {
        this.userAccount = userAccount;
    }
    public void setStoryImage(StoryImage storyImage, Uri uri)
    {
        this.storyImage = storyImage;
        this.uri = uri;
    }
    public User getUser()
    {
        return user;
    }
    public UserAccount getUserAccount()
    {
        return userAccount;
    }
    public StoryImage getStoryImage() {
        return storyImage;
    }
    public Uri getUri()
    {
        return uri;
    }

    public void clear() {
        userAccount = null;
        user = null;
        storyImage = null;
        uri = null;
    }

    public Boolean isComplete(Context c){
        Boolean existUs = (user != null);
        Boolean existUsAc = (userAccount != null);
        String userPath = user.getPathToUserData();
        Boolean isDoctor = (userPath.equals(c.getString(R.string.doctors_table)));
        Boolean existImage = (storyImage != null);
        return existUs && existUsAc && (isDoctor || existImage);
    }

    private NewUserSingltone()
    {

    }

    public static NewUserSingltone getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewUserSingltone();
        }
        return INSTANCE;
    }
}