package com.chiao.pattern.widgets.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年11月07日
 */
public class LoadMoreRecyclerView extends RecyclerView {

	private String TAG = "LoadMoreRecyclerView";
	private boolean debug = true;

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		this.onLoadMoreListener = onLoadMoreListener;
	}

	private OnLoadMoreListener onLoadMoreListener;

	public LoadMoreRecyclerView(Context context) {
		this(context, null);
	}

	public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (!isLoadMore() && dy > 0) {//check for scroll down
					if (getLayoutManager() instanceof LinearLayoutManager) {
						int visibleItemCount = getLayoutManager().getChildCount();
						int totalItemCount = getLayoutManager().getItemCount();
						int pastVisibleItems = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
						if (totalItemCount == (visibleItemCount + pastVisibleItems)) {
							if (debug) {
								Log.d(TAG, "onLoadMore");
							}
							if (null != onLoadMoreListener) {
								setLoadMore(true);
								onLoadMoreListener.onLoadMore();
							}
						}
					}
				}
			}

		});
	}

	public boolean isLoadMore() {
		if (getAdapter() instanceof LoadMoreAdapter) {
			return ((LoadMoreAdapter) getAdapter()).isLoadMore();
		}
		return false;
	}

	public void setLoadMore(boolean loadMore) {
		if (getAdapter() instanceof LoadMoreAdapter) {
			((LoadMoreAdapter) getAdapter()).setLoadMore(loadMore);
		}
	}
}
