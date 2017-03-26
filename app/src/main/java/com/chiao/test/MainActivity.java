package com.chiao.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.chiao.pattern.base.BaseActivity;
import com.chiao.pattern.utils.CollectionUtil;
import com.chiao.pattern.utils.ViewClick;
import com.chiao.pattern.widgets.maskview.MaskView;
import com.chiao.pattern.widgets.recyclerview.RecyclerViewPager;
import com.chiao.pattern.widgets.timeview.CountDownCircleView;
import com.chiao.pattern.widgets.timeview.ReverseDownCircleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

	private List<BluetoothConnectGuideType> list = new ArrayList<>();
	private LinearLayout rootView;
	private CountDownCircleView countDownCircleView;
	private TextSwitcher countDownTextSwitcher;
	private ReverseDownCircleView reverseDownCircleView;
	private TextSwitcher reverseTextSwitcher;

	private int circle = 0;

	private RecyclerViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		countDownTextSwitcher = (TextSwitcher) findViewById(R.id.count_textSwitcher);
		rootView = (LinearLayout) findViewById(R.id.activity_main);
		countDownCircleView = (CountDownCircleView) findViewById(R.id.count_down_view);
		countDownCircleView.start(10000, 30000, new CountDownCircleView.CountDownViewListener() {

			@Override
			public void onPause() {

			}

			@Override
			public void onFinished() {
				countDownTextSwitcher.setText("30000");
			}

			@Override
			public void onTick(long currentTimeInMill, long targetTimeInMill) {
				countDownTextSwitcher.setText(currentTimeInMill + "");
			}
		});
		countDownTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

			@Override
			public View makeView() {
				TextView tv = new TextView(MainActivity.this);
				tv.setTextSize(20);
				// 字体颜色品红
				tv.setTextColor(Color.BLACK);
				return tv;
			}
		});
		reverseTextSwitcher = (TextSwitcher) findViewById(R.id.reverse_textSwitcher);
		reverseTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

			@Override
			public View makeView() {
				TextView tv = new TextView(MainActivity.this);
				tv.setTextSize(20);
				// 字体颜色品红
				tv.setTextColor(Color.BLACK);
				return tv;
			}
		});
		reverseDownCircleView = (ReverseDownCircleView) findViewById(R.id.reverse_down_view);
		reverseDownCircleView.start(7000);
		reverseDownCircleView.setReverseListener(new ReverseDownCircleView.ReverseListener() {

			@Override
			public void onCircle() {
				circle++;
				reverseTextSwitcher.setText(circle + "");
			}
		});

		MaskView maskView = (MaskView) mInflater.inflate(R.layout.maskview_test, rootView, false);
		maskView.addMask(R.id.count_down_view, MaskView.MaskViewType.DOUBLE_CIRCLE);
		maskView.show(1000);

		viewPager = (RecyclerViewPager) findViewById(R.id.recycler_viewPage);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		viewPager.setLayoutManager(linearLayoutManager);
		list.add(BluetoothConnectGuideType.LOW_BATTERY);

		list.add(BluetoothConnectGuideType.LOCATION);

		list.add(BluetoothConnectGuideType.CONNECTED_BY_OTHER_APP);
		list.add(BluetoothConnectGuideType.TARGET_DEVICE_NOT_BIND_DEVICE);
		list.add(BluetoothConnectGuideType.CONNECTED_BY_OTHER_PHONE);
		list.add(BluetoothConnectGuideType.RESET);
		list.add(BluetoothConnectGuideType.REBOOT_BLUETOOTH);

		list.add(BluetoothConnectGuideType.REBOOT_PHONE);
		list.add(BluetoothConnectGuideType.STOP_WIFI);
		viewPager.setCurrentIndex(0);
		viewPager.setNestedScrollingEnabled(false);
		BluetoothGuideAdapter bluetoothGuideAdapter = new BluetoothGuideAdapter(list, new ViewClick() {

			@Override
			public void onClick(int id, RecyclerView.ViewHolder viewHolder) {
				int position = viewHolder.getAdapterPosition();
				BluetoothConnectGuideType type = list.get(position);
				switch (id) {
					case R.id.close_card_ly:
						break;
					case R.id.forward_tv:
						switch (type) {
							case LOW_BATTERY:
								//点击去充电
								break;
							case LOCATION:
								viewPager.scrollToLast();
							case CONNECTED_BY_OTHER_APP:
								viewPager.scrollToLast();
								break;
							case TARGET_DEVICE_NOT_BIND_DEVICE:
								viewPager.scrollToLast();
								break;
							case CONNECTED_BY_OTHER_PHONE:
								viewPager.scrollToLast();
								break;
							case RESET:
								viewPager.scrollToLast();
								break;
							case REBOOT_BLUETOOTH:
								viewPager.scrollToLast();
								break;
							case REBOOT_PHONE:
								viewPager.scrollToLast();
								break;
							case STOP_WIFI:
								viewPager.scrollToLast();
								break;
						}
						break;
					case R.id.backward_tv:
						switch (type) {
							case LOW_BATTERY:
								//点击继续排查
								viewPager.scrollToNext();
								break;
							case LOCATION:
								viewPager.scrollToNext();
								break;
							case CONNECTED_BY_OTHER_APP:
								viewPager.scrollToNext();
								break;
							case TARGET_DEVICE_NOT_BIND_DEVICE:
								viewPager.scrollToNext();
								break;
							case CONNECTED_BY_OTHER_PHONE:
								//点击去设置
								break;
							case RESET:
								viewPager.scrollToNext();
								break;
							case REBOOT_BLUETOOTH:
								viewPager.scrollToNext();
								break;
							case REBOOT_PHONE:
								viewPager.scrollToNext();
								break;
							case STOP_WIFI:
								break;
						}
						break;
				}
			}

			@Override
			public void onClick(int id, RecyclerView.ViewHolder parentHolder, RecyclerView.ViewHolder childHolder) {

			}
		});
		viewPager.setAdapter(bluetoothGuideAdapter);
	}

	private static class BluetoothGuideAdapter extends RecyclerView.Adapter<BluetoothGuideViewHolder> {

		private List<BluetoothConnectGuideType> list = new ArrayList<>();

		private ViewClick clickListener;

		public BluetoothGuideAdapter(List<BluetoothConnectGuideType> list, ViewClick clickListener) {
			this.clickListener = clickListener;
			if (CollectionUtil.isNotEmpty(list)) {
				this.list = list;
			}
		}

		@Override
		public BluetoothGuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
									  .inflate(R.layout.viewpager_item_last_next_step_closeable, parent, false);
			BluetoothGuideViewHolder bluetoothGuideViewHolder = new BluetoothGuideViewHolder(view, clickListener);
			return bluetoothGuideViewHolder;
		}

		@Override
		public void onBindViewHolder(BluetoothGuideViewHolder holder, int position) {
			BluetoothConnectGuideType type = list.get(position);
			switch (type) {
				case LOW_BATTERY:
					holder.backImageView.setImageResource(R.drawable.chongdian_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_low_battery_content);
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText(R.string.bluetooth_dialog_low_battery_forward);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_low_battery_backward);
					break;
				case LOCATION:
					holder.backImageView.setImageResource(R.drawable.dingwei_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_location_content);
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText(R.string.bluetooth_dialog_location_forward);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_location_backward);
					break;
				case CONNECTED_BY_OTHER_APP:
					holder.backImageView.setImageResource(R.drawable.close_app_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_connected_by_others_content);
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText(R.string.bluetooth_dialog_connected_by_others_forward);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_connected_by_others_backward);
					break;
				case TARGET_DEVICE_NOT_BIND_DEVICE:
					holder.backImageView.setImageResource(R.drawable.jiechu_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_not_mine_content);
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText(R.string.bluetooth_dialog_not_mine_forward);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_not_mine_backward);
					break;
				case CONNECTED_BY_OTHER_PHONE:
					holder.backImageView.setImageResource(R.drawable.two_phone_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_by_other_phone_content);
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText(R.string.bluetooth_dialog_by_other_phone_forward);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_by_other_phone_backward);
					break;
				case RESET:
					holder.backImageView.setImageResource(R.drawable.chongdian_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_reset_content);
					holder.forwardTextView.setVisibility(View.GONE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_reset_backward);
					break;
				case REBOOT_BLUETOOTH:
					holder.backImageView.setImageResource(R.drawable.restart_bluetooth_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_reboot_bluetooth_content);
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText(R.string.bluetooth_dialog_reboot_bluetooth_forward);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_reboot_bluetooth_backward);
					break;
				case REBOOT_PHONE:
					holder.backImageView.setImageResource(R.drawable.restart_phone_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_reboot_phone_content);
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText(R.string.bluetooth_dialog_reboot_phone_forward);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_reboot_phone_forward);
					break;
				case STOP_WIFI:
					holder.backImageView.setImageResource(R.drawable.close_router_card);
					holder.contentTextView.setText(R.string.bluetooth_dialog_wifi_content);
					holder.forwardTextView.setVisibility(View.VISIBLE);
					holder.backwardTextView.setVisibility(View.VISIBLE);
					holder.forwardTextView.setText(R.string.bluetooth_dialog_wifi_forward);
					holder.backwardTextView.setText(R.string.bluetooth_dialog_wifi_backward);
					break;
			}
		}

		@Override
		public int getItemCount() {
			return CollectionUtil.size(list);
		}
	}

	private static class BluetoothGuideViewHolder extends RecyclerView.ViewHolder {

		private ImageView backImageView;

		private TextView contentTextView;

		private TextView forwardTextView;

		private TextView backwardTextView;

		private LinearLayout closeCardLinearLayout;

		public BluetoothGuideViewHolder(View itemView, final ViewClick clickListener) {
			super(itemView);
			backImageView = (ImageView) itemView.findViewById(R.id.back_iv);
			contentTextView = (TextView) itemView.findViewById(R.id.content_tv);
			forwardTextView = (TextView) itemView.findViewById(R.id.forward_tv);
			backwardTextView = (TextView) itemView.findViewById(R.id.backward_tv);
			forwardTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					clickListener.onClick(v.getId(), BluetoothGuideViewHolder.this);
				}
			});
			backwardTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					clickListener.onClick(v.getId(), BluetoothGuideViewHolder.this);
				}
			});
			closeCardLinearLayout = (LinearLayout) itemView.findViewById(R.id.close_card_ly);
			closeCardLinearLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					clickListener.onClick(v.getId(), BluetoothGuideViewHolder.this);
				}
			});
		}
	}
}
