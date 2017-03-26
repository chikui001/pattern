package com.chiao.test;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年11月29日
 */
public enum BluetoothConnectGuideType {
	/**
	 * 设备没电了
	 */
	LOW_BATTERY(1),
	/**
	 * android需要开启定位才能扫描到
	 */
	LOCATION(8),
	/**
	 * 点击设置
	 */
	CONNECTED_BY_OTHER_APP(2),
	/**
	 * 绑定的不是当前'附近'要连接的
	 */
	TARGET_DEVICE_NOT_BIND_DEVICE(3),
	/**
	 * 设置中的蓝牙已经连接的设备list
	 */
	CONNECTED_BY_OTHER_PHONE(4),

	/**
	 * 连接失败，将萌动放在充电器上reset
	 */
	RESET(5),
	/**
	 * 重启蓝牙
	 */
	REBOOT_BLUETOOTH(6),
	/**
	 * 重启手机
	 */
	REBOOT_PHONE(9),
	/**
	 * 关闭wifi
	 */
	STOP_WIFI(10),;

	private int type;

	BluetoothConnectGuideType(int type) {
		this.type = type;
	}
}
