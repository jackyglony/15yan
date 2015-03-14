package org.liuyichen.fifteenyan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import org.liuyichen.fifteenyan.R;
import org.liuyichen.fifteenyan.activity.BaseActivty;
import org.liuyichen.fifteenyan.activity.DetailActivity;
import org.liuyichen.fifteenyan.model.Story;
import org.liuyichen.fifteenyan.utils.Settings;


/**
 * By liuyichen on 14-12-12 下午4:36.
 */
public class StoryAdapter extends BaseAdapter {

    private RoundedTransformationBuilder transformationBuilder;

    public StoryAdapter(Context context) {
        super(context, null);

        int radiu = (int)(mContext.getResources().getDimension(R.dimen.item_avatar_size) / 2.0f);
        transformationBuilder = new RoundedTransformationBuilder().cornerRadius(radiu).oval(false);
    }

    public class StoryViewHoler extends RecyclerView.ViewHolder {

        public TextView subtitle;
        public TextView title;
        public TextView editor;
        public TextView readcost;
        public ImageView avatar;

        public StoryViewHoler(View v) {
            super(v);
            this.title = (TextView)v.findViewById(R.id.title);
            this.subtitle = (TextView) v.findViewById(R.id.subtitle);
            this.editor = (TextView) v.findViewById(R.id.editor);
            this.readcost = (TextView) v.findViewById(R.id.readcost);
            this.avatar = (ImageView) v.findViewById(R.id.avatar);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_item_story;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View itemView) {
        return new StoryViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {

        final Story story = Story.loadCursor(cursor);

        final StoryViewHoler vh = (StoryViewHoler) holder;
        vh.title.setText(story.title);
        if (story.subtitle == null || TextUtils.isEmpty(story.subtitle)) {
            vh.subtitle.setVisibility(View.GONE);
        } else {
            vh.subtitle.setVisibility(View.VISIBLE);
            vh.subtitle.setText(story.subtitle);
        }

        vh.editor.setText(story.account.realname);
        vh.readcost.setText(mContext.getString(R.string.read_cost, story.readCost));
        if (Settings.canLoadImage()) {
            vh.avatar.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(story.account.avatar)
                    .fit()
                    .transform(transformationBuilder.build())
                    .into(vh.avatar);
        } else {
            vh.avatar.setVisibility(View.GONE);
        }
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DetailActivity.launch((BaseActivty) mContext, story);

            }
        });
    }

}
