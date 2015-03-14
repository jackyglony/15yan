package org.liuyichen.fifteenyan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.liuyichen.fifteenyan.FifteenApp;
import org.liuyichen.fifteenyan.FifteenConstant;

/**
 * Created by root on 15-3-13.
 */
public class Settings {

    public static boolean isOnlyWifiOpen() {

        SharedPreferences pf = FifteenApp.getSelf().getSharedPreferences(FifteenConstant.SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pf.getBoolean(FifteenConstant.SETTINGS_PREFERENCES_ONLY_WIFI, true);
    }

    public static void switchOnlyWifiMode(boolean is) {

        SharedPreferences pf = FifteenApp.getSelf().getSharedPreferences(FifteenConstant.SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pf.edit();
        ed.putBoolean(FifteenConstant.SETTINGS_PREFERENCES_ONLY_WIFI, is);
        ed.commit();
    }

    public static boolean canLoadImage() {

        SharedPreferences pf = FifteenApp.getSelf().getSharedPreferences(FifteenConstant.SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean isOpenOnlyWifi = pf.getBoolean(FifteenConstant.SETTINGS_PREFERENCES_ONLY_WIFI, true);
        if (isOpenOnlyWifi && !NetWorkHelper.isWifi(FifteenApp.getSelf()))  {
            return false;
        }

        return true;
    }
}
