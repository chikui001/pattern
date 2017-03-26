package com.chiao.pattern.widgets.recyclerview;

import android.view.View;

/**
 * Created by jiao on 16/3/24.
 */
public interface CenterScrollListener {

	void onScroll(int[] leftRightPosition, View itemView);
	//小时滑动的时候，引起天滚动，天被动滚动的时候，不会执行onScrollStop,所以要主动刷新

	//滑动柱状图的时候，不能通过onBindViewHolder设置model颜色
	void onScrollStop(int position);

	void onScrollStopInit(int position);
}
