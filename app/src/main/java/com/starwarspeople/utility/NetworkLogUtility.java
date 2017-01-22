package com.starwarspeople.utility;

import android.util.Log;

import retrofit2.Call;

/**
 * Created by Divya on 1/22/2017.
 */
public class NetworkLogUtility {

    public static void logFailure(Call call, Throwable throwable) {
        if (call != null) {
            if (call.isCanceled())
                Log.e(NetworkLogUtility.class.getSimpleName(), "Request was cancelled");
        }

        if (throwable != null) {
            Throwable cause = throwable.getCause();

            if (cause != null) {
                Log.e(NetworkLogUtility.class.getSimpleName(), String.format("logFailure() : cause.toString() : %s", cause.toString()));
            }
        }
    }
}