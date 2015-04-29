package cn.yoyo.mobile.ui;


import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.video.aac.R;
import cn.yoyo.mobile.util.StringUtils;
import cn.yoyo.mobile.util.ToastUtils;

import com.fengyi.gamesdk.service.MyPay;
import com.umeng.analytics.MobclickAgent;

public class Activity_Start extends Activity{
	private View progress_bar,dialog_warning,dialog_libao;
	private MyReceiver receiver;
	private String FYPAY_RESUKT_MSG = "com.fengyi.gamesdk.service.feenotify";
	private int price = 16;
	private int times = 0;//尝试次数
	private SharedPreferences preferences;
	private String paytime = "time";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		MyPay.init(this);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		progress_bar = findViewById(R.id.pb);
		showWarningDialog();
		
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(FYPAY_RESUKT_MSG);
        this.registerReceiver(receiver,filter);
	}
	
	public void onDestroy() {
		super.onDestroy();
		if(receiver != null)
		unregisterReceiver(receiver);
		}
	
	private void showWarningDialog(){
		dialog_warning = findViewById(R.id.dialog_warning);
		findViewById(R.id.btn_warning).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(preferences.getString(paytime,"").equals(StringUtils.getDate())){
					startActivity(new Intent(Activity_Start.this, Activity_Main.class));
			    	overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			    	finish();
				}else{
					dialog_warning.setVisibility(8);
					showLibaoDialog();
				}
			}
		});
	}
	private void showLibaoDialog(){
		dialog_libao = findViewById(R.id.dialog_libao);
		dialog_libao.setVisibility(0);
		findViewById(R.id.btn_libao).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				progress_bar.setVisibility(0);
				//点击统计
				MobclickAgent.onEvent(Activity_Start.this, "click");
				pay();
			}
		});
	}
	
	private void pay(){
		MobclickAgent.onEvent(Activity_Start.this, "pay",price);
		MyPay.pay(Activity_Start.this, price);
	}
    
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals(FYPAY_RESUKT_MSG)){
				Bundle bundle = intent.getExtras();
			    if(bundle == null)
			    	return;
			    int value = bundle.getInt("orderResult");
			    switch (value) {
				case 0:
					//保存时间
					preferences.edit().putString(paytime, StringUtils.getDate()).commit();
					MobclickAgent.onEvent(Activity_Start.this, "success",""+price);
					progress_bar.setVisibility(8);
			    	startActivity(new Intent(context, Activity_Main.class));
			    	overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			    	finish();
					break;
				case -2:
					MobclickAgent.onEvent(Activity_Start.this, "NetError",""+price);
					progress_bar.setVisibility(8);
			        ToastUtils.showLongToast("网络异常,请检查网络后重试");
					break;
				default:
					MobclickAgent.onEvent(Activity_Start.this, "fail",""+price);
					times++;
			    	if(times == 1){
			    		price = 10;
			    		pay();
			    	}else if(times == 2){
			    		progress_bar.setVisibility(8);
			    		price = 16;
			    		times = 0;
			    		ToastUtils.showLongToast("连接失败,请重试");
			    	}
					break;
				}
			}
		}
	}
	
	private long exitTime = 0;
	   @Override
	   public boolean onKeyDown(int keyCode, KeyEvent event)
	   {
		   if (keyCode == KeyEvent.KEYCODE_BACK)
	       {
	    	   if ((System.currentTimeMillis() - exitTime) > 1000)
	           {
	                   Toast.makeText(this, getString(R.string.exit_click), Toast.LENGTH_SHORT).show();
	                   exitTime = System.currentTimeMillis();
	           } else
	           {
	        	   finish();
	           }
	   			return true;
	       }
	       return super.onKeyDown(keyCode, event);
	   }
	   
}
