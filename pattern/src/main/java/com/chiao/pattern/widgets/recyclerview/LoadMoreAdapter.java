package com.chiao.pattern.widgets.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.chiao.pattern.R;
import com.chiao.pattern.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年11月07日
 */
public abstract class LoadMoreAdapter<V extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter {

	private static final int TYPE_FOOTER = 10086;

	private boolean isLoadMore = false;

	public List<T> getList() {
		return list;
	}

	private List<T> list = new ArrayList<>();

	public T getItem(int position) {
		if (position >= 0 && position < CollectionUtil.size(list)) {
			return list.get(position);
		}
		return null;
	}

	public void setList(List<T> list) {
		if (CollectionUtil.isNotEmpty(list)) {
			this.list = list;
			notifyDataSetChanged();
		}
	}

	public void addList(List<T> list) {
		if (CollectionUtil.isNotEmpty(list)) {
			int startPosition = CollectionUtil.size(this.list);
			this.list.addAll(list);
			notifyItemRangeInserted(startPosition, CollectionUtil.size(list));
		}
	}

	protected boolean isLoadMore() {
		return isLoadMore;
	}

	protected void setLoadMore(boolean loadMore) {
		isLoadMore = loadMore;
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_FOOTER) {
			FootViewHolder viewHolder = new FootViewHolder(
					LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_item_footer, parent, false));
			return viewHolder;
		}
		return onCreateMyViewHolder(parent, viewType);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof FootViewHolder) {
			FootViewHolder footViewHolder = (FootViewHolder) holder;
		} else {
			onBindMyViewHolder((V) holder, position);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (isLoadMore()) {
			if (position == CollectionUtil.size(list)) {
				return TYPE_FOOTER;
			}
		}
		return getMyItemType(position);
	}

	@Override
	public int getItemCount() {
		if (isLoadMore()) {
			return CollectionUtil.size(list) + 1;
		} else {
			return CollectionUtil.size(list);
		}
	}

	public abstract V onCreateMyViewHolder(ViewGroup parent, int viewType);

	public abstract void onBindMyViewHolder(V holder, int position);

	public abstract int getMyItemType(int position);

}
