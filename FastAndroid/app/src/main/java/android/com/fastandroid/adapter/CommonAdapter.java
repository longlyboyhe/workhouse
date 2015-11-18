package android.com.fastandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前类注释:自定义公共的Adapter，只需要传入布局文件id和需要展示的数据
 * 项目名：FastAndroid
 * 包名：android.com.fastandroid.adapter
 * 作者：longlyboyhe on 15/10/23 08:41
 * 邮箱：longlyboyhe@126.com
 * QQ： 1462780453
 * 公司：技术猿
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas = new ArrayList<T>();
	protected final int mItemLayoutId;

	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	/*
	 * 分页时添加数据
	 * 
    */	
	public void addData(List<T> mDatas) {
		this.mDatas.addAll(mDatas);
		notifyDataSetChanged();
	}
	
	/*
	 * 设置或者更新添加数据
	 * 
    */	
	public void setData(List<T> mDatas) {
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();

	}

	public abstract void convert(ViewHolder helper, T item);

	private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}

}
