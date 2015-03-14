package org.liuyichen.fifteenyan.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import org.liuyichen.fifteenyan.R;

public class SettingsActivity extends BaseActivty {

    public static void launch(BaseActivty activity) {

        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.swipeback_stack_right_in,
                R.anim.swipeback_stack_to_back);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(getLayoutId())
                .setTouchMode(SwipeBack.TOUCH_MODE_BEZEL)
                .setSwipeBackView(R.layout.swipeback_default);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
