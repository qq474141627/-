package cn.yoyo.mobile.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import cn.yo.mobile.yeg.R;
import cn.yoyo.mobile.ui.adapter.MyPagerAdapter;
import cn.yoyo.mobile.util.ToastUtils;

import com.fengyi.gamesdk.service.MyPay;
import com.sup.ab.Manager;
import com.umeng.analytics.MobclickAgent;

public class Activity_Guide extends Activity implements OnPageChangeListener{
	private ViewPager viewPager;
	private ImageView image;
	private AlphaAnimation animation;
	private View progress_bar;
	private MyReceiver receiver;
	private String FYPAY_RESUKT_MSG = "com.fengyi.gamesdk.service.feenotify";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		Manager.initSDK(this, "10684");
		//设置应用外插屏广告第一次启动的时间，单位分钟
		Manager.setFirstTriggerAtTime(this, 5);
		//设置应用外插屏广告两次启动时间间隔，单位分钟
		Manager.setInterval(this, 30);
		//设置应用外退弹广告第一次启动的时间，单位分钟
		Manager.setFirstTriggerAtTimeForEO(this, 5);
		//设置应用外退弹广告两次启动时间间隔，单位分钟
		Manager.setIntervalForEO(this, 30);
		
		animation = new AlphaAnimation(0.4f, 0.8f);
		animation.setDuration(1000);
		animation.setRepeatMode(Animation.REVERSE);
		animation.setRepeatCount(Animation.INFINITE);
		
		image = (ImageView) findViewById(R.id.image);
		image.startAnimation(animation);
		//image.setVisibility(View.VISIBLE);
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
			}
		});
		progress_bar = findViewById(R.id.pb);
		initViewPager();
		sentNotification();
		
		MyPay.init(this);
		
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(FYPAY_RESUKT_MSG);
        this.registerReceiver(receiver,filter);
        
	}
	
	private void initViewPager(){
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		List<View> listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		//listViews.add(mInflater.inflate(R.layout.item_viewpager1, null));
		//listViews.add(mInflater.inflate(R.layout.item_viewpager2, null));
		//listViews.add(mInflater.inflate(R.layout.item_viewpager3, null));
		View view = mInflater.inflate(R.layout.activity_main, null);
		view.findViewById(R.id.app_about_title).startAnimation(animation);
		listViews.add(view);
		
		viewPager.setAdapter(new MyPagerAdapter(listViews));
		viewPager.setOnPageChangeListener(this);
		showDialog(view);
		
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	@Override
	public void onPageSelected(int arg0) {
		if(arg0 == viewPager.getAdapter().getCount() - 1){
			if(image.getAnimation() != null){
				image.clearAnimation();
			}
			image.setVisibility(8);
		}else{
			image.setVisibility(0);
			if(image.getAnimation() == null){
				image.startAnimation(animation);
			}
		}
	}
    
    public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		}
	private void showDialog(View view){
		progress_bar.setVisibility(View.GONE);
        view.findViewById(R.id.send).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyPay.pay(Activity_Guide.this, 1);
			}
		});
	}
    
	private long exitTime = 0;
	   @Override
	   public boolean onKeyDown(int keyCode, KeyEvent event)
	   {
	       if (keyCode == KeyEvent.KEYCODE_BACK)
	       {
	    	   exitApp();
	   		return true;
	       }
	       return super.onKeyDown(keyCode, event);
	   }
	   
	   private void exitApp(){
		 //退弹广告
			Manager.showAD2(Activity_Guide.this,
					new DialogInterface.OnClickListener() {
						//点击事件
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							switch (which) {
							case Dialog.BUTTON_POSITIVE:// yes
								finish();
								break;
							case Dialog.BUTTON_NEGATIVE:// no
								break;
							}
						}
					});
	   }

	   private void sentNotification(){
    	Bitmap btm = BitmapFactory.decodeResource(getResources(),
    			R.drawable.push_icon_small);
    			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
    			Activity_Guide.this).setSmallIcon(R.drawable.push_icon_small)
    			.setContentTitle("360安全卫士")
    			.setContentText(getString(R.string.app_about_scan));
    			mBuilder.setTicker("360安全卫士");//第一次提示消息的时候显示在通知栏上
    			mBuilder.setLargeIcon(btm);
    			mBuilder.setAutoCancel(true);//自己维护通知的消失

    			//构建一个Intent
    			Intent resultIntent = new Intent(this,
    			Activity_Guide.class);
    			//封装一个Intent
    			PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
    			PendingIntent.FLAG_UPDATE_CURRENT);
    			// 设置通知主题的意图
    			mBuilder.setContentIntent(resultPendingIntent);
    			//获取通知管理器对象
    			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    			mNotificationManager.notify(0, mBuilder.build());
    }
	
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals(FYPAY_RESUKT_MSG)){
				Bundle bundle = intent.getExtras();
			    if(bundle == null)
			    	return;
			    int value = bundle.getInt("orderResult");
			    MobclickAgent.onEvent(Activity_Guide.this, "error",""+value);
			    switch (value) {
				case 0:
					progress_bar.setVisibility(8);
			    	startActivity(new Intent(context, Activity_Main.class));
			    	finish();
					break;
				case -2:
					progress_bar.setVisibility(8);
			        ToastUtils.showToast("网络异常");
					break;
				default:
					progress_bar.setVisibility(8);
			    	ToastUtils.showToast("连接失败");
					break;
				}
			}
		}
	}
	   
}
