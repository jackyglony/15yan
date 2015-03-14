package org.liuyichen.fifteenyan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.activity.BaseActivty;
import org.liuyichen.fifteenyan.module.FifteenApiModule;
import org.liuyichen.fifteenyan.module.FragmentModule;

import dagger.ObjectGraph;

/**
 * By liuyichen on 15-3-3 下午5:04.
 */
public abstract class BaseFragment extends Fragment {


    protected abstract int getLayoutId();

    protected abstract void  onBindView(View rootView, Bundle savedInstanceState);

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), container, false);
        ObjectGraph.create(FifteenApiModule.class, new FragmentModule(this, v)).inject(this);
        onBindView(v, savedInstanceState);
        return v;
    }

    protected Toolbar getToolbar() {
        Toolbar toolbarView = (Toolbar)getActivity().findViewById(R.id.toolbar);
        return toolbarView;
    }

    protected int getScreenHeight() {
        return ((BaseActivty)getActivity()).getScreenHeight();
    }
}
