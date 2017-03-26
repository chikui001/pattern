// Copyright (c) 2015. ExtantFuture Inc. All Rights Reserved.
package com.chiao.pattern.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;
import com.chiao.pattern.permission.PermissionInterface;
import com.chiao.pattern.permission.PermissionManager;
import com.chiao.pattern.utils.StringUtil;

import java.util.List;

/**
 * @author Rambo, <rambo@extantfuture.com>
 * @date 2015年10月16日
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener, PermissionInterface {

	protected Context mContext;

	/**
	 * fragment able to commit
	 */
	public boolean mFragmentAbleToCommit = true;

	/**
	 * 有的话，不弹
	 *
	 * @param dialogFragment
	 * @param tag
	 * @return
	 */
	public boolean showDialogFragmentReplace(@NonNull DialogFragment dialogFragment, @NonNull String tag) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment instanceof DialogFragment && fragment.isAdded()) {
			return true;
		}
		if (mFragmentAbleToCommit) {
			dialogFragment.show(fragmentManager, tag);
			return true;
		}
		return false;
	}

	/**
	 * 有的话，不弹
	 *
	 * @param dialogFragment
	 * @param tag
	 * @return
	 */
	public boolean showDialogFragmentJustOne(@NonNull DialogFragment dialogFragment, @NonNull String tag) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		List<Fragment> lists = fragmentManager.getFragments();
		for (int i = 0; i < lists.size(); i++) {
			Fragment fragment = lists.get(i);
			if (fragment instanceof DialogFragment && fragment.isVisible()) {
				return false;
			}
		}
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment instanceof DialogFragment && fragment.isAdded()) {
			return true;
		}
		if (mFragmentAbleToCommit) {
			dialogFragment.show(fragmentManager, tag);
			return true;
		}
		return false;
	}

	/**
	 * 有的话，重新弹一个
	 * 业务互斥
	 * 弹出框是，判断是否已有相同tag弹出框，有的话，dismiss
	 *
	 * @param dialogFragment
	 * @param tag
	 * @return
	 */
	public boolean showDialogFragment(@NonNull DialogFragment dialogFragment, @NonNull String tag) {
		if (mFragmentAbleToCommit) {
			if (null == dialogFragment || StringUtil.isEmpty(tag)) {
				throw new IllegalArgumentException("DialogFragment not null & tag not null");
			}
			FragmentManager fragmentManager = getSupportFragmentManager();
			Fragment fragment = fragmentManager.findFragmentByTag(tag);
			if (fragment instanceof DialogFragment && fragment.isAdded()) {
				((DialogFragment) fragment).dismissAllowingStateLoss();
			}
			dialogFragment.show(fragmentManager, tag);
			return true;
		}
		return false;
	}

	/**
	 * be careful when call it in fragment lifecycle callback
	 * see {link FragmentManagerImpl#execPendingActions()}
	 *
	 * @param dialogFragment
	 * @param tag
	 * @return
	 */
	public boolean showDialogFragmentImmediately(@NonNull DialogFragment dialogFragment, @NonNull String tag) {
		if (mFragmentAbleToCommit) {
			if (null == dialogFragment || StringUtil.isEmpty(tag)) {
				throw new IllegalArgumentException("DialogFragment not null & tag not null");
			}
			FragmentManager fragmentManager = getSupportFragmentManager();
			Fragment fragment = fragmentManager.findFragmentByTag(tag);
			if (fragment instanceof DialogFragment && fragment.isAdded()) {
				((DialogFragment) fragment).dismissAllowingStateLoss();
			}
			dialogFragment.show(fragmentManager, tag);
			try {
				fragmentManager.executePendingTransactions();
			} catch (Exception e) {
			}
			return true;
		}
		return false;
	}

	/**
	 * dismiss
	 */
	public void dismissDialogFragment(@NonNull String tag) {
		if (StringUtil.isEmpty(tag)) {
			throw new IllegalArgumentException();
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment instanceof DialogFragment && fragment.isAdded()) {
			((DialogFragment) fragment).dismissAllowingStateLoss();
		}
	}

	public boolean ismFragmentAbleToCommit() {
		return mFragmentAbleToCommit;
	}

	public LayoutInflater mInflater;

	/**
	 * 当前Activity是否在在最顶端
	 */
	protected boolean mIsFront = false;

	public boolean isFront() {
		return mIsFront;
	}

	protected Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (keepScreenOn()) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		mInflater = getLayoutInflater();
		mFragmentAbleToCommit = true;
		mContext = this;
	}

	protected boolean keepScreenOn() {
		return false;
	}

	/**
	 * make view's clickListener clear
	 *
	 * @param view
	 */
	@Override
	public void onClick(View view) {

	}

	@Override
	protected void onResume() {
		super.onResume();
		mFragmentAbleToCommit = true;
		mIsFront = true;
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		mFragmentAbleToCommit = true;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mFragmentAbleToCommit = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mIsFront = false;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mFragmentAbleToCommit = true;
	}

	@Override
	public void onStateNotSaved() {
		super.onStateNotSaved();
		mFragmentAbleToCommit = true;
	}

	public String tag() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mFragmentAbleToCommit = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//		MengdongApp.getInstance().getRefWatcher().watch(this);
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mFragmentAbleToCommit = false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	/**
	 * 权限被允许
	 */ public void onPermissionManagerAuthorized(int requestCode) {

	}

	@Override
	/**
	 * 权限被拒绝
	 */ public void onPermissionDenied() {

	}

	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (!PermissionManager.verifyPermissions(grantResults)) {
			showWhenRequestPermissionDenied(requestCode);
		} else {
			onPermissionManagerAuthorized(requestCode);
		}
	}

	/**
	 * 权限申请被deny
	 */
	public void showWhenRequestPermissionDenied(final int requestCode) {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				String message = null;
				switch (requestCode) {
					case PermissionManager.REQUEST_CODE_PERMISSION_LOCATION:
						message = "在设置－应用－萌动－权限中开启位置权限，以正常使用萌动";
						break;
					case PermissionManager.REQUEST_CODE_PERMISSION_STORAGE:
						message = "在设置－应用－萌动－权限中开启存储空间权限，以正常使用萌动";
						break;
					case PermissionManager.REQUEST_CODE_PERMISSION_PHONE:
						message = "在设置－应用－萌动－权限中开启电话权限，以正常使用萌动";
						break;
					case PermissionManager.REQUEST_CODE_PERMISSION_CAMERA:
						message = "在设置－应用－萌动－权限中开启相机权限，以正常使用萌动";
						break;
					case PermissionManager.REQUEST_CODE_PERMISSION_MICROPHONE:
						message = "在设置－应用－萌动－权限中开启麦克风权限，以正常使用萌动";
						break;
				}
				AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
				alertDialog.setTitle("权限申请");
				alertDialog.setMessage(message);
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "去设置", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						onPermissionDenied();
						Uri packageURI = Uri.parse("package:" + getPackageName());
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
						startActivity(intent);
					}
				});
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						onPermissionDenied();
					}
				});
				alertDialog.show();
			}
		}, 200);
	}

	public void showToast(int stringId) {
		Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
