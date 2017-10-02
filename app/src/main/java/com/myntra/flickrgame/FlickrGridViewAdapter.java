package com.myntra.flickrgame;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.myntra.flickrgame.util.ImageCache;
import com.myntra.flickrgame.util.SquaredImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link FlickrGridViewAdapter}
 */

public class FlickrGridViewAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> urls = new ArrayList<>();

    public FlickrGridViewAdapter(Context context, ArrayList<String> imagesUrls) {
        this.context = context;
        Collections.shuffle(imagesUrls);
        urls.addAll(imagesUrls);
    }

    public void suffleImages(){
        Collections.shuffle(urls);
        notifyDataSetChanged();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        SquaredImageView view = (SquaredImageView) convertView;
        if (view == null) {
            view = new SquaredImageView(context);
            view.setScaleType(CENTER_CROP);
        }
        String url = getItem(position);
        view.loadImage(url);
        return view;
    }

    public String getRandomImageUrl(){
        Random random = new Random();
        int randomIndex = random.nextInt(urls.size());
        return urls.get(randomIndex);
    }

    @Override public int getCount() {
        return urls.size();
    }

    @Override public String getItem(int position) {
        return urls.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

}



