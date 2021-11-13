package other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hfad.healthstory.R;
import com.hfad.healthstory.firebase_classes.StoryImage;

import java.util.ArrayList;

public class StoryImageAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<StoryImage> objects;
    //ArrayList<URI>

    public StoryImageAdapter(Context ctx, ArrayList<StoryImage> objects) {
        this.ctx = ctx;
        this.objects = objects;
        lInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /// 1. Preparing view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_listview, parent, false);
        }

        /// 2. Getting StoryImage from array
        StoryImage storyImage = getStoryImage(position);

        /// 3. Setting preview
        ImageView preview = view.findViewById(R.id.item_image_preview);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference= storage.getReferenceFromUrl(storyImage.getPathInStorage());

        GlideApp.with(ctx).load(storageReference).fitCenter().into(preview);

        /// 4. Setting date
        TextView date = view.findViewById(R.id.item_image_date_textview);
        date.setText(storyImage.getDate().toString());

        /// 5. Setting tegs
        TextView tegs = view.findViewById(R.id.item_tegs_textview);
        String tegsString = new String();
        ArrayList<Integer> tegsArray = storyImage.getTegs();
        for (int i = 0; i < tegsArray.size(); ++i) {
            tegsString += getTegString(tegsArray.get(i));
            if (i != tegsArray.size() - 1)
                tegsString += ", ";
        }
        tegs.setText(tegsString);

        return view;
    }

    private StoryImage getStoryImage(int position)
    {
        return (StoryImage) getItem(position);
    }

    private String getTegString(int tegID)
    {
        switch (tegID)
        {
            case StoryImage.TYPE_SNIMOK:
                return ctx.getString(R.string.teg_snimok);
            case StoryImage.TYPE_SPRAVKA:
                return ctx.getString(R.string.teg_spravka);
            case StoryImage.TYPE_ANALIZ:
                return ctx.getString(R.string.teg_analiz);
            case StoryImage.TYPE_VISIT:
                return ctx.getString(R.string.teg_visit);
            case StoryImage.TYPE_OTHER:
                return ctx.getString(R.string.teg_other);
            default:
                return null;
        }
    }
}
