package org.liuyichen.fifteenyan.adapter;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

/**
 * By liuyichen on 14-12-2 下午4:49.
 */
public abstract class CursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected boolean mDataValid;

    protected boolean mAutoRequery;

    protected Cursor mCursor;

    protected Context mContext;

    protected int mRowIDColumn;

    protected ChangeObserver mChangeObserver;

    protected DataSetObserver mDataSetObserver;

    @Deprecated
    public static final int FLAG_AUTO_REQUERY = 0x01;

    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 0x02;

    @Deprecated
    public CursorAdapter(Context context, Cursor c) {
        init(context, c, FLAG_AUTO_REQUERY);
    }

    public CursorAdapter(Context context, Cursor c, boolean autoRequery) {
        init(context, c, autoRequery ? FLAG_AUTO_REQUERY : FLAG_REGISTER_CONTENT_OBSERVER);
    }

    public CursorAdapter(Context context, Cursor c, int flags) {
        init(context, c, flags);
    }

    void init(Context context, Cursor c, int flags) {

        if ((flags & FLAG_AUTO_REQUERY) == FLAG_AUTO_REQUERY) {
            flags |= FLAG_REGISTER_CONTENT_OBSERVER;
            mAutoRequery = true;
        } else {
            mAutoRequery = false;
        }
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mContext = context;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        if ((flags & FLAG_REGISTER_CONTENT_OBSERVER) == FLAG_REGISTER_CONTENT_OBSERVER) {
            mChangeObserver = new ChangeObserver();
            mDataSetObserver = new MyDataSetObserver();
        } else {
            mChangeObserver = null;
            mDataSetObserver = null;
        }

        if (cursorPresent) {
            if (mChangeObserver != null) {
                c.registerContentObserver(mChangeObserver);
            }
            if (mDataSetObserver != null) {
                c.registerDataSetObserver(mDataSetObserver);
            }
        }
    }

    public abstract void onBindViewHolder(VH holder, Cursor cursor);

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, mCursor);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIDColumn);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    protected void onContentChanged() {
        if (mAutoRequery && mCursor != null && !mCursor.isClosed()) {
            mDataValid = mCursor.requery();
        }
    }

    private class ChangeObserver extends ContentObserver {

        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }

    private class MyDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            mDataValid = false;
            notifyDataSetChanged();
        }
    }
}
