package com.chiao.pattern.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年02月17日
 */
public class RespectWidthHorizontalScrollView extends HorizontalScrollView {

	public RespectWidthHorizontalScrollView(Context context) {
		super(context);
	}

	public RespectWidthHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RespectWidthHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public RespectWidthHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}

	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
		ViewGroup.LayoutParams lp = child.getLayoutParams();
		int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
		int childHeightMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 0, lp.height);
		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	@Override
	protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec,
			int heightUsed) {
		MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
		int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
														getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
																+ widthUsed, lp.width);
		final int childHeightMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
															   lp.topMargin + lp.bottomMargin, lp.height);
		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}
}
