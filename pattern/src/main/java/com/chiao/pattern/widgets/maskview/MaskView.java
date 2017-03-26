package com.chiao.pattern.widgets.maskview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.chiao.pattern.R;

import java.util.HashMap;
import java.util.Map;

/**
 * guide view
 * Created by jiao on 16/9/28.
 */

public class MaskView extends ViewGroup {

	private int currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	private static final String TAG = "MaskView";

	private static final int SHADOW_COLOR_DEFAULT = Color.parseColor("#C0000000");

	public void setColor(int color) {
		this.color = color;
		if (null != paint) {
			paint.setColor(color);
		}
	}

	private int color = SHADOW_COLOR_DEFAULT;

	/**
	 * 安全套的理想距离
	 */
	private static final int SHADOW_OFFSET_DEFAULT = 10;
	private int shadowOffset = SHADOW_OFFSET_DEFAULT;

	public void setShadowOffset(int shadowOffset) {
		this.shadowOffset = shadowOffset;
	}

	private boolean hasClicked = false;
	private Context mContext;

	private View rootView = null;

	private DismissListener dismissListener;

	private boolean dismissListenerHasInvoked = false;
	/**
	 * rect抠图
	 */
	private SparseArray<Rect> targetRect = new SparseArray();
	/**
	 * view抠图
	 */
	private Map<Long, MaskViewType> targetIds = new HashMap<>();
	/**
	 * 包括抠图的view,和anchorView
	 */
	private Map<Long, Target> targetAnchorViews = new HashMap<>();

	/**
	 * 一个anchor-view的时候
	 */
	private View anchorView;

	/**
	 * 一个anchor-rect的时候
	 */
	private Rect anchorRect;

	private Paint paint = new Paint();

	public void setDismissListener(DismissListener dismissListener) {
		this.dismissListener = dismissListener;
	}

	public void addRect(@IdRes int viewId, Rect rect) {
		if (viewId > 0 && null != rect) {
			targetRect.put(viewId, rect);
		}
	}

	public void addMask(int someSpecific, View view, MaskViewType viewType) {
		if (null != view && null != viewType) {
			targetIds.put((long) someSpecific, viewType);
			targetAnchorViews.put((long) someSpecific, new ViewTarget(view));
		}
	}

	/**
	 * 2000000 200000 - viewParentId,viewId
	 *
	 * @param viewId
	 * @param parentId
	 * @param viewType
	 */
	public void addMask(@IdRes int viewId, @IdRes int parentId, MaskViewType viewType) {
		long id1 = (long) viewId;
		long id2 = (long) parentId;
		targetIds.put(id2 << 32 | id1, viewType);
	}

	public void addMask(@IdRes int id, MaskViewType viewType) {
		addMask(id, 0, viewType);
	}

	public MaskView(Context context) {
		this(context, null);
	}

	public MaskView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(21)
	public MaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	@Override
	public void dispatchConfigurationChanged(Configuration newConfig) {
		super.dispatchConfigurationChanged(newConfig);
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (null != newConfig && newConfig.orientation != currentOrientation) {
			setVisibility(View.GONE);
			if (null != dismissListener && !dismissListenerHasInvoked) {
				dismissListenerHasInvoked = true;
				dismissListener.onDismiss();
			}
		}
		currentOrientation = newConfig.orientation;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		Path path = new Path();
		for (int i = 0; i < targetRect.size(); i++) {
			int key = targetRect.keyAt(i);
			Rect rect = targetRect.get(key);
			if (null != rect) {
				path.addCircle(rect.centerX(), rect.centerY(), rect.width() / 2, Path.Direction.CW);
			}
		}
		for (Map.Entry<Long, Target> entry : targetAnchorViews.entrySet()) {
			long viewId = entry.getKey();
			Target viewTarget = targetAnchorViews.get(viewId);
			MaskViewType drawType = targetIds.get(viewId);
			if (null != viewTarget && null != drawType) {
				switch (drawType) {
					case REAL_DOUBLE_CIRCLE:
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius(), Path.Direction.CW);
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius() - dp2px(1),
									   Path.Direction.CW);
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius() - dp2px(3),
									   Path.Direction.CW);
						break;
					case DOUBLE_CIRCLE:
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius() + dp2px(12),
									   Path.Direction.CW);
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius() + dp2px(11),
									   Path.Direction.CW);
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius() + dp2px(5),
									   Path.Direction.CW);
						break;
					case LITTLE_DOUBLE_CIRCLE:
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius() + dp2px(7),
									   Path.Direction.CW);
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius() + dp2px(6),
									   Path.Direction.CW);
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getRadius() + dp2px(3),
									   Path.Direction.CW);
						break;
					case INNER_DOUBLE_CIRCLE:
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getInCircleRadius() + dp2px(7),
									   Path.Direction.CW);
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getInCircleRadius() + dp2px(6),
									   Path.Direction.CW);
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getInCircleRadius(),
									   Path.Direction.CW);
						break;
					case INNER_CIRCLE:
						path.addCircle(viewTarget.getPoint().x, viewTarget.getPoint().y, viewTarget.getInCircleRadius(),
									   Path.Direction.CW);
						break;
					case RECT:
						//												path.addRect(viewTarget.getBoundFs(), Path.Direction.CW);
						path.addRoundRect(viewTarget.getBoundFs(), new float[] { 0, 0, 0, 0, 0, 0, 0, 0, }, Path.Direction.CW);
						break;
					case X:
						path.addRoundRect(viewTarget.getBoundFs(-dp2px(6)), dp2px(40), dp2px(40), Path.Direction.CW);
						break;
					case Y:
						path.addRoundRect(viewTarget.getBoundFs(),
										  new float[] { dp2px(22), dp2px(22), dp2px(22), dp2px(22), 0, 0, 0, 0, },
										  Path.Direction.CW);
						break;
					case HEART:
						RectF rectF = viewTarget.getBoundFs();
						float x = rectF.right;
						float y = rectF.bottom;
						float length = rectF.width() * 2 / 3;
						path.moveTo(x, y);
						path.lineTo(x - length, y);
						path.arcTo(new RectF(x - length - (length / 2), y - length, x - (length / 2), y), 90, 180);
						path.arcTo(new RectF(x - length, y - length - (length / 2), x, y - (length / 2)), 180, 180);
						path.lineTo(x, y);

						Matrix mMatrixRotate = new Matrix();
						RectF bounds = new RectF();
						path.computeBounds(bounds, true);
						mMatrixRotate.postRotate(45, x - length / 2, y - length - (length / 2));
						path.transform(mMatrixRotate);
						Matrix mMatrixTransform = new Matrix();
						mMatrixTransform.postTranslate((length * 1) / 3, 0);
						path.transform(mMatrixTransform);
						break;
				}
			}
		}
		path.addRect(getLeft(), getTop(), getRight(), getBottom(), Path.Direction.CW);
		path.setFillType(Path.FillType.EVEN_ODD);
		//		canvas.clipPath(path, Region.Op.XOR);
		//		canvas.drawColor(Color.parseColor("#59000000"));
		canvas.drawPath(path, paint);
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		rootView = getRootView();
		for (Map.Entry<Long, MaskViewType> entry : targetIds.entrySet()) {
			long id = entry.getKey();
			int viewId = (int) id;
			int viewParentId = (int) (id >> 32);

			if (0 == viewParentId) {
				View targetView = rootView.findViewById(viewId);
				if (null != targetView) {
					targetAnchorViews.put(id, new ViewTarget(targetView));
				}
			} else {
				View parentView = rootView.findViewById(viewParentId);
				if (null != parentView) {
					View targetView = parentView.findViewById(viewId);
					if (null != targetView) {
						targetAnchorViews.put(id, new ViewTarget(targetView));
					}
				}
			}
		}
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hasClicked = true;

				detachItselfFromParent();

				if (null != dismissListener && !dismissListenerHasInvoked) {
					dismissListenerHasInvoked = true;
					dismissListener.onDismiss();
				}

				if (null != showAnimation) {
					showAnimation.cancel();
				}
				if (null != dismissAnimation) {
					dismissAnimation.cancel();
				}
			}
		});
	}

	private void detachItselfFromParent() {
		if (null != getParent()) {
			((ViewGroup) getParent()).removeView(this);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (null != showAnimation) {
			showAnimation.cancel();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutChildren(l, t, r, b, false);
	}

	private void layoutChildren(int l, int t, int r, int b, boolean forceLeftGravity) {
		//targetIds,有没有find到的view再尝试一次(menu-actionLayout)
		for (Map.Entry<Long, MaskViewType> entry : targetIds.entrySet()) {
			long id = entry.getKey();
			if (targetAnchorViews.containsKey(id)) {
				continue;
			}
			int viewId = (int) id;
			int viewParentId = (int) (id >> 32);

			if (0 == viewParentId) {
				View targetView = rootView.findViewById(viewId);
				if (null != targetView) {
					targetAnchorViews.put(id, new ViewTarget(targetView));
				}
			} else {
				View parentView = rootView.findViewById(viewParentId);
				if (null != parentView) {
					View targetView = parentView.findViewById(viewId);
					if (null != targetView) {
						targetAnchorViews.put(id, new ViewTarget(targetView));
					}
				}
			}
		}

		final int count = getChildCount();
		final int parentLeft = getLeft();
		final int parentRight = getRight();

		final int parentTop = getTop();
		final int parentBottom = getBottom();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				final int width = child.getMeasuredWidth();
				final int height = child.getMeasuredHeight();

				int childLeft = 0;
				int childTop = 0;

				int gravity = lp.gravity;
				int anchor = lp.anchor;

				if (-1 != anchor) {
					Target target = null;
					if (null != anchorRect) {
						target = new RectTarget(anchorRect);
					} else if (null != anchorView) {
						target = new ViewTarget(anchorView);
					} else {
						long viewId = lp.anchorId;
						long viewParentId = lp.anchorParentId;
						long id = viewParentId << 32 | viewId;
						if (viewId == View.NO_ID) {
							continue;
						}
						target = (View.NO_ID == viewParentId) ? targetAnchorViews.get(viewId) : targetAnchorViews.get(id);
						if (null == target) {
							if (View.NO_ID == viewParentId) {
								View targetView = rootView.findViewById((int) viewId);
								if (null != targetView) {
									target = new ViewTarget(targetView);
									targetAnchorViews.put(id, target);
								}
							} else {
								View parentView = rootView.findViewById((int) viewParentId);
								if (null != parentView) {
									View targetView = parentView.findViewById((int) viewId);
									if (null != targetView) {
										target = new ViewTarget(targetView);
										targetAnchorViews.put(id, target);
									}
								}
							}
						}
					}

					if (null != target) {
						switch (lp.getHorizontal()) {
							case LayoutParams.RIGHT:
								childLeft = target.getBounds(dp2px(shadowOffset)).right + lp.leftMargin - lp.rightMargin;
								switch (lp.getVertical()) {
									case LayoutParams.BOTTOM:
										Log.d(TAG, "right|bottom");
										childTop = target.getBounds(dp2px(shadowOffset)).bottom + lp.topMargin - lp.bottomMargin;
										break;
									case LayoutParams.TOP:
										Log.d(TAG, "right|top");
										childTop =
												target.getBounds(dp2px(shadowOffset)).top - height + lp.topMargin - lp.bottomMargin;
										break;
									default:
										Log.d(TAG, "right");
										childTop = target.getPoint().y - height / 2 + lp.topMargin - lp.bottomMargin;
								}
								break;
							case LayoutParams.LEFT:
								childLeft = target.getBounds(dp2px(shadowOffset)).left - width + lp.leftMargin - lp.rightMargin;
								switch (lp.getVertical()) {
									case LayoutParams.BOTTOM:
										Log.d(TAG, "left|bottom");
										childTop = target.getBounds(dp2px(shadowOffset)).bottom + lp.topMargin - lp.bottomMargin;
										break;
									case LayoutParams.TOP:
										Log.d(TAG, "left|top");
										childTop =
												target.getBounds(dp2px(shadowOffset)).top - height + lp.topMargin - lp.bottomMargin;
										break;
									default:
										Log.d(TAG, "left");
										childTop = target.getPoint().y - height / 2 + lp.topMargin - lp.bottomMargin;
								}
								break;
							default:
								//without horizontal offset,horizontal set center
								childLeft = target.getPoint().x - width / 2 + lp.leftMargin - lp.rightMargin;
								switch (lp.getVertical()) {
									case LayoutParams.BOTTOM:
										Log.d(TAG, "bottom");
										childTop = target.getBounds(dp2px(shadowOffset)).bottom + lp.topMargin - lp.bottomMargin;
										break;
									case LayoutParams.TOP:
										Log.d(TAG, "top");
										childTop =
												target.getBounds(dp2px(shadowOffset)).top - height + lp.topMargin - lp.bottomMargin;
										break;
									case LayoutParams.ABOVE:
									default:
										//正上方
										childTop = target.getPoint().y - height / 2 + lp.topMargin - lp.bottomMargin;

								}
								//考虑gravity
						}
					}
				}
				final int layoutDirection = getLayoutDirection();
				final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
				final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
				switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
					case Gravity.CENTER_HORIZONTAL:
						Log.d(TAG, "gravity-centerHorizontal");
						childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
								lp.leftMargin - lp.rightMargin;
						break;
					case Gravity.RIGHT:
						Log.d(TAG, "gravity-right");
						if (!forceLeftGravity) {
							childLeft = parentRight - width - lp.rightMargin;
							break;
						}
					case Gravity.LEFT:
						Log.d(TAG, "gravity-left");
						childLeft = parentLeft + lp.leftMargin;
						break;
					default:
						if (-1 == anchor) {
							childLeft = parentLeft + lp.leftMargin;
						}
				}

				switch (verticalGravity) {
					case Gravity.TOP:
						Log.d(TAG, "gravity-top");
						childTop = parentTop + lp.topMargin;
						break;
					case Gravity.CENTER_VERTICAL:
						Log.d(TAG, "gravity-centerVertical");
						childTop = parentTop + (parentBottom - parentTop - height) / 2 +
								lp.topMargin - lp.bottomMargin;
						break;
					case Gravity.BOTTOM:
						Log.d(TAG, "gravity-bottom");
						childTop = parentBottom - height - lp.bottomMargin;
						break;
					default:
						if (-1 == anchor) {
							childTop = parentTop + lp.topMargin;
						}
				}

				child.layout(childLeft, childTop, childLeft + width, childTop + height);
			}
		}
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int w = MeasureSpec.getSize(widthMeasureSpec);
		final int h = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(w, h);
		final int count = getChildCount();
		View child;
		for (int i = 0; i < count; i++) {
			child = getChildAt(i);
			if (child != null) {
				LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (lp == null) {
					child.setLayoutParams(lp);
				}
				measureChild(child, w + MeasureSpec.AT_MOST, h + MeasureSpec.AT_MOST);
			}
		}
	}

	private void init(Context context, AttributeSet attrs) {
		this.mContext = context;
		paint.setAntiAlias(true);
		if (null != attrs) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaskView);
			color = typedArray.getColor(R.styleable.MaskView_mask_shadow_color, SHADOW_COLOR_DEFAULT);
			typedArray.recycle();
		}
		currentOrientation = getResources().getConfiguration().orientation;
		paint.setColor(color);
	}

	public enum MaskViewType {
		INNER_CIRCLE(1),
		OUTER_CIRCLE(2),
		REAL_DOUBLE_CIRCLE(3),
		LITTLE_DOUBLE_CIRCLE(4),
		INNER_DOUBLE_CIRCLE(5),
		DOUBLE_CIRCLE(6),
		OVAL(7),
		RECT(8),
		X(9),
		Y(10),
		HEART(11);

		MaskViewType(int cmd) {
			this.cmd = (byte) cmd;
		}

		private byte cmd;

		public byte getCmd() {
			return cmd;
		}

		public static MaskViewType getValue(byte cmd) {
			for (MaskViewType type : values()) {
				if (type.getCmd() == cmd) {
					return type;
				}
			}
			return null;
		}
	}

	private ObjectAnimator showAnimation = null;

	private ObjectAnimator dismissAnimation = null;

	private long duration = 1000;

	private ViewGroup containerView;

	public void show(FrameLayout containerView, long animationDuration) {
		this.containerView = containerView;
		show(animationDuration);
	}

	/**
	 * @param animationDuration
	 */
	public void show(long animationDuration) {
		this.duration = animationDuration;
		if (null != getParent()) {
			((ViewGroup) getParent()).removeView(this);
		}
		if (mContext instanceof Activity) {
			if (null != containerView) {
				containerView.addView(this);
			} else {
				((ViewGroup) ((Activity) mContext).getWindow().getDecorView()).addView(this);
			}
			if (null == showAnimation) {
				showAnimation = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
				showAnimation.setDuration(duration).addListener(new Animator.AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animator) {
					}

					@Override
					public void onAnimationEnd(Animator animator) {
						if (!hasClicked) {
							dismissAfterDelay();
						}
					}

					@Override
					public void onAnimationCancel(Animator animator) {
					}

					@Override
					public void onAnimationRepeat(Animator animator) {
					}
				});
			}
			showAnimation.start();
		}
	}

	/**
	 *
	 */
	private void dismissAfterDelay() {
		if (null == dismissAnimation) {
			dismissAnimation = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
			dismissAnimation.setDuration(duration).addListener(new Animator.AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animator) {
				}

				@Override
				public void onAnimationEnd(Animator animator) {
					if (!hasClicked) {
						detachItselfFromParent();
						if (null != dismissListener && !dismissListenerHasInvoked) {
							dismissListenerHasInvoked = true;
							dismissListener.onDismiss();
						}
					}
				}

				@Override
				public void onAnimationCancel(Animator animator) {
				}

				@Override
				public void onAnimationRepeat(Animator animator) {
				}
			});
		}
		dismissAnimation.setStartDelay(5000);
		dismissAnimation.start();
	}

	public static class LayoutParams extends LinearLayout.LayoutParams {

		public int getHorizontal() {
			return anchor & HORIZONTAL;
		}

		public int getVertical() {
			return anchor & VERTICAL;
		}

		public static final int LEFT = 0x0001;
		public static final int RIGHT = 0x0002;

		public static final int HORIZONTAL = LEFT | RIGHT;

		public static final int TOP = 0x0004;
		public static final int BOTTOM = 0x0008;

		/**
		 * 正上方
		 */
		public static final int ABOVE = 0x0010;

		public static final int VERTICAL = TOP | BOTTOM;

		public int anchor = -1;

		public int anchorId = View.NO_ID;

		public int anchorParentId = View.NO_ID;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MaskView_Layout);
			anchor = a.getInt(R.styleable.MaskView_Layout_mask_layout_anchor, -1);
			anchorId = a.getResourceId(R.styleable.MaskView_Layout_mask_layout_anchorId, View.NO_ID);
			anchorParentId = a.getResourceId(R.styleable.MaskView_Layout_mask_layout_anchorParentId, View.NO_ID);
			a.recycle();
		}
	}

	public int dp2px(float value) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
	}

	public interface DismissListener {

		void onDismiss();
	}

	public void setAnchorRect(Rect anchorRect) {
		this.anchorRect = anchorRect;
	}

	public void setAnchorView(View anchorView) {
		this.anchorView = anchorView;
	}
}
