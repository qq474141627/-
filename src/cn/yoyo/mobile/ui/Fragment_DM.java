package cn.yoyo.mobile.ui;



import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import cn.yoyo.mobile.beans.Parameter;
import cn.yoyo.mobile.beans.VideoBean;
import cn.yoyo.mobile.ui.adapter.HotAdpter;
import cn.yoyo.mobile.ui.adapter.MyPagerAdapter;
import cn.yoyo.mobile.util.ToastUtils;
import cn.yoyo.mobile.xml.XMLParser;

import cn.yo.mobile.yeg.R;

public class Fragment_DM extends Fragment implements OnItemClickListener ,OnScrollListener,OnCheckedChangeListener{
	private ListView listView; //展示数据的listview
	private HotAdpter adpter;  //绑定数据的适配器
	private ArrayList<VideoBean> beans;   //放置视频对象的集合
	private RadioGroup radioGroup;  //放置上面的各个频道按钮
	private boolean isResh; //是否获取数据结束，防止listview滚动到最下面，重复获取数据
	private View pView; //转圈圈view
	private int[] location; //保存频道button的位置
	private int postion;
	private int width;
	private HorizontalScrollView scrollView; 
	private View mView;
	private View footerView;
	private Parameter parameter=new Parameter();
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  beans = new ArrayList<VideoBean>();
	  adpter = new HotAdpter( getActivity(), beans);
	  parameter.setCategory(getResources().getString(R.string.Anime));
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mView==null){
			mView = inflater.inflate(R.layout.fragment_dm, container,
					false);
			scrollView = (HorizontalScrollView) mView.findViewById(R.id.hscroll);
			location = new int[2];
			width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
			radioGroup = (RadioGroup) mView.findViewById(R.id.bar_rg);
			radioGroup.setOnCheckedChangeListener(this);
			pView = mView.findViewById(R.id.pb);
			listView = (ListView) mView.findViewById(R.id.listView);
			listView.setAdapter(adpter);
			listView.setOnItemClickListener(this);
			listView.setOnScrollListener(this);
			footerView = inflater.inflate(R.layout.progress_bar, null);
			footerView.setVisibility(View.GONE);
			listView.addFooterView(footerView);
			initViewPager(inflater);
			new MyThread().start();
		}
		return mView;
	}

	private void initViewPager(LayoutInflater mInflater){
		ViewPager viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
		List<View> listViews = new ArrayList<View>();
		View view1 = mInflater.inflate(R.layout.item_viewpager5, null);
		view1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),HtmlPlayer.class);
				intent.putExtra("url", "http://www.letv.com/ptv/vplay/22222449.html");
				startActivity(intent);
			}
		});
		listViews.add(view1);
		View view2 = mInflater.inflate(R.layout.item_viewpager6, null);
		view2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),HtmlPlayer.class);
				intent.putExtra("url", "http://www.letv.com/ptv/vplay/22195131.html");
				startActivity(intent);
			}
		});
		listViews.add(view2);
		View view3 = mInflater.inflate(R.layout.item_viewpager7, null);
		view3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),HtmlPlayer.class);
				intent.putExtra("url", "http://www.letv.com/ptv/vplay/22195131.html");
				startActivity(intent);
			}
		});
		listViews.add(view3);
		View view4 = mInflater.inflate(R.layout.item_viewpager8, null);
		view4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),HtmlPlayer.class);
				intent.putExtra("url", "http://www.letv.com/ptv/vplay/22195131.html");
				startActivity(intent);
			}
		});
		listViews.add(view4);
		viewPager.setAdapter(new MyPagerAdapter(listViews));
		((Activity_Main)getActivity()).getSlideMenu().addIgnoredView(viewPager);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Intent intent=new Intent();
		intent.putExtra("movie", beans.get(arg2));
		intent.setClass(getActivity(), Activity_MovieInfo.class);
		startActivity(intent);
	}
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		if (arg1 + arg2 == arg3 && isResh && arg3 > 0) {
			parameter.setPage(parameter.getPage()+1);
			new MyThread().start();
			isResh = false;
		}
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pView.setVisibility(View.GONE);
			if (beans.isEmpty()) {
				ToastUtils.showToast("获取数据失败");
				return;
			}
			isResh = true;
			adpter.notifyDataSetChanged();
			if(beans.size()>0){
				footerView.setVisibility(View.VISIBLE);
			}
		};
	};

	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			beans.addAll(XMLParser.getVideoBeans(parameter));
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

		switch (arg1) {
		case R.id.bar_rd1:
			parameter.clearData();
			setPostion(arg0.getChildAt(0));
			break;
		case R.id.bar_rd2:
			parameter.clearData();
			parameter.setOrderby(1);
			setPostion(arg0.getChildAt(1));
			break;
		case R.id.bar_rd3:
			parameter.clearData();
			parameter.setArea("日本");
			setPostion(arg0.getChildAt(2));
			break;
		case R.id.bar_rd4:
			parameter.clearData();
			parameter.setGenre("冒险");
			setPostion(arg0.getChildAt(3));
			break;
		case R.id.bar_rd5:
			parameter.clearData();
			parameter.setGenre("科幻");
			setPostion(arg0.getChildAt(4));
			break;
		case R.id.bar_rd6:
			parameter.clearData();
			parameter.setGenre("搞笑");
			setPostion(arg0.getChildAt(5));
			break;
		case R.id.bar_rd7:
			parameter.clearData();
			parameter.setGenre("战争");
			setPostion(arg0.getChildAt(6));
			break;
		case R.id.bar_rd8:
			parameter.clearData();
			parameter.setGenre("忍者");
			setPostion(arg0.getChildAt(7));
			break;
		default:
			break;
		}
	}

	private void setPostion(View view) {
		view.getLocationInWindow(location);
		postion = location[0] - width / 2;
		if (postion != 0) {
			postion += 50;
			scrollView.smoothScrollBy(postion, 0);
		}
		beans.clear();
		adpter.notifyDataSetChanged();
		pView.setVisibility(View.VISIBLE);
		new MyThread().start();
	}

}


