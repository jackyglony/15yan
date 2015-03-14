package org.liuyichen.fifteenyan.fragment;


import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.utils.Settings;

import javax.inject.Inject;


public class SettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Inject
    protected SwitchCompat switchCompat;

    @Override
    protected void onBindView(View rootView, Bundle savedInstanceState) {

        switchCompat.setChecked(Settings.isOnlyWifiOpen());
        switchCompat.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Settings.switchOnlyWifiMode(b);
    }
}
