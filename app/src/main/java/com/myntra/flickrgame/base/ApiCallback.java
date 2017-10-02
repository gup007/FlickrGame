package com.myntra.flickrgame.base;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link ApiCallback}
 */
public abstract class ApiCallback<T extends ApiResponse> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onComplete();
        int status = response != null ? response.code() : ApiException.Kind.UNEXPECTED.ordinal();

        if (response == null) {
            onFailure(new ApiException(ApiException.Kind.INVALID_RESPONSE, "response == null"));
            return;
        }
        //check for error body
        if (!response.isSuccessful()) {
            onFailure(getAPIException(response, null));
            return;
        }

        T apiResponse = response.body();
        if (apiResponse == null) {
            onFailure(new ApiException(ApiException.Kind.NULL, status, "IResponse can't be null"));
            return;
        }
        if (!apiResponse.isValid()) {
            ApiException validationError = new ApiException(ApiException.Kind.INVALID_RESPONSE
                    , status, "Validation error for the given scenario");
            onFailure(validationError);
            return;
        }
        onSuccess(call, apiResponse);
    }

    @Override
    public void onFailure(Call<T> call, Throwable cause) {
        onComplete();
        ApiException apiException = getAPIException(null, cause);
        onFailure(apiException);
    }

    private ApiException getAPIException(Response<T> response, Throwable cause) {
        if (cause != null) {
            if (cause instanceof SocketTimeoutException) {
                return new ApiException(ApiException.Kind.SOCKET_TIMEOUT, "Internet Connection is slow, Try Again.", cause);
            } else if (cause instanceof JSONException) {
                return new ApiException(ApiException.Kind.CONVERSION, "Conversion Error", cause);
            } else if (cause instanceof IOException) {
                return new ApiException(ApiException.Kind.NETWORK, "Network Error", cause);
            } else if (cause instanceof ApiException) {
                return (ApiException) cause;
            } else {
                return new ApiException(ApiException.Kind.UNEXPECTED, "Unknown", cause);
            }
        }
        return new ApiException(ApiException.Kind.UNEXPECTED, "Unknown Error");
    }

    public abstract void onSuccess(Call<T> call, T response);

    public abstract void onComplete();

    public abstract void onFailure(ApiException e);
}
