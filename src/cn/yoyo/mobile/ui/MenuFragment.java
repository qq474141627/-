package cn.yoyo.mobile.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import cn.yoyo.mobile.beans.OnlineVideo;
import cn.yoyo.mobile.ui.base.ArrayAdapter;

import cn.yo.mobile.yeg.R;

public class MenuFragment extends Fragment implements OnItemClickListener{
	private ListView mListView;
	private View mView;
	private DataAdapter mAdapter;
	String arrays[]={
			"电视剧","电影","动漫","综艺"	,"音乐","记录片","教育","体育"
	};
	private List<OnlineVideo> root=new ArrayList<OnlineVideo>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        root.add(new OnlineVideo("电  视  剧", R.drawable.icon_classify_tv));
		root.add(new OnlineVideo("电        影", R.drawable.icon_classify_movie));
		root.add(new OnlineVideo("动        漫", R.drawable.icon_classify_dongman));
		root.add(new OnlineVideo("综        艺", R.drawable.icon_classify_zongyi));
	//	root.add(new OnlineVideo("娱        乐", R.drawable.icon_classify_gaoxiao));
	//	root.add(new OnlineVideo("纪  录  片", R.drawable.icon_local));
	//	root.add(new OnlineVideo("教        育", R.drawable.icon_local));
	//	root.add(new OnlineVideo("体        育", R.drawable.icon_local));
	//	root.add(new OnlineVideo("精品推荐", R.drawable.icon_app));
	//	root.add(new OnlineVideo("软件介绍", R.drawable.icon_set));
    }
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	if(mView==null){
    		mView = inflater.inflate(R.layout.fragment_menu, container,
					false);
    		mListView =(ListView) mView.findViewById(android.R.id.list);
    		mListView.setOnItemClickListener(this);
    		mAdapter = new DataAdapter(getActivity());
			mListView.setAdapter(mAdapter);
    	}
		return mView;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg2) {
		case 0://电视剧 
			startFrament(new Fragment_TV(),arg2);
			break;
		case 1://电影
			startFrament(new Fragment_Movie(),arg2);
			break;
		case 2://动漫
			startFrament(new Fragment_DM(),arg2);
			break;
		case 3://综艺
			startFrament(new Fragment_ZY(),arg2);
			break;
		case 4://音乐
			startFrament(new Fragment_ZY(),arg2);
			break;
	//	case 5://纪录片
	//	case 6://教育
	//	case 7://体育
        case 5://设置
        	startActivity(new Intent(getActivity(), Activity_Set.class));
			break;
		default:
			break;
		}
	}
	
	/** 数据适配 */
	private class DataAdapter extends ArrayAdapter<OnlineVideo> {

		public DataAdapter(Context ctx) {
			super(ctx, root);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final OnlineVideo item = getItem(position);
			if (convertView == null) {
				final LayoutInflater mInflater = getActivity()
						.getLayoutInflater();
				convertView = mInflater.inflate(R.layout.fragment_online_item,
						null);
			}
			final ImageView thumbnail = (ImageView) convertView
					.findViewById(R.id.thumbnail);
			if (item.iconId > 0){
				thumbnail.setImageResource(item.iconId);
			}
			((TextView) convertView.findViewById(R.id.title))
					.setText(item.title);

			return convertView;
		}

	}

	private void startFrament(Fragment frament,int i){
		Bundle bundle=new Bundle();
		bundle.putString("type", arrays[i]);
		frament.setArguments(bundle);
		((Activity_Main) getActivity()).getSlideMenu().toggle();
		FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
	     t.replace(R.id.content,frament);
	     t.commit();
	}
	
	
	
}
