package com.chiao.pattern.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * 圆角抠图方案1
 *
 * @author jiao, <chikui_001@163.com>
 * @date 2016年10月27日
 */
public class RoundedCornerLayoutTwo extends FrameLayout {

	private Context mContext;

	public RoundedCornerLayoutTwo(Context context) {
		this(context, null);
	}

	public RoundedCornerLayoutTwo(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundedCornerLayoutTwo(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	@TargetApi(21)
	public RoundedCornerLayoutTwo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs, defStyleAttr);

	}

	private void init(Context context, AttributeSet attrs, int defStyleAttr) {
		this.mContext = context;
		setWillNotDraw(false);
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.parseColor("#DBDCDD"));
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(2);
	}

	private int offset = 1;
	private int radius = 4;
	private Paint borderPaint;

	@Override
	public void draw(Canvas canvas) {
		Path path = new Path();
		RectF roundF = new RectF(0, 0, getWidth(), getHeight());
		path.addRoundRect(roundF, dp2px(radius), dp2px(radius), Path.Direction.CCW);
		canvas.clipPath(path);
		super.draw(canvas);
		roundF.inset(offset, offset);
		canvas.drawRoundRect(roundF, dp2px(radius), dp2px(radius), borderPaint);

	}

	public int dp2px(float value) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
	}

}
