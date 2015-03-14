package org.liuyichen.fifteenyan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.blur.Blur;

/**
 * Created by root on 15-3-5.
 */
public class BlurView extends View {


    private int bindImageId;
    public BlurView(Context context) {
        super(context);
        init();
    }

    public BlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.blurImage);
        bindImageId = a.getResourceId(R.styleable.blurImage_bind_image, NO_ID);
        a.recycle();
        init();
    }

    public BlurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.blurImage);
        bindImageId = a.getResourceId(R.styleable.blurImage_bind_image, NO_ID);
        a.recycle();
        init();
    }


    private void init() {

        if (bindImageId != NO_ID) {
            getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {

                        @Override
                        public boolean onPreDraw() {

                            ViewGroup parent = (ViewGroup)getParent();
                            View bg = parent.findViewById(bindImageId);
                            bg.setDrawingCacheEnabled(true);

                            final Bitmap bitmap = bg.getDrawingCache();
                            if (bitmap == null) return true;
                            blur(bitmap, BlurView.this);
                            getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }
                    });
        }
    }


    private static void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 4.0f;

        int w = (int)(view.getMeasuredWidth() / scaleFactor);
        int h = (int) (view.getMeasuredHeight() / scaleFactor);
        Bitmap overlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = Blur.apply(view.getContext(), overlay);

        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(new BitmapDrawable(view.getResources(), overlay));
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(view.getResources(), overlay));
        }
        Log.i("BlurImage", "Render time: " + (System.currentTimeMillis() - startMs + "ms"));
    }
}
