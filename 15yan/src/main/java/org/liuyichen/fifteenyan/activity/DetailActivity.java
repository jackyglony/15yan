package org.liuyichen.fifteenyan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;


import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.fragment.DetailFragment;
import org.liuyichen.fifteenyan.model.Story;



/**
 * Created by root on 15-3-6.
 */
public class DetailActivity extends BaseActivty {

    private static final String EXTRA_DATA = "DetailActivity:EXTRA_DATA";
    public static void launch(BaseActivty activity, Parcelable data) {

        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_DATA, data);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.swipeback_stack_right_in,
                R.anim.swipeback_stack_to_back);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(getLayoutId())
                .setSwipeBackView(R.layout.swipeback_default);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, DetailFragment.create((Story)getIntent().getParcelableExtra(EXTRA_DATA)))
                    .commit();
        }
        gestureDetector = new GestureDetector(this, simpleOnGestureListener);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }

    private static final int FLING_MIN_DISTANCE_X = 200;
    private static final int FLING_MIN_DISTANCE = 10;
    private static final int FLING_MIN_VELOCITY = 1;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        try {
            gestureDetector.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(ev);
    }

    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean isOK = Math.abs(e2.getX() - e1.getX()) < FLING_MIN_DISTANCE_X ? true : false;
            if (getSupportActionBar() == null) return false;
            if (isOK && e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                getSupportActionBar().hide();
            } else if (isOK && e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                getSupportActionBar().show();
            }
            return false;
        }
    };
}
