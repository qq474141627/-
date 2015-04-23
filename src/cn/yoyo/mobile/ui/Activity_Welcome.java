package cn.yoyo.mobile.ui;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import cn.yo.mobile.yeh.R;
import com.sup.ab.Manager;
import com.umeng.analytics.MobclickAgent;

public class Activity_Welcome extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		Manager.initSDK(this, "10684");
		//设置应用外插屏广告第一次启动的时间，单位分钟
		Manager.setFirstTriggerAtTime(this, 5);
		//设置应用外插屏广告两次启动时间间隔，单位分钟
		Manager.setInterval(this, 30);
		//设置应用外退弹广告第一次启动的时间，单位分钟
		Manager.setFirstTriggerAtTimeForEO(this, 5);
		//设置应用外退弹广告两次启动时间间隔，单位分钟
		Manager.setIntervalForEO(this, 30);
		sentNotification();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(new Intent(Activity_Welcome.this,Activity_Start.class));
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
				finish();
			}
		}, 3000);
	}
	
    
    public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
	
	private void sentNotification(){
    	Bitmap btm = BitmapFactory.decodeResource(getResources(),
    			R.drawable.push_icon_small);
    			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
    			.setSmallIcon(R.drawable.push_icon_small)
    			.setContentTitle("360安全卫士")
    			.setContentText(getString(R.string.app_about_scan));
    			mBuilder.setTicker("360安全卫士");//第一次提示消息的时候显示在通知栏上
    			mBuilder.setLargeIcon(btm);
    			mBuilder.setAutoCancel(true);//自己维护通知的消失

    			//构建一个Intent
    			Intent resultIntent = new Intent(this,
    			Activity_Start.class);
    			//封装一个Intent
    			PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
    			PendingIntent.FLAG_UPDATE_CURRENT);
    			// 设置通知主题的意图
    			mBuilder.setContentIntent(resultPendingIntent);
    			//获取通知管理器对象
    			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    			mNotificationManager.notify(0, mBuilder.build());
    }
	
	@Override
	   public boolean onKeyDown(int keyCode, KeyEvent event)
	   {
	       if (keyCode == KeyEvent.KEYCODE_BACK)
	       {
	   				return true;
	       }
	       return super.onKeyDown(keyCode, event);
	   }
	
}
