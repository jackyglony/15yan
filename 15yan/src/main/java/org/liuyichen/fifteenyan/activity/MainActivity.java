package org.liuyichen.fifteenyan.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.fragment.DetailFragment;
import org.liuyichen.fifteenyan.fragment.ViewPagerTabFragment;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.utils.Toast;


public class MainActivity extends BaseActivty {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ViewPagerTabFragment())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SettingsActivity.launch(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
