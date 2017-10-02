package com.myntra.flickrgame.api;

import android.content.Context;

import com.myntra.flickrgame.base.ApiService;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link FlickrService}
 */

public class FlickrService extends ApiService<FlickrService.FlickrApi, FlickrRequest, FlickrResponse> {

    public FlickrService(Context context) {
        super(context);
    }

    @Override
    protected Class<FlickrApi> getAPI() {
        return FlickrApi.class;
    }

    @Override
    protected Call<FlickrResponse> onExecute(FlickrApi api, FlickrRequest request) {
        return api.getPhotos(request.getFormat(), request.getCallback());
    }


    interface FlickrApi {
        @GET("photos_public.gne")
        Call<FlickrResponse> getPhotos(@Query("format") String format,
                                       @Query("nojsoncallback") int callback);
    }
}
