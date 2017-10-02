package com.myntra.flickrgame.api;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by SurvivoR on 7/5/2017.
 * {@link ImageData}
 */

public class ImageData {

    @SerializedName("title")
    private String title;
    @SerializedName("media")
    private Map<String, String> media;

    public String getTitle() {
        return title;
    }

    public String getImage(){
        return media.get("m");
    }
}
