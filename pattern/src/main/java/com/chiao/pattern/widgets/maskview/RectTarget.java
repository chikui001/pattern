package com.chiao.pattern.widgets.maskview;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by jiao on 16/10/12.
 */

public class RectTarget implements Target {

	private Rect rect;

	public RectTarget(Rect rect) {
		this.rect = rect;
	}

	@Override
	public Point getPoint() {
		if (null != rect) {
			return new Point(rect.centerX(), rect.centerY());
		}
		return null;
	}

	@Override
	public Rect getBounds() {
		return rect;
	}

	@Override
	public RectF getBoundFs() {
		return new RectF(rect);
	}

	@Override
	public Rect getBounds(int offset) {
		if (null != rect) {
			return new Rect(rect.left - offset, rect.top - offset, rect.right + offset, rect.bottom + offset);
		}
		return null;
	}

	@Override
	public int getRadius() {
		if (null != rect) {
			return (int) Math.sqrt(Math.pow(rect.width() / 2, 2) + Math.pow(rect.height() / 2, 2));
		}
		return 0;
	}

	@Override
	public RectF getBoundFs(int offset) {
		if (null != rect) {
			return new RectF(rect.left - offset, rect.top - offset, rect.right + offset, rect.bottom + offset);
		}
		return null;
	}

	@Override
	public int getInCircleRadius() {
		if (null != rect) {
			return Math.min(rect.width() / 2, rect.height() / 2);
		}
		return 0;
	}
}
