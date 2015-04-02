package org.liuyichen.fifteenyan.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * By liuyichen on 15-3-4 上午10:50.
 */
public abstract class BaseActivty extends ActionBarActivity {


    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }
}
