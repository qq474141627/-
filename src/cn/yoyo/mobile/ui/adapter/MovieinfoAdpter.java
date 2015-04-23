package cn.yoyo.mobile.ui.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yoyo.mobile.beans.SmallVideoBean;
import cn.yoyo.mobile.util.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import cn.yo.mobile.yeh.R;
public class MovieinfoAdpter extends BaseAdapter {
	private Context context;
	public ArrayList<SmallVideoBean> beans;
	public MovieinfoAdpter(Context context, ArrayList<SmallVideoBean> beans) {
		this.context = context;
		this.beans = beans;
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return beans.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = arg1;
		GetView getView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.movieinfo_list_item, null);
			getView = new GetView();
			getView.imageView = (ImageView) view.findViewById(R.id.hot_mv_img);
			getView.nameView = (TextView) view.findViewById(R.id.hot_mv_name);
			getView.textView = (TextView) view.findViewById(R.id.hot_mv_duration);
			view.setTag(getView);
		} else {
			getView = (GetView) view.getTag();
		}
		getView.nameView.setText(beans.get(arg0).getName());
		try {
			String time=StringUtils.generateTime(Long.parseLong(beans.get(arg0).getDuration()));
			getView.textView.setText("时长："+time);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ImageLoader.getInstance().displayImage(beans.get(arg0).getThumbnail(),
				getView.imageView);
		return view;
	}

	static class GetView {
		ImageView imageView;
		TextView nameView;
		TextView textView;
	}

}
