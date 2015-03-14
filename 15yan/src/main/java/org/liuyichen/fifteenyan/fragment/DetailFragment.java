package org.liuyichen.fifteenyan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.api.FifteenYanService;
import org.liuyichen.fifteenyan.model.DetailCache;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.utils.AssetsUtils;
import org.liuyichen.fifteenyan.utils.Settings;
import org.liuyichen.fifteenyan.utils.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.inject.Inject;

import ollie.query.Select;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

/**
 * By liuyichen on 15-3-3 下午5:04.
 */
public class DetailFragment extends BaseFragment implements Callback<Response> {

    private static final String EXTRA_STORY = "DetailFragment:EXTRA_STORY";

    public static DetailFragment create(Story story) {

        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STORY, story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mStory = getArguments().getParcelable(EXTRA_STORY);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Inject
    protected WebView webview;
    @Inject
    protected FifteenYanService service;

    private Story mStory;

    @Override
    protected void onBindView(View rootView, Bundle savedInstanceState) {

        DetailCache cache = Select.from(DetailCache.class).where("storyId = ?", mStory.storyId).fetchSingle();
        if (cache != null) {
            webview.loadDataWithBaseURL(null, cache.detail, "text/html", "utf-8", null);
        } else {
            service.getDetailStory(mStory.storyId, this);
        }
    }

    @Override
    public void success(Response response, Response ignored) {

        TypedInput body = response.getBody();
        StringBuilder out = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.in(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String html = fixHtml(out.toString());
        DetailCache cache = new DetailCache();
        cache.storyId = mStory.storyId;
        cache.detail = html;
        cache.save();

        // picture mode
        webview.getSettings().setBlockNetworkImage(!Settings.canLoadImage());
        webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    private String fixHtml(String h) {

        Document doc = Jsoup.parse(h);
        Element body = doc.body();
        body.getElementsByClass("site-nav").remove();
        body.getElementsByClass("nav-side").remove();
        body.getElementsByClass("site-nav-overlay").remove();
        body.getElementsByClass("warn-bar").remove();
        body.getElementsByClass("image-mask-down").remove();
        body.getElementsByClass("post-item-meta").remove();
        body.getElementsByClass("story-cover-reading").remove();
        body.getElementsByClass("post-actions").remove();
        body.getElementsByClass("post-info").remove();
        body.getElementsByClass("post-footer").remove();

        String html = AssetsUtils.loadText(getActivity().getApplication(), "www/template.html");
        html = html.replace("{content}", body.html());

        return html;
    }

    @Override
    public void failure(RetrofitError error) {

        Toast.makeText(getActivity(), R.string.read_fail, Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }
}
