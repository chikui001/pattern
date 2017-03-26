package com.chiao.pattern.permission;

/**
 * Created by jiao on 16/5/19.
 */
public interface PermissionInterface {

	void onPermissionManagerAuthorized(int requestCode);

	void onPermissionDenied();
}
