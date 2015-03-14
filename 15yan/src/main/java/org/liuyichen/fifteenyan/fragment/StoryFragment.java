package org.liuyichen.fifteenyan.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.adapter.StoryAdapter;
import org.liuyichen.fifteenyan.api.Category;
import org.liuyichen.fifteenyan.api.FifteenYanService;
import org.liuyichen.fifteenyan.model.Data;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.provider.FifteenYanProvider;
import org.liuyichen.fifteenyan.utils.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * By liuyichen on 15-3-3 下午5:03.
 */
public class StoryFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, PtrHandler, Callback<Data>{

    private static final String EXTRA_CATEGORY = "StoryFragment:EXTRA_CATEGORY";

    public static StoryFragment create(Category category) {

        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        category = (Category)getArguments().getSerializable(EXTRA_CATEGORY);
    }

    private Category category;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_story;
    }

    @Inject
    protected FifteenYanService service;
    @Inject
    protected RecyclerView recyclerView;
    @Inject
    protected PtrFrameLayout ptrFrameLayout;
    @Inject
    protected StoryAdapter storyAdapter;

    private int offset = 0;

    private boolean loading = false;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onBindView(View rootView, Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        ptrFrameLayout.setPtrHandler(this);
        recyclerView.setAdapter(storyAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                if (!loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        load(offset);
                    }
                }
            }
        });
    }

    protected void load(int offset) {

        loading = true;
        service.getStorys(offset, category.value(), this);
    }

    @Override
    public void success(Data data, Response response) {
        ptrFrameLayout.refreshComplete();
        ArrayList<Story> results = data.result;
        for (Story story: results) {
            story.category = category.value();
            story.saved();
        }
        loading = false;
    }

    @Override
    public void failure(RetrofitError error) {
        ptrFrameLayout.refreshComplete();
        Toast.makeText(getActivity(), R.string.read_fail, Toast.LENGTH_SHORT).show();
        loading = false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                FifteenYanProvider.createUri(Story.class),
                null, "category = ?", new String[]{category.value()}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        storyAdapter.changeCursor(cursor);
        offset = cursor.getCount();
        if (cursor != null && cursor.getCount() == 0) {
            load(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        storyAdapter.changeCursor(null);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptr) {
        offset = 0;
        Story.clear(category.value());
        load(offset);
    }
}
