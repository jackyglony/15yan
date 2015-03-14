package org.liuyichen.fifteenyan.module;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.activity.BaseActivty;
import org.liuyichen.fifteenyan.adapter.NavigationAdapter;
import org.liuyichen.fifteenyan.adapter.StoryAdapter;
import org.liuyichen.fifteenyan.fragment.BaseFragment;
import org.liuyichen.fifteenyan.view.SlidingTabLayout;

import dagger.Module;
import dagger.Provides;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * By liuyichen on 15-3-4 上午10:39.
 */
@Module(library = true)
public class FragmentModule {

    private BaseActivty activty;
    private BaseFragment fragment;
    private View v;

    public FragmentModule(BaseFragment fragment, View v) {
        this.fragment = fragment;
        this.activty = (BaseActivty)fragment.getActivity();
        this.v = v;
    }

    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(activty);
    }

    @Provides
    RecyclerView provideRecyclerView(RecyclerView.LayoutManager lm) {

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);
        if (Build.VERSION.SDK_INT >= 9) {
            recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        } else {
            recyclerView.setFadingEdgeLength(0);
        }

        recyclerView.setLayoutManager(lm);

        return recyclerView;
    }

    @Provides
    PtrUIHandler providePtrUIHandler() {

        StoreHouseHeader header = new StoreHouseHeader(activty);
        header.initWithString("15 yan");

        return header;
    }

    @Provides
    PtrFrameLayout providePtrFrameLayout(PtrUIHandler header) {

        PtrFrameLayout ptrFrameLayout = (PtrFrameLayout)v.findViewById(R.id.ptr_frame);
        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView((View)header);
        ptrFrameLayout.addPtrUIHandler(header);

        return ptrFrameLayout;
    }

    @Provides
    StoryAdapter provideStoryAdapter() {

        return new StoryAdapter(activty);
    }

    @Provides
    TouchInterceptionFrameLayout provideTouchInterceptionFrameLayout() {

        return (TouchInterceptionFrameLayout)v.findViewById(R.id.container);
    }

    @Provides
    ViewPager provideViewPager() {

        return (ViewPager) v.findViewById(R.id.pager);
    }

    @Provides
    NavigationAdapter provideNavigationAdapter() {
        return new NavigationAdapter(activty, fragment.getChildFragmentManager());
    }

    @Provides
    int provideSlop() {
        ViewConfiguration vc = ViewConfiguration.get(activty);
        return vc.getScaledTouchSlop();
    }

    @Provides
    SlidingTabLayout provideSlidingTabLayout() {
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.view_tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(fragment.getResources().getColor(R.color.accent));

        return slidingTabLayout;
    }

    @Provides
    WebView provideObservableWebView() {
        WebView webView = (WebView) v.findViewById(R.id.scrollable);
        if (Build.VERSION.SDK_INT < 9) {
            webView.setFadingEdgeLength(0);
        } else if (Build.VERSION.SDK_INT < 21) {
            webView.setOverScrollMode(OVER_SCROLL_NEVER);
        }
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT >= 11) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
   //     String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
 //       webView.getSettings().setAppCachePath(cacheDirPath);
        webView.getSettings().setAppCacheEnabled(true);

        return webView;
    }

    @Provides
    SwitchCompat providesSwitchCompat() {

        return (SwitchCompat) v.findViewById(R.id.only_wifi_switch);
    }
}
