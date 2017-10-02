package com.myntra.flickrgame.api;

import com.myntra.flickrgame.util.AppConstant;
import com.myntra.flickrgame.base.ApiRequest;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link FlickrRequest}
 */

public class FlickrRequest extends ApiRequest {

    private String format;
    private int callback = 1;

    public FlickrRequest(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public int getCallback() {
        return callback;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String getBaseUrl() {
        return AppConstant.BASE_URL;
    }
}
