package cn.yoyo.mobile.ui;




import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import cn.yoyo.mobile.beans.SmallVideoBean;
import cn.yoyo.mobile.beans.VideoBean;
import cn.yoyo.mobile.ui.adapter.MovieinfoAdpter;
import cn.yoyo.mobile.util.ToastUtils;
import cn.yoyo.mobile.xml.XMLParser;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.android.video.aab.R;

public class Activity_SelectNum extends SherlockActivity implements OnItemClickListener,OnScrollListener{
	private ListView listView; //展示数据的listview
	private MovieinfoAdpter adpter;  //绑定数据的适配器
	private ArrayList<SmallVideoBean> beans;   //放置视频对象的集合
	private View pView; //转圈圈view
	private VideoBean oBean;
	private boolean isResh; //是否获取数据结束，防止listview滚动到最下面，重复获取数据
	private int page=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  oBean=(VideoBean) getIntent().getSerializableExtra("movie");
	  prepareActionBar();
	  beans = new ArrayList<SmallVideoBean>();
	  adpter = new MovieinfoAdpter( this, beans);
	  setContentView(R.layout.fragment_selectnum);
	  pView = findViewById(R.id.pb);
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(adpter);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		new MyThread().start();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if(TextUtils.isEmpty(beans.get(arg2).getId())){
			ToastUtils.showToast("该播放地址失效");
			return;
		}
		Intent intent = new Intent(Activity_SelectNum.this,HtmlPlayer.class);
		intent.putExtra("url", beans.get(arg2).getLink());
		startActivity(intent);
		//String uri="http://v.youku.com/player/getRealM3U8/vid/"+beans.get(arg2).getId()+"/type/video.m3u8";
		/*String type = "/video";
		if(oBean.getTypes()!=null&&oBean.getTypes().size()>0){
			if(oBean.getTypes().contains("hd2")){
				type="hd2/v";
			}else if(oBean.getTypes().contains("hd")){
				type="hd/v";
			}else if(oBean.getTypes().contains("mp4")){
				type="mp4/v";
			}else if(oBean.getTypes().contains("flv")){
				type="flv/v";
			}
		}
		String uri="http://v.youku.com/player/getM3U8/vid/"+beans.get(arg2).getId()+"/type/"+type+".m3u8";
		Intent intent=new Intent();
		intent.setData(Uri.parse(uri));
		intent.putExtra(
				"displayName                                                                                                                                                                                            ",
				oBean.getName());
		intent.setClass(this, VideoActivity.class);
		startActivity(intent);
		Log.i("TAG999", beans.get(arg2).getId());*/
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
		};
	};

	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			beans.addAll(XMLParser.getSmallVideoBeans(oBean.getId(),page));
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2,
			int arg3) {
		if (arg1 + arg2 == arg3 && isResh && arg3 > 0) {
			page+=1;
			new MyThread().start();
			isResh = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	private void prepareActionBar() {
	    ActionBar mActionBar = getSupportActionBar();
        //mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
       // mActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(oBean.getName());
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
		finish();
		break;
	default:
		break;
	}
	return super.onOptionsItemSelected(item);
	}

}


