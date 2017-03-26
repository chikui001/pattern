package com.chiao.pattern.widgets.maskview;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public interface Target {

	Point getPoint();

	Rect getBounds();

	RectF getBoundFs();

	Rect getBounds(int offset);

	int getRadius();

	RectF getBoundFs(int offset);

	int getInCircleRadius();
}
