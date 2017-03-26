package com.chiao.pattern.utils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.lang.ref.WeakReference;

/**
 * Created by jiao on 16/5/20.
 */
public abstract class ScheduleCountTimer {

	public ScheduleCountTimer(long mInterval) {
		this.mInterval = mInterval;
	}

	private static final int MSG = 1;

	/**
	 * boolean representing if the timer was cancelled
	 */
	private boolean mCancelled = false;

	public synchronized void setInterval(long mInterval) {
		this.mInterval = mInterval;
	}

	private long mInterval = 10000;

	private ScheduleCountTimerHandler mHandler = new ScheduleCountTimerHandler(this);

	/**
	 * Cancel the countdown.
	 */
	public synchronized final void cancel() {
		mCancelled = true;
		mHandler.removeMessages(MSG);
	}

	public abstract void onTick();

	/**
	 * Callback fired when the time is up.
	 */
	public abstract void onIntervalError();

	/**
	 * Start
	 */
	public synchronized final ScheduleCountTimer start() {
		mCancelled = false;
		if (mInterval <= 0) {
			onIntervalError();
			return this;
		}
		mHandler.sendMessage(mHandler.obtainMessage(MSG));
		return this;
	}

	private static class ScheduleCountTimerHandler extends Handler {

		/**
		 * WeakReference of the outer class CountDownTimer.
		 */
		private final WeakReference<ScheduleCountTimer> scheduleCountTimerWeakReference;

		public ScheduleCountTimerHandler(ScheduleCountTimer scheduleCountTimer) {
			scheduleCountTimerWeakReference = new WeakReference<ScheduleCountTimer>(scheduleCountTimer);
		}

		@Override
		public void handleMessage(Message msg) {
			ScheduleCountTimer scheduleCountTimer = scheduleCountTimerWeakReference.get();
			if (scheduleCountTimer == null) {
				return;
			}
			synchronized (scheduleCountTimer) {
				if (scheduleCountTimer.mCancelled) {
					return;
				}
				if (scheduleCountTimer.mInterval <= 0) {
					scheduleCountTimer.onIntervalError();
					return;
				}
				long lastTickStart = SystemClock.elapsedRealtime();
				scheduleCountTimer.onTick();
				// take into account user's onTick taking time to execute
				long delay = lastTickStart + scheduleCountTimer.mInterval - SystemClock.elapsedRealtime();

				// special case: user's onTick took more than interval to
				// complete, skip to next interval
				while (delay < 0)
					delay += scheduleCountTimer.mInterval;

				sendMessageDelayed(obtainMessage(MSG), delay);

			}
		}

	}

}
