package com.starwarspeople.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Divya on 1/22/2017.
 */

public class NetworkUtility {

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isKnownException(Throwable t){
        return (t instanceof ConnectException
                || t instanceof UnknownHostException
                || t instanceof SocketTimeoutException
                || t instanceof IOException);
    }

}