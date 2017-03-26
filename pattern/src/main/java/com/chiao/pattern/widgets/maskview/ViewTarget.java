package com.chiao.pattern.widgets.maskview;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class ViewTarget implements Target {

	private final View mView;

	public ViewTarget(View view) {
		mView = view;
	}

	public ViewTarget(int viewId, Activity activity) {
		mView = activity.findViewById(viewId);
	}

	@Override
	public Point getPoint() {
		int[] location = new int[2];
		mView.getLocationInWindow(location);
		int x = location[0] + mView.getWidth() / 2;
		int y = location[1] + mView.getHeight() / 2;
		return new Point(x, y);
	}

	@Override
	public Rect getBounds(int offset) {
		int[] location = new int[2];
		mView.getLocationInWindow(location);
		return new Rect(location[0] - offset, location[1] - offset, location[0] + mView.getMeasuredWidth() + offset,
						location[1] + mView.getMeasuredHeight() + offset);
	}

	@Override
	public Rect getBounds() {
		int[] location = new int[2];
		mView.getLocationInWindow(location);
		return new Rect(location[0], location[1], location[0] + mView.getMeasuredWidth(), location[1] + mView.getMeasuredHeight());
	}

	@Override
	public RectF getBoundFs() {
		int[] location = new int[2];
		mView.getLocationInWindow(location);
		return new RectF(location[0], location[1], location[0] + mView.getMeasuredWidth(), location[1] + mView.getMeasuredHeight());
	}

	@Override
	public RectF getBoundFs(int offset) {
		int[] location = new int[2];
		mView.getLocationInWindow(location);
		return new RectF(location[0] - offset, location[1] - offset, location[0] + mView.getMeasuredWidth() + offset,
						 location[1] + mView.getMeasuredHeight() + offset);
	}

	@Override
	public int getInCircleRadius() {
		return Math.min(mView.getMeasuredWidth() / 2, mView.getMeasuredHeight() / 2);
	}

	@Override
	public int getRadius() {
		return (int) Math.sqrt(Math.pow(mView.getMeasuredWidth() / 2, 2) + Math.pow(mView.getMeasuredHeight() / 2, 2));
	}
}
