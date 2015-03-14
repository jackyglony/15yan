package org.liuyichen.fifteenyan.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by root on 15-3-12.
 */
public class Toast  {

    private static android.widget.Toast single = null;

    public static final int LENGTH_SHORT = 0;

    public static final int LENGTH_LONG = 1;

    public static android.widget.Toast makeText(Context context, CharSequence text, int duration) {

        single = android.widget.Toast.makeText(context.getApplicationContext(), text, duration);
        return single;
    }

    public static android.widget.Toast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public static void cancel() {

        if (single != null) {
            single.cancel();
        }
    }
}
