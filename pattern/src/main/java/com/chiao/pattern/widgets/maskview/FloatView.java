package com.chiao.pattern.widgets.maskview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 控件上浮现的view
 * Created by jiao on 16/9/28.
 */

public class FloatView extends ViewGroup {

	private static final String TAG = "FloatView";
	private View anchorView;

	public void setAnchorView(View anchorView) {
		this.anchorView = anchorView;
	}

	/**
	 * 安全套的理想距离
	 */
	private static final int SHADOW_OFFSET_DEFAULT = 10;
	private int shadowOffset = SHADOW_OFFSET_DEFAULT;

	public void setShadowOffset(int shadowOffset) {
		this.shadowOffset = shadowOffset;
	}

	private Context mContext;

	private View rootView = null;

	private Map<Long, ViewTarget> targetViews = new HashMap<>();

	public FloatView(Context context) {
		this(context, null);
	}

	public FloatView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(21)
	public FloatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	@Override
	public void addView(View child) {
		if (getChildCount() > 0) {
			throw new IllegalStateException("FloatView can host only one direct child");
		}
		super.addView(child);
	}

	@Override
	public void addView(View child, int index) {
		if (getChildCount() > 0) {
			throw new IllegalStateException("FloatView can host only one direct child");
		}
		super.addView(child, index);
	}

	@Override
	public void addView(View child, int width, int height) {
		if (getChildCount() > 0) {
			throw new IllegalStateException("FloatView can host only one direct child");
		}
		super.addView(child, width, height);
	}

	@Override
	public void addView(View child, LayoutParams params) {
		if (getChildCount() > 0) {
			throw new IllegalStateException("FloatView can host only one direct child");
		}
		super.addView(child, params);
	}

	@Override
	public void addView(View child, int index, LayoutParams params) {
		if (getChildCount() > 0) {
			throw new IllegalStateException("FloatView can host only one direct child");
		}
		super.addView(child, index, params);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		rootView = getRootView();
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
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				final MaskView.LayoutParams lp = (MaskView.LayoutParams) child.getLayoutParams();
				final int width = child.getMeasuredWidth();
				final int height = child.getMeasuredHeight();

				int childLeft = 0;
				int childTop = 0;

				int gravity = lp.gravity;
				int anchor = lp.anchor;

				if (-1 != anchor) {
					ViewTarget viewTarget = null;
					if (null != anchorView) {
						viewTarget = new ViewTarget(anchorView);
					} else {
						long viewId = lp.anchorId;
						long viewParentId = lp.anchorParentId;
						long id = viewParentId << 32 | viewId;
						if (viewId == View.NO_ID) {
							continue;
						}
						viewTarget = (View.NO_ID == viewParentId) ? targetViews.get(viewId) : targetViews.get(id);
						if (null == viewTarget) {
							if (View.NO_ID == viewParentId) {
								View targetView = rootView.findViewById((int) viewId);
								if (null != targetView) {
									viewTarget = new ViewTarget(targetView);
									targetViews.put(id, viewTarget);
								}
							} else {
								View parentView = rootView.findViewById((int) viewParentId);
								if (null != parentView) {
									View targetView = parentView.findViewById((int) viewId);
									if (null != targetView) {
										viewTarget = new ViewTarget(targetView);
										targetViews.put(id, viewTarget);
									}
								}
							}
						}
					}

					if (null != viewTarget) {
						switch (lp.getHorizontal()) {
							case MaskView.LayoutParams.RIGHT:
								childLeft = viewTarget.getBounds(dp2px(shadowOffset)).right + lp.leftMargin - lp.rightMargin;
								switch (lp.getVertical()) {
									case MaskView.LayoutParams.BOTTOM:
										childTop =
												viewTarget.getBounds(dp2px(shadowOffset)).bottom + lp.topMargin - lp.bottomMargin;
										break;
									case MaskView.LayoutParams.TOP:
										childTop = viewTarget.getBounds(dp2px(shadowOffset)).top - height + lp.topMargin
												- lp.bottomMargin;
										break;
									default:
										childTop = viewTarget.getPoint().y - height / 2 + lp.topMargin - lp.bottomMargin;
								}
								break;
							case MaskView.LayoutParams.LEFT:
								childLeft = viewTarget.getBounds(dp2px(shadowOffset)).left - width + lp.leftMargin - lp.rightMargin;
								switch (lp.getVertical()) {
									case MaskView.LayoutParams.BOTTOM:
										childTop =
												viewTarget.getBounds(dp2px(shadowOffset)).bottom + lp.topMargin - lp.bottomMargin;
										break;
									case MaskView.LayoutParams.TOP:
										childTop = viewTarget.getBounds(dp2px(shadowOffset)).top - height + lp.topMargin
												- lp.bottomMargin;
										break;
									default:
										childTop = viewTarget.getPoint().y - height / 2 + lp.topMargin - lp.bottomMargin;
								}
								break;
							default:
								//without horizontal offset,horizontal set center
								childLeft = viewTarget.getPoint().x - width / 2 + lp.leftMargin - lp.rightMargin;
								switch (lp.getVertical()) {
									case MaskView.LayoutParams.BOTTOM:
										childTop =
												viewTarget.getBounds(dp2px(shadowOffset)).bottom + lp.topMargin - lp.bottomMargin;
										break;
									case MaskView.LayoutParams.TOP:
										childTop = viewTarget.getBounds(dp2px(shadowOffset)).top - height + lp.topMargin
												- lp.bottomMargin;
										break;
									case MaskView.LayoutParams.ABOVE:
									default:
										//正上方
										childTop = viewTarget.getPoint().y - height / 2 + lp.topMargin - lp.bottomMargin;

								}
								//考虑gravity
						}
					}
				}
				final int parentLeft = getPaddingLeft();
				final int parentRight = r - l - getPaddingRight();

				final int parentTop = getPaddingTop();
				final int parentBottom = b - t - getPaddingBottom();

				final int layoutDirection = getLayoutDirection();
				final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
				final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
				switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
					case Gravity.CENTER_HORIZONTAL:
						childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
								lp.leftMargin - lp.rightMargin;
						break;
					case Gravity.RIGHT:
						if (!forceLeftGravity) {
							childLeft = parentRight - width - lp.rightMargin;
							break;
						}
					case Gravity.LEFT:
						childLeft = parentLeft + lp.leftMargin;
						break;
					default:
						if (-1 == anchor) {
							childLeft = parentLeft + lp.leftMargin;
						}
				}

				switch (verticalGravity) {
					case Gravity.TOP:
						childTop = parentTop + lp.topMargin;
						break;
					case Gravity.CENTER_VERTICAL:
						childTop = parentTop + (parentBottom - parentTop - height) / 2 +
								lp.topMargin - lp.bottomMargin;
						break;
					case Gravity.BOTTOM:
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
		return new MaskView.LayoutParams(getContext(), attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getChildCount() > 0) {
			final View child = getChildAt(0);
			final int measuredHeight = getMeasuredHeight();
			if (child.getMeasuredHeight() < measuredHeight) {
				final int widthPadding;
				final int heightPadding;
				final MaskView.LayoutParams lp = (MaskView.LayoutParams) child.getLayoutParams();
				widthPadding = getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin;
				heightPadding = getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin;

				final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, widthPadding, lp.width);
				final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, heightPadding, lp.height);
				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

				int specModeW = MeasureSpec.getMode(widthMeasureSpec);
				int specSizeW = MeasureSpec.getSize(widthMeasureSpec);
				int specModeH = MeasureSpec.getMode(heightMeasureSpec);
				int specSizeH = MeasureSpec.getSize(heightMeasureSpec);
				int width = 0;

				switch (specModeW) {
					case MeasureSpec.EXACTLY:
						width = specSizeW;
						break;
					case MeasureSpec.UNSPECIFIED:
						width = child.getMeasuredWidth() + widthPadding;
						break;
					case MeasureSpec.AT_MOST:
						width = Math.min(child.getMeasuredWidth() + widthPadding, specSizeW);
						break;
				}
				int height = 0;

				switch (specModeH) {
					case MeasureSpec.EXACTLY:
						height = specSizeH;
						break;
					case MeasureSpec.UNSPECIFIED:
						height = child.getMeasuredHeight() + heightPadding;
						break;
					case MeasureSpec.AT_MOST:
						height = Math.min(child.getMeasuredHeight() + heightPadding, specSizeH);
						break;
				}
				setMeasuredDimension(width, height);
			}
		}
	}

	private void init(Context context, AttributeSet attrs) {
		this.mContext = context;
	}

	private ObjectAnimator showAnimation = null;

	private long duration = 1000;

	private ViewGroup containerView;

	public void show(FrameLayout containerView, ObjectAnimator showAnimation) {
		this.containerView = containerView;
		this.showAnimation = showAnimation;
		show();
	}

	public void show(ObjectAnimator showAnimation) {
		this.showAnimation = showAnimation;
		show();
	}

	public void show(FrameLayout containerView, long animationDuration) {
		this.containerView = containerView;
		this.duration = animationDuration;
		show();
	}

	public void dismiss() {
		if (null != getParent()) {
			((ViewGroup) getParent()).removeView(this);
		}
	}

	/**
	 *
	 */
	public void show() {
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

	public int dp2px(float value) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
	}
}
