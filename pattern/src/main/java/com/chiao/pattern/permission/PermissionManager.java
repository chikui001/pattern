package com.chiao.pattern.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.chiao.pattern.utils.CollectionUtil;
import com.chiao.pattern.utils.ContextUtil;
import com.chiao.pattern.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 第一次下次为这个权限请求requestPermissions时，拒绝授权前被用户勾选了。shouldShowRequestPermissionRationale ＝ true
 * 第二次requestPermissions时，会多一个”不再提醒“的勾选框，如果勾选了shouldShowRequestPermissionRationale ＝ true
 * 第三次requestPermissions时，啥也不提示，结果就是，app啥都不干。shouldShowRequestPermissionRationale ＝ false
 * <p/>
 * 当shouldShowRequestPermissionRationale ＝ false时，弹自己定义的对话框
 * 为了防止上面的情况，
 * Created by jiao on 16/5/18.
 */
@TargetApi(23)
public class PermissionManager {

	public static final int REQUEST_CODE_PERMISSION_LOCATION = 0x0107;

	public static final int REQUEST_CODE_PERMISSION_STORAGE = 0x0108;

	public static final int REQUEST_CODE_PERMISSION_PHONE = 0x0109;

	public static final int REQUEST_CODE_PERMISSION_CAMERA = 0x010A;

	public static final int REQUEST_CODE_PERMISSION_MICROPHONE = 0x010B;
	//未被授权的permission--默认都没有授权
	private List<String> permissionGroups = new ArrayList<String>();

	private Context context;

	public PermissionManager(Context context) {
		this.context = context;
		if (ContextUtil.isAndroidM()) {
			permissionGroups.add(Manifest.permission_group.LOCATION);
			permissionGroups.add(Manifest.permission_group.STORAGE);
			permissionGroups.add(Manifest.permission_group.PHONE);
			permissionGroups.add(Manifest.permission_group.CAMERA);
			permissionGroups.add(Manifest.permission_group.MICROPHONE);
		}
	}

	/**
	 * 在FlashActivity中必须调用这个方法
	 * you must invoke this method before use PermissionManager
	 * 检查所有的权限，返回未授权的权限
	 *
	 * @return
	 */
	public List<String> checkUnauthorizedPermission() {
		if (ContextUtil.isAndroidM()) {
			//位置信息
			List<String> permissions = checkPermissionLocation(context);
			if (CollectionUtil.isEmpty(permissions)) {
				permissionGroups.remove(Manifest.permission_group.LOCATION);
			}
			//存储空间
			permissions = checkPermissionStorage(context);
			if (CollectionUtil.isEmpty(permissions)) {
				permissionGroups.remove(Manifest.permission_group.STORAGE);
			}
			//电话
			permissions = checkPermissionPhone(context);
			if (CollectionUtil.isEmpty(permissions)) {
				permissionGroups.remove(Manifest.permission_group.PHONE);
			}
			//相机
			permissions = checkPermissionCamera(context);
			if (CollectionUtil.isEmpty(permissions)) {
				permissionGroups.remove(Manifest.permission_group.CAMERA);
			}
			//麦克风
			permissions = checkPermissionMicroPhone(context);
			if (CollectionUtil.isEmpty(permissions)) {
				permissionGroups.remove(Manifest.permission_group.MICROPHONE);
			}
			return permissionGroups;
		}
		return null;
	}

	/***
	 * 检查位置权限
	 */
	public static List<String> checkPermissionLocation(Context context) {
		if (ContextUtil.isAndroidM()) {
			List<String> list = new ArrayList<String>(2);
			//位置信息
			boolean findLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED;
			boolean coarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
					== PackageManager.PERMISSION_GRANTED;
			if (!findLocation) {
				list.add(Manifest.permission.ACCESS_FINE_LOCATION);
			}
			if (!coarseLocation) {
				list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
			}
			boolean location = findLocation && coarseLocation;
			if (!location) {
				return list;
			}
		}
		return null;
	}

	/**
	 * 检查存储权限
	 */
	public static List<String> checkPermissionStorage(Context context) {
		if (ContextUtil.isAndroidM()) {
			List<String> list = new ArrayList<String>(2);
			//存储空间
			boolean readStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
					== PackageManager.PERMISSION_GRANTED;
			boolean writeStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
					== PackageManager.PERMISSION_GRANTED;
			if (!readStorage) {
				list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			}
			if (!writeStorage) {
				list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}
			boolean storage = readStorage && writeStorage;
			if (!storage) {
				return list;
			}
		}
		return null;
	}

	/**
	 * 检查电话权限
	 */
	public static List<String> checkPermissionPhone(Context context) {
		if (ContextUtil.isAndroidM()) {
			List<String> list = new ArrayList<String>(1);
			//电话
			boolean callPhone =
					ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
			if (!callPhone) {
				list.add(Manifest.permission.CALL_PHONE);
			}
			if (!callPhone) {
				return list;
			}
		}
		return null;
	}

	/**
	 * 检查相机权限
	 */
	public static List<String> checkPermissionCamera(Context context) {
		if (ContextUtil.isAndroidM()) {
			List<String> list = new ArrayList<String>(1);
			//相机
			boolean camera =
					ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
			if (!camera) {
				list.add(Manifest.permission.CAMERA);
			}
			if (!camera) {
				return list;
			}
		}
		return null;
	}

	public static List<String> checkPermissionMicroPhone(Context context) {
		if (ContextUtil.isAndroidM()) {
			List<String> list = new ArrayList<String>(1);
			//麦克风
			boolean microPhone = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
					== PackageManager.PERMISSION_GRANTED;
			if (!microPhone) {
				list.add(Manifest.permission.RECORD_AUDIO);
			}
			if (!microPhone) {
				return list;
			}
		}
		return null;
	}

	public static void requestPermission(Activity activity, String permissionGroup) {
		if (null != activity && StringUtil.isNotEmpty(permissionGroup)) {
			if (Manifest.permission_group.LOCATION.equals(permissionGroup)) {
				List<String> list = checkPermissionLocation(activity);
				if (CollectionUtil.isNotEmpty(list)) {
					ActivityCompat.requestPermissions(activity, CollectionUtil.convertListToArray(list),
													  REQUEST_CODE_PERMISSION_LOCATION);
				}
			} else if (Manifest.permission_group.STORAGE.equals(permissionGroup)) {
				List<String> list = checkPermissionStorage(activity);
				if (CollectionUtil.isNotEmpty(list)) {
					ActivityCompat
							.requestPermissions(activity, CollectionUtil.convertListToArray(list), REQUEST_CODE_PERMISSION_STORAGE);
				}

			} else if (Manifest.permission_group.PHONE.equals(permissionGroup)) {
				List<String> list = checkPermissionPhone(activity);
				if (CollectionUtil.isNotEmpty(list)) {
					ActivityCompat
							.requestPermissions(activity, CollectionUtil.convertListToArray(list), REQUEST_CODE_PERMISSION_PHONE);
				}

			} else if (Manifest.permission_group.CAMERA.equals(permissionGroup)) {
				List<String> list = checkPermissionCamera(activity);
				if (CollectionUtil.isNotEmpty(list)) {
					ActivityCompat
							.requestPermissions(activity, CollectionUtil.convertListToArray(list), REQUEST_CODE_PERMISSION_CAMERA);
				}

			} else if (Manifest.permission_group.MICROPHONE.equals(permissionGroup)) {
				List<String> list = checkPermissionMicroPhone(activity);
				if (CollectionUtil.isNotEmpty(list)) {
					ActivityCompat.requestPermissions(activity, CollectionUtil.convertListToArray(list),
													  REQUEST_CODE_PERMISSION_MICROPHONE);
				}

			}
		}
	}

	/**
	 * Checks all given permissions have been granted.
	 *
	 * @param grantResults results
	 * @return returns true if all permissions have been granted.
	 */
	public static boolean verifyPermissions(int... grantResults) {
		if (grantResults.length == 0) {
			return false;
		}
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks given permissions are needed to show rationale.
	 *
	 * @param activity    activity
	 * @param permissions permission list
	 * @return returns true if one of the permission is needed to show rationale.
	 */
	public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
		for (String permission : permissions) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * remove permission which was authorized
	 */
	public static void removeAuthorizedPermission(List<String> list, int reqCode) {
		switch (reqCode) {
			case REQUEST_CODE_PERMISSION_LOCATION:
				list.remove(Manifest.permission_group.LOCATION);
				break;
			case REQUEST_CODE_PERMISSION_STORAGE:
				list.remove(Manifest.permission_group.STORAGE);
				break;
			case REQUEST_CODE_PERMISSION_PHONE:
				list.remove(Manifest.permission_group.PHONE);
				break;
			case REQUEST_CODE_PERMISSION_CAMERA:
				list.remove(Manifest.permission_group.CAMERA);
				break;
			case REQUEST_CODE_PERMISSION_MICROPHONE:
				list.remove(Manifest.permission_group.MICROPHONE);
				break;

		}
	}
}
