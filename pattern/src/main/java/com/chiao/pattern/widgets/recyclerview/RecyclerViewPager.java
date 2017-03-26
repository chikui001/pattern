package com.chiao.pattern.widgets.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 1，如果子view，在MotionAction_ACTION_CANCEL的时候触发－>performClick->scrollToNext
 * RecyclerView会执行cancelTouch->stopScroll，和上面的scrollToNext冲突
 * <p>
 * Created by jiao on 16/7/18.
 */
public class RecyclerViewPager extends RecyclerView {

	public void setDisableTouchScroll(boolean disableTouchScroll) {
		this.disableTouchScroll = disableTouchScroll;
	}

	private boolean disableTouchScroll = true;

	public void setCurrentIndex(final int currentIndex) {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrollToPosition(currentIndex);
			}
		}, 300);
	}

	public RecyclerViewPager(Context context) {
		this(context, null);
	}

	public RecyclerViewPager(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RecyclerViewPager(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public boolean fling(int velocityX, int velocityY) {
		return super.fling(velocityX, velocityY);
	}

	@Override
	public void stopScroll() {
		super.stopScroll();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		if (disableTouchScroll) {
			return true;
		}
		return super.onInterceptTouchEvent(e);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (disableTouchScroll) {
			//如果不允许手动滑动，需要把事件主动的传给recyclerView-subView,以便后者的click时间可以处理
			View view = findChildViewUnder(e.getX(), e.getY());
			if (null != view) {
				view.dispatchTouchEvent(e);
			}
			return true;
		}
		return super.onTouchEvent(e);
	}

	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		super.onMeasure(widthSpec, heightSpec);
	}

	private Handler handler = new Handler();

	private void init() {
		//禁止手动滑动，就不需要computerOffset了

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

	private void computerOffset() {
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
			View child = linearLayoutManager.getChildAt(i);
			int childStart = linearLayoutManager.getDecoratedLeft(child);
			int childEnd = linearLayoutManager.getDecoratedRight(child);
			if (childStart <= getWidth() / 2 && childEnd >= getWidth() / 2) {
				int index = linearLayoutManager.getPosition(child);
				int left = getWidth() / 2 - childStart;
				int right = childEnd - getWidth() / 2;
				int width = child.getWidth();
				if (2 > Math.abs(right - left)) {
					//小时滑动的时候，引起天滚动，天被动滚动的时候，不会执行onScrollStop,所以要主动刷新
					scrollStop(index);
					break;
				}
				if (!disableTouchScroll) {
					//禁止手动滑动不需要矫正offset
					if (left < width / 2) {
						//					stopScroll();
						smoothScrollBy(width / 2 - left, 0);
					} else {
						//					stopScroll();
						smoothScrollBy(width / 2 - left, 0);
					}
				}
				break;
			}
		}
	}

	public void scrollToNext() {
		int currentPosition = -1;
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
			View child = linearLayoutManager.getChildAt(i);
			int childStart = linearLayoutManager.getDecoratedLeft(child);
			int childEnd = linearLayoutManager.getDecoratedRight(child);
			if (childStart <= getWidth() / 2 && childEnd >= getWidth() / 2) {
				currentPosition = linearLayoutManager.getPosition(child);
				break;
			}
		}
		if (currentPosition < getAdapter().getItemCount() - 1) {
			smoothScrollToPosition(currentPosition + 1);

		} else {
			//Toast.makeText(getContext(), "没有下一个月了", Toast.LENGTH_SHORT).show();
		}
	}

	public void scrollToLast() {
		int currentPosition = -1;
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
			View child = linearLayoutManager.getChildAt(i);
			int childStart = linearLayoutManager.getDecoratedLeft(child);
			int childEnd = linearLayoutManager.getDecoratedRight(child);
			if (childStart <= getWidth() / 2 && childEnd >= getWidth() / 2) {
				currentPosition = linearLayoutManager.getPosition(child);
				break;
			}
		}
		if (currentPosition > 0) {
			//			stopScroll();
			smoothScrollToPosition(currentPosition - 1);
		} else {
			//Toast.makeText(getContext(), "没有上一个月了", Toast.LENGTH_SHORT).show();
		}
	}

	public int getCurrentPosition() {
		int currentPosition = -1;
		LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) getLayoutManager());
		for (int i = 0; i < linearLayoutManager.getChildCount(); i++) {
			View child = linearLayoutManager.getChildAt(i);
			int childStart = linearLayoutManager.getDecoratedLeft(child);
			int childEnd = linearLayoutManager.getDecoratedRight(child);
			if (childStart <= getWidth() / 2 && childEnd >= getWidth() / 2) {
				currentPosition = linearLayoutManager.getPosition(child);
				break;
			}
		}
		return currentPosition;
	}

	private void scrollStop(int position) {
		Log.d("RecyclerViewPager", "scrollStop-position:" + position);
		if (null != scrollListener) {
			scrollListener.onScrollStop(position);
		}
	}

	public void setScrollListener(ScrollListener scrollListener) {
		this.scrollListener = scrollListener;
	}

	private ScrollListener scrollListener;

	public interface ScrollListener {

		void onScrollStop(int position);
	}
}
