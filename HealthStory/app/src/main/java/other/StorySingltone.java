package other;

import com.hfad.healthstory.firebase_classes.StoryImage;

import java.util.ArrayList;

public class StorySingltone {

    private static StorySingltone INSTANCE;
    private ArrayList<StoryImage> imagesArray;

    private StorySingltone()
    {
        imagesArray = new ArrayList<StoryImage>();
    }

    public static StorySingltone getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StorySingltone();
        }
        return INSTANCE;
    }

    public int getCount() { return imagesArray.size(); }

    public StoryImage getImage(int position)
    {
        return imagesArray.get(position);
    }

    public void setImagesArray(ArrayList<StoryImage> imagesArray)
    {
        for (int i = 0; i < imagesArray.size(); ++i)
            this.imagesArray.add(i, imagesArray.get(i));
    }

    public void clear()
    {
        if (imagesArray != null)
            imagesArray.clear();
    }
}
