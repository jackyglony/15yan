package org.liuyichen.fifteenyan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * By liuyichen on 14-12-1 下午5:22.
 */
public abstract class BaseAdapter extends CursorAdapter {


    protected BaseAdapter(Context context, Cursor c) {
        super(context, c);
    }

    protected BaseAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    protected BaseAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    protected abstract int getLayoutId();
    protected abstract RecyclerView.ViewHolder getViewHolder(View itemView);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutId(), viewGroup, false);
        RecyclerView.ViewHolder holder = getViewHolder(v);
        return holder;
    }


}
