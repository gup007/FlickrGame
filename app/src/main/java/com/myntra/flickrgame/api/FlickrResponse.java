package com.myntra.flickrgame.api;

import com.google.gson.annotations.SerializedName;
import com.myntra.flickrgame.base.ApiResponse;

import java.util.List;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link FlickrResponse}
 */

public class FlickrResponse extends ApiResponse {

    @SerializedName("items")
    private List<ImageData> images;

    public List<ImageData> getImages() {
        return images;
    }

    @Override
    public boolean isValid() {
        return !(images == null || images.isEmpty());
    }
}
