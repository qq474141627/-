package cn.yoyo.mobile.ui;



import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import cn.yoyo.mobile.util.UITimer;
import cn.yoyo.mobile.util.UITimer.OnUITimer;
import cn.yoyo.mobile.xml.XMLParser;

import com.android.video.aac.R;

public class Fragment_TV extends Fragment implements OnItemClickListener ,OnScrollListener,OnCheckedChangeListener{
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
	private ViewPager viewPager ;
	private UITimer timer = new UITimer(5000, new OnUITimer() {
		@Override
		public void onTimer() {
			// TODO Auto-generated method stub
			int p = viewPager.getCurrentItem();
			if(p == viewPager.getAdapter().getCount() - 1) {// 从最后一页向右滑动
				viewPager.setCurrentItem(0,true);
			}else{
				viewPager.setCurrentItem(p+1,true);
			}
		}
	});
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  beans = new ArrayList<VideoBean>();
	  adpter = new HotAdpter( getActivity(), beans);
	  parameter.setCategory(getResources().getString(R.string.TV));
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mView==null){
			mView = inflater.inflate(R.layout.fragment_tv, container,
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
			
			//initViewPager(inflater);
			
			new MyThread().start();
			
		}
		return mView;
	}

	
	public void onResume() {
		super.onResume();
		//if(timer!=null)
		//   timer.start(5000);
		}

	public void onPause() {
		super.onPause();
		if(timer!=null)
		   timer.stop();
		   timer = null;
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
			parameter.setGenre("古装");
			setPostion(arg0.getChildAt(1));
			break;
		case R.id.bar_rd3:
			parameter.clearData();
			parameter.setArea("韩国");
			setPostion(arg0.getChildAt(2));
			break;
		case R.id.bar_rd4:
			parameter.clearData();
			parameter.setOrderby(1);
			setPostion(arg0.getChildAt(3));
			break;
		case R.id.bar_rd5:
			parameter.clearData();
			setPostion(arg0.getChildAt(4));
			break;
		case R.id.bar_rd6:
			parameter.clearData();
			parameter.setArea("大陆");
			setPostion(arg0.getChildAt(5));
			break;
		case R.id.bar_rd7:
			parameter.clearData();
			parameter.setArea("美国");
			setPostion(arg0.getChildAt(6));
			break;
		case R.id.bar_rd8:
			parameter.clearData();
			parameter.setArea("香港");
			setPostion(arg0.getChildAt(7));
			break;
		case R.id.bar_rd9:
			parameter.clearData();
			parameter.setArea("日本");
			setPostion(arg0.getChildAt(8));
			break;
		case R.id.bar_rd10:
			parameter.clearData();
			parameter.setArea("英国");
			setPostion(arg0.getChildAt(9));
			break;
		case R.id.bar_rd11:
			parameter.clearData();
			parameter.setOrderby(3);
			setPostion(arg0.getChildAt(10));
			break;
		case R.id.bar_rd12:
			parameter.clearData();
			parameter.setArea("台湾");
			setPostion(arg0.getChildAt(11));
			break;
		case R.id.bar_rd13:
			parameter.clearData();
			parameter.setArea("泰国");
			setPostion(arg0.getChildAt(12));
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


