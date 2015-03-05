package cn.yoyo.mobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yoyo.mobile.beans.VideoBean;
import cn.yoyo.mobile.util.StringUtils;
import cn.yoyo.mobile.xml.XMLParser;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import cn.yoyo.mobile.yef.R;

public class Activity_MovieInfo extends SherlockActivity implements OnClickListener{
	private VideoBean oBean;
	private ImageView imageView;
	private TextView show_movie_area,show_movie_director,
	                 show_movie_performer,show_movie_info,show_movie_genre,
	                 text_play,
	                 text_up,text_favorite_count;
    private View pView;
    private ImageView ll_play;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_movi_info);
		oBean = (VideoBean) getIntent().getSerializableExtra("movie");
		initView();
		getInfo();
	}

	private void initView() {
		prepareActionBar();
		pView = findViewById(R.id.pb);
		imageView = (ImageView) findViewById(R.id.show_movie_info_imag_id);
		show_movie_area = (TextView) findViewById(R.id.show_movie_area);
		show_movie_director = (TextView) findViewById(R.id.show_movie_director);
		show_movie_performer = (TextView) findViewById(R.id.show_movie_performer);
		show_movie_genre = (TextView) findViewById(R.id.show_movie_genre);
		show_movie_info = (TextView)findViewById(R.id.show_movie_info);
		ll_play = (ImageView)findViewById(R.id.ll_play);
		text_play = (TextView)findViewById(R.id.text_play);
		text_up = (TextView)findViewById(R.id.text_up);
		text_favorite_count = (TextView)findViewById(R.id.text_favorite_count);
		ll_play.setOnClickListener(this);
	}


	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			setDate();
		};
	};

	private void setDate() {
		pView.setVisibility(View.GONE);
		ImageLoader.getInstance().displayImage(oBean.getThumbnail(), imageView);
		show_movie_area.setText("国家/地区："+oBean.getArea());
		show_movie_genre.setText("类型："+oBean.getGenre());
		show_movie_director.setText(oBean.getDirector());
		show_movie_performer.setText(oBean.getPerformer());
		show_movie_info.setText(oBean.getDescription());
		text_play.setText(StringUtils.getViewNum(oBean.getView_count()));
		text_up.setText("顶("+StringUtils.getViewNum(oBean.getUp_count())
				+") / 踩("+StringUtils.getViewNum(oBean.getDown_count())
				+")");
		text_favorite_count.setText(StringUtils.getViewNum(oBean.getComment_count())+"人正在观看");
	}

	class th extends Thread {
		@Override
		public void run() {
			super.run();
			XMLParser.getVideoInfo(oBean);
			handler.sendEmptyMessage(1);
		}
	}

	private void getInfo() {
		new th().start();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub ll_play,ll_down,ll_share,ll_save
		if(v.getId() == R.id.ll_play){
			Log.i("TAG999", "点击了");
			Intent intent=new Intent();
			intent.putExtra("movie", oBean);
			intent.setClass(Activity_MovieInfo.this, Activity_SelectNum.class);
			startActivity(intent);
			
		}
			
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
	private void prepareActionBar() {
	    ActionBar mActionBar = getSupportActionBar();
        //mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
       // mActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(oBean.getName());
    }
}
