package com.chiao.pattern.widgets.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.chiao.pattern.R;

public class FootViewHolder extends RecyclerView.ViewHolder {

	public CircleProgressBar progressBar;

	public FootViewHolder(View itemView) {
		super(itemView);
		progressBar = (CircleProgressBar) itemView.findViewById(R.id.progressBar);
	}
}