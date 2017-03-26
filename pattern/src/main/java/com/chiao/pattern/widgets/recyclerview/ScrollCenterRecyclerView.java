package com.chiao.pattern.widgets.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by jiao on 16/3/24.
 */
public class ScrollCenterRecyclerView extends RecyclerView {

	public ScrollCenterRecyclerView(Context context) {
		this(context, null);
	}

	public ScrollCenterRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public ScrollCenterRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private Handler handler = new Handler();

	private void init() {
		addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					//滑动停止
					computerOffset();
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});
	}

	private CenterScrollListener centerScrollListener;

	public void setCenterScrollListener(CenterScrollListener centerScrollListener) {
		this.centerScrollListener = centerScrollListener;
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				int judgeX = getWidth() / 2;
				setPadding(judgeX, 0, judgeX, 0);
				View view = getChildAt(0);
				if (null != view) {
					itemWidth = getChildViewHolder(view).itemView.getWidth();
					scrollSpecificPosition(setDefaultPosition);
					onScrollStopInit(setDefaultPosition);
				}
			}
		}, 300);
	}

	private void onScrollStopInit(int position) {
		if (null != centerScrollListener) {
			mCurrentPosition = position;
			mExplicitCurrentPosition = position;
			centerScrollListener.onScrollStopInit(position);
		}
	}

	public void scrollSpecificPosition(int position) {
		if (0 != itemWidth) {
			int judgeX = getWidth() / 2;
			int leftPadding = getPaddingLeft();
			int offsetPadding = leftPadding + position * itemWidth + itemWidth / 2 - judgeX;
			int offsetIndex = offsetPadding / itemWidth;
			int offset = offsetPadding % itemWidth;
			//((LinearLayoutManager) (mRecyclerView.getLayoutManager())).scrollToPosition(offsetIndex);
			((LinearLayoutManager) (getLayoutManager())).scrollToPositionWithOffset(offsetIndex, -offset);
		}
	}

	private int itemWidth = 0;
	private int setDefaultPosition = 0;

	//not explicit
	private int mCurrentPosition = -1;

	private int mExplicitCurrentPosition = -1;

	public int getCurrentPositionExplicitPosition() {
		return mExplicitCurrentPosition == -1 ? setDefaultPosition : mExplicitCurrentPosition;
	}

	public int getCurrentPosition() {
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		int judgeX = getWidth() / 2;
		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
			View child = linearLayoutManager.getChildAt(i);
			int childStart = linearLayoutManager.getDecoratedLeft(child);
			int childEnd = linearLayoutManager.getDecoratedRight(child);
			if (childStart <= judgeX && childEnd >= judgeX) {
				int index = linearLayoutManager.getPosition(child);
				mCurrentPosition = index;
				break;
			}
		}
		if (-1 == mCurrentPosition) {
			return setDefaultPosition;
		}
		return mCurrentPosition;
	}

	public void setSetDefaultPosition(int position) {
		if (position < 0) {
			position = 0;
		}
		this.setDefaultPosition = position;
	}

	private void computerOffset() {
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		int judgeX = getWidth() / 2;
		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
			View child = linearLayoutManager.getChildAt(i);
			int childStart = linearLayoutManager.getDecoratedLeft(child);
			int childEnd = linearLayoutManager.getDecoratedRight(child);
			if (childStart <= judgeX && childEnd >= judgeX) {
				int index = linearLayoutManager.getPosition(child);
				int left = judgeX - childStart;
				int right = childEnd - judgeX;
				int width = child.getWidth();
				Log.d("DCRecyclerView", "left:" + left + ";right" + right);
				mCurrentPosition = index;
				if (2 > Math.abs(right - left)) {
					//小时滑动的时候，引起天滚动，天被动滚动的时候，不会执行onScrollStop,所以要主动刷新
					stopScroll();
					mExplicitCurrentPosition = index;
					scrollStop(index);
					break;
				}
				if (left < width / 2) {
					stopScroll();
					smoothScrollBy(width / 2 - left, 0);
				} else {
					stopScroll();
					smoothScrollBy(width / 2 - left, 0);
				}
				break;
			}
		}
	}

	private void scrollStop(int index) {
		if (null != centerScrollListener) {
			centerScrollListener.onScrollStop(index);
		}
	}
}
