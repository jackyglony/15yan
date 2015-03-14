package org.liuyichen.fifteenyan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.adapter.NavigationAdapter;
import org.liuyichen.fifteenyan.view.SlidingTabLayout;

import javax.inject.Inject;

/**
 * Created by root on 15-3-6.
 */
public class ViewPagerTabFragment extends BaseFragment {


    public ViewPagerTabFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewpagertab;
    }

    @Inject
    protected TouchInterceptionFrameLayout interceptionLayout;
    @Inject
    protected NavigationAdapter pagerAdapter;

    @Inject
    protected ViewPager pager;

    @Inject
    protected int slop;

    @Inject
    protected SlidingTabLayout slidingTabLayout;

    private boolean mScrolled;
    private ScrollState mLastScrollState;

    @Override
    protected void onBindView(View rootView, Bundle savedInstanceState) {

        pager.setAdapter(pagerAdapter);
        slidingTabLayout.setViewPager(pager);
        interceptionLayout.setScrollInterceptionListener(mInterceptionListener);
    }

    private Scrollable getCurrentScrollable() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return null;
        }
        View view = fragment.getView();
        if (view == null) {
            return null;
        }
        return (Scrollable) view.findViewById(R.id.recyclerview);
    }

    private void adjustToolbar(ScrollState scrollState) {
        View toolbarView = getToolbar();
        int toolbarHeight = toolbarView.getHeight();
        final Scrollable scrollable = getCurrentScrollable();
        if (scrollable == null) {
            return;
        }
        int scrollY = scrollable.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else if (!toolbarIsShown() && !toolbarIsHidden()) {
            showToolbar();
        }
    }

    private Fragment getCurrentFragment() {
        return pagerAdapter.getItemAt(pager.getCurrentItem());
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(interceptionLayout) == 0;
    }

    private boolean toolbarIsHidden() {
        View view = getView();
        if (view == null) {
            return false;
        }
        View toolbarView = getToolbar();
        return ViewHelper.getTranslationY(interceptionLayout) == -toolbarView.getHeight();
    }

    private void showToolbar() {
        animateToolbar(0);
    }

    private void hideToolbar() {
        View toolbarView = getToolbar();
        animateToolbar(-toolbarView.getHeight());
    }

    private void animateToolbar(final float toY) {
        float layoutTranslationY = ViewHelper.getTranslationY(interceptionLayout);
        if (layoutTranslationY != toY) {
            ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(interceptionLayout), toY).setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translationY = (float) animation.getAnimatedValue();
                    View toolbarView = getToolbar();
                    ViewHelper.setTranslationY(interceptionLayout, translationY);
                    ViewHelper.setTranslationY(toolbarView, translationY);
                    if (translationY < 0) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) interceptionLayout.getLayoutParams();
                        lp.height = (int) (-translationY + getScreenHeight());
                        interceptionLayout.requestLayout();
                    }
                }
            });
            animator.start();
        }
    }

    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            if (!mScrolled && slop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
                // Horizontal scroll is maybe handled by ViewPager
                return false;
            }

            Scrollable scrollable = getCurrentScrollable();
            if (scrollable == null) {
                mScrolled = false;
                return false;
            }

            // If interceptionLayout can move, it should intercept.
            // And once it begins to move, horizontal scroll shouldn't work any longer.
            View toolbarView = getToolbar();
            int toolbarHeight = toolbarView.getHeight();
            int translationY = (int) ViewHelper.getTranslationY(interceptionLayout);
            boolean scrollingUp = 0 < diffY;
            boolean scrollingDown = diffY < 0;
            if (scrollingUp) {
                if (translationY < 0) {
                    mScrolled = true;
                    mLastScrollState = ScrollState.UP;
                    return true;
                }
            } else if (scrollingDown) {
                if (-toolbarHeight < translationY) {
                    mScrolled = true;
                    mLastScrollState = ScrollState.DOWN;
                    return true;
                }
            }
            mScrolled = false;
            return false;
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            View toolbarView = getToolbar();
            float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(interceptionLayout) + diffY, -toolbarView.getHeight(), 0);
            ViewHelper.setTranslationY(interceptionLayout, translationY);
            ViewHelper.setTranslationY(toolbarView, translationY);
            if (translationY < 0) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) interceptionLayout.getLayoutParams();
                lp.height = (int) (-translationY + getScreenHeight());
                interceptionLayout.requestLayout();
            }
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            mScrolled = false;
            adjustToolbar(mLastScrollState);
        }
    };
}
