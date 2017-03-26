package com.chiao.pattern.widgets.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * 广告，礼包基础dialog
 *
 * @date 2016年11月25日
 */
public abstract class AdDialog extends Dialog {

	private View rootView;
	protected DisplayMetrics mDisplayMetrics;
	private AnimatorSet showAnimatorSet;

	private AnimatorSet dismissAnimatorSet;

	public void setCloseOnTouchOutside(boolean closeOnTouchOutside) {
		this.closeOnTouchOutside = closeOnTouchOutside;
	}

	private boolean closeOnTouchOutside = false;

	public AdDialog(Context context) {
		super(context);
		mDisplayMetrics = context.getResources().getDisplayMetrics();
		setDialogTheme();
	}

	/**
	 * set dialog theme(设置对话框主题)
	 */
	private void setDialogTheme() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
	}

	@Override
	public void dismiss() {
		if (null != dismissAnimatorSet) {
			dismissAnimatorSet.addListener(new Animator.AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					superDismiss();
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					superDismiss();
				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}
			});
			dismissAnimatorSet.start();
		} else {
			super.dismiss();
		}
	}

	private void superDismiss() {
		if (isShowing()) {
			try {
				super.dismiss();
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showAnimatorSet = onCreateShowAnimationSet(rootView);
		dismissAnimatorSet = onCreateDismissAnimationSet(rootView);

	}

	@Override
	public void setContentView(View view) {
		int width = (null != mDisplayMetrics) ? mDisplayMetrics.widthPixels : 0;
		int height = (null != mDisplayMetrics) ? mDisplayMetrics.heightPixels : 0;
		rootView = view;
		rootView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (closeOnTouchOutside) {
					dismiss();
				}
			}
		});
		setContentView(view, new ViewGroup.LayoutParams(width, height));
	}

	public abstract AnimatorSet onCreateShowAnimationSet(View rootView);

	public abstract AnimatorSet onCreateDismissAnimationSet(View rootView);

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (null != showAnimatorSet && null != rootView) {
			showAnimatorSet.start();
		}
	}
}
