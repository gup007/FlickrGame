package com.myntra.flickrgame.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.myntra.flickrgame.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ImageCache {

    private RequestCreator builder;
    private String imageUrl;
    private @DrawableRes int placeHolder = R.drawable.ic_insert_photo;
    private @DrawableRes int error = R.drawable.ic_error;
    private Picasso picasso;

    /**
     * ImageCache Constructor
     */
    private ImageCache() {
    }

    public RequestCreator getBuilder() {
        return builder;
    }

    public void setBuilder(RequestCreator builder) {
        this.builder = builder;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Picasso getPicasso() {
        return picasso;
    }

    public void setPicasso(Picasso picasso) {
        this.picasso = picasso;
    }

    /**
     * @param imageUrl to load image into ImageView
     * @return instance of image catching
     */
    public static ImageCache load(@NonNull Context context, @NonNull String imageUrl) {
        ImageCache imageCaching = new ImageCache();
        Picasso picasso = Picasso.with(context);
        imageCaching.setPicasso(picasso);
        imageCaching.setImageUrl(imageUrl);
        imageCaching.setBuilder(picasso.load(imageUrl));
//                .networkPolicy(NetworkPolicy.OFFLINE));
        return imageCaching;
    }

    /**
     * @param placeholderResId resourceId for place holder
     * @return instance of image catching
     */
    public ImageCache placeholder(@DrawableRes int placeholderResId) {
        setPlaceHolder(placeholderResId);
        if (getBuilder() != null) {
            getBuilder().placeholder(getPlaceHolder());
        }
        return this;
    }

    /**
     * @param placeholderResId resourceId for place holder
     * @return instance of image catching
     */
    public ImageCache error(@DrawableRes int placeholderResId) {
        setError(placeholderResId);
        if (getBuilder() != null) {
            getBuilder().error(getError());
        }
        return this;
    }

    public ImageCache fit(){
        if (getBuilder() != null) {
            getBuilder().fit();
        }
        return this;
    }

    /**
     * @param target is ImageView where load image from server
     * @return instance of image catching
     */
    public ImageCache into(final ImageView target) {
        if (getBuilder() != null) {
            getBuilder().into(target, new Callback() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    getPicasso().load(getImageUrl()).networkPolicy(NetworkPolicy.NO_STORE,
                            NetworkPolicy.NO_CACHE).placeholder(getPlaceHolder())
                            .error(getError()).into(target);
                }
            });
        }
        return this;
    }
} 