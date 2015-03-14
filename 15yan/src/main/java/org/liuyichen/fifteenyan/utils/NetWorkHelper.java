package org.liuyichen.fifteenyan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by root on 15-3-13.
 */
public class NetWorkHelper {

    public static boolean isWifi(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
