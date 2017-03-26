package com.chiao.pattern.widgets.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

public class GiftADialog extends AdDialog {

	public GiftADialog(Context context) {
		super(context);
	}

	@Override
	public AnimatorSet onCreateShowAnimationSet(View rootView) {
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(ObjectAnimator.ofFloat(rootView, "scaleX", 0.1f, 0.475f, 1),
								 ObjectAnimator.ofFloat(rootView, "scaleY", 0.1f, 0.475f, 1),
								 ObjectAnimator.ofFloat(rootView, "translationY", -1000, -200, 0),
								 ObjectAnimator.ofFloat(rootView, "alpha", 0, 1, 1));
		animatorSet.setDuration(1000);
		return animatorSet;
	}

	@Override
	public AnimatorSet onCreateDismissAnimationSet(View rootView) {
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(ObjectAnimator.ofFloat(rootView, "scaleX", 1f, 0.2f),
								 ObjectAnimator.ofFloat(rootView, "scaleY", 1f, 0.2f),
								 ObjectAnimator.ofFloat(rootView, "translationY", 0, 600),
								 ObjectAnimator.ofFloat(rootView, "translationX", 0, 400),
								 ObjectAnimator.ofFloat(rootView, "alpha", 1, 0.5f));
		animatorSet.setDuration(1000);
		return animatorSet;
	}
}