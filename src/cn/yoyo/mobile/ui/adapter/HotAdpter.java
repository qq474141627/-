package cn.yoyo.mobile.ui.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yoyo.mobile.beans.VideoBean;
import cn.yoyo.mobile.util.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import cn.yo.mobile.yeh.R;
public class HotAdpter extends BaseAdapter {
	private Context context;
	public ArrayList<VideoBean> beans;
	public HotAdpter(Context context, ArrayList<VideoBean> beans) {
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
					R.layout.home_list_item, null);
			getView = new GetView();
			getView.imageView = (ImageView) view.findViewById(R.id.hot_mv_img);
			getView.nameView = (TextView) view
					.findViewById(R.id.hot_mv_name_id);
			getView.hot_update = (TextView) view
					.findViewById(R.id.hot_mv_update_id);
			getView.hot_nums = (TextView) view.findViewById(R.id.hot_nums1);
			getView.hot_time=(TextView) view.findViewById(R.id.hot_time1);
			getView.hot_score=(TextView) view.findViewById(R.id.hot_score1);
			view.setTag(getView);
		} else {
			getView = (GetView) view.getTag();
		}
		getView.nameView.setText(beans.get(arg0).getName());
		getView.hot_nums.setText(StringUtils.getViewNum(beans.get(arg0).getView_count()));
		getView.hot_time.setText(beans.get(arg0).getPublished());
		getView.hot_score.setText(beans.get(arg0).getScore());
		if(!TextUtils.isEmpty(beans.get(arg0).getEpisode_count())
				&&!TextUtils.isEmpty(beans.get(arg0).getEpisode_updated())
				){
			if(beans.get(arg0).getEpisode_count().equals(beans.get(arg0).getEpisode_updated())){
				if(!beans.get(arg0).getEpisode_updated().equals("1")){
					getView.hot_update.setText("共"+beans.get(arg0).getEpisode_updated()+"集全");
				}else{
					getView.hot_update.setText("被赞"+beans.get(arg0).getFavorite_count()+"次");
				}
			}else{
				getView.hot_update.setText("更新至第"+beans.get(arg0).getEpisode_updated()+"集");
			}
		}
		ImageLoader.getInstance().displayImage(beans.get(arg0).getThumbnail(),
				getView.imageView);
		return view;
	}

	static class GetView {
		ImageView imageView;
		TextView nameView;
		TextView hot_nums;
		TextView hot_time;
		TextView hot_score;
		TextView hot_update;
	}

}
