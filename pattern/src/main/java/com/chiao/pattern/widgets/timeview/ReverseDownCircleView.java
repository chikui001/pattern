package com.chiao.pattern.widgets.timeview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.chiao.pattern.R;
import com.chiao.pattern.utils.ScheduleCountTimer;

/**
 * circle one by one callback
 *
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年02月15日
 */
public class ReverseDownCircleView extends View {

	public void setReverseListener(ReverseListener reverseListener) {
		this.reverseListener = reverseListener;
	}

	private ReverseListener reverseListener;
	/**
	 * internal＝ 1000/25 ＝ 40；
	 * fps = 25
	 * 40毫秒一刷新
	 */
	private long interval = 40l;

	private long currentTimeInMill;
	/**
	 * 一圈几秒：20s
	 * 比如20s转完一圈
	 */
	private long oneCircleMill;

	private float currentSweepAngle = 0;

	private RectF mArcRect;

	private int circleColor = Color.BLACK;
	private int circleWidth = 10;

	private boolean mClockwise = true;

	private Paint paint;

	private ScheduleCountTimer scheduleCountTimer;

	/**
	 * start just when you tell me how long cross a circle
	 *
	 * @param testOneCircleMill
	 */
	public void start(long testOneCircleMill) {
		if (null != scheduleCountTimer) {
			return;
		}
		this.oneCircleMill = testOneCircleMill;
		scheduleCountTimer = new ScheduleCountTimer(interval) {

			@Override
			public void onTick() {
				if (currentTimeInMill + interval >= oneCircleMill) {
					if (null != reverseListener) {
						reverseListener.onCircle();
					}
				}
				currentTimeInMill = (currentTimeInMill + interval) % oneCircleMill;
				currentSweepAngle = ((float) currentTimeInMill / (float) oneCircleMill) * 360f;
				invalidate();

			}

			@Override
			public void onIntervalError() {

			}
		};
		scheduleCountTimer.start();
	}

	public void stop() {
		if (null != scheduleCountTimer) {
			scheduleCountTimer.cancel();
			scheduleCountTimer = null;
		}
		currentSweepAngle = 0;
		invalidate();
	}

	public ReverseDownCircleView(Context context) {
		this(context, null);
	}

	public ReverseDownCircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ReverseDownCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(21)
	public ReverseDownCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mArcRect = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null == mArcRect) {
			mArcRect = new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
								 getHeight() - getPaddingBottom());
			float dxDy = (float) circleWidth / 2f;
			mArcRect.inset(dxDy, dxDy);
		}
		canvas.drawArc(mArcRect, -90, (mClockwise ? 1 : -1) * currentSweepAngle, false, paint);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (null != scheduleCountTimer) {
			scheduleCountTimer.cancel();
			scheduleCountTimer = null;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int pleft = getPaddingLeft();
		int pright = getPaddingRight();
		int ptop = getPaddingTop();
		int pbottom = getPaddingBottom();

		int defaultWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int defaultHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		int circleDiameter = 0;
		if (0 == defaultWidth && 0 != defaultHeight) {
			int remainWidthForCircle = defaultWidth - pleft - pright;
			int remainHeightForCircle = defaultHeight - ptop - pbottom;
			circleDiameter = Math.min(remainWidthForCircle, remainHeightForCircle);
		} else if (0 == defaultWidth) {
			circleDiameter = defaultHeight - ptop - pbottom;
		} else {
			circleDiameter = defaultWidth - pleft - pright;
		}
		setMeasuredDimension(pleft + pright + circleDiameter, ptop + pbottom + circleDiameter);
	}

	private void init(Context context, AttributeSet attrs) {
		if (null != attrs) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownCircleView);
			circleWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, 5);
			circleColor = a.getColor(R.styleable.CircleImageView_civ_border_color, circleColor);
			if (null != a) {
				a.recycle();
			}
		}
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(circleColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(circleWidth);
	}

	public interface ReverseListener {

		void onCircle();
	}
}
