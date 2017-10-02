package com.myntra.flickrgame.base;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link ApiException}
 */

public class ApiException extends Exception {

    public final Kind kind;
    public final int code;

    public ApiException(Kind kind, String msg) {
        this(kind, -1, msg);
    }

    public ApiException(Kind kind, String msg, Throwable cause) {
        this(kind, -1, msg, cause);
    }

    public ApiException(Kind kind, int code, String msg) {
        super(msg);
        this.kind = kind;
        this.code = code;
    }

    public ApiException(Kind kind, int code, String msg, @NonNull Throwable cause) {
        super(msg, cause);
        this.kind = kind;
        this.code = code;
    }

    /**
     * Identifies the event kind which triggered a {@link ApiException}.
     */
    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * An exception was thrown while (de)serializing a body.
         */
        CONVERSION,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,

        /**
         * call back is null
         */
        NULL,

        /**
         *
         */
        SOCKET_TIMEOUT,
        /**
         * Exception kind for invalid request
         */
        INVALID_REQUEST,

        /**
         * Exception kind for invalid response
         */
        INVALID_RESPONSE,

        /**
         * Exception for incompatible data cache version
         */
        CACHE_EXCEPTION,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

}
