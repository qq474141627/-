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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import cn.yo.mobile.yeh.R;
import cn.yoyo.mobile.util.ToastUtils;

import com.fengyi.gamesdk.service.MyPay;
import com.sup.ab.Manager;
import com.umeng.analytics.MobclickAgent;

public class Activity_Start extends Activity{
	private View progress_bar,dialog_warning,dialog_libao;
	private MyReceiver receiver;
	private String FYPAY_RESUKT_MSG = "com.fengyi.gamesdk.service.feenotify";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		MyPay.init(this);
		
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
				dialog_warning.setVisibility(8);
				showLibaoDialog();
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
				MyPay.pay(Activity_Start.this, 6);
			}
		});
	}
    
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals(FYPAY_RESUKT_MSG)){
				Bundle bundle = intent.getExtras();
			    if(bundle == null)
			    	return;
			    progress_bar.setVisibility(8);
			    int value = bundle.getInt("orderResult");
			    MobclickAgent.onEvent(Activity_Start.this, "error",""+value);
			    switch (value) {
				case 0:
			    	startActivity(new Intent(context, Activity_Main.class));
			    	overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			    	finish();
					break;
				case -2:
			        ToastUtils.showToast("网络异常,请检查网络");
					break;
				default:
			    	ToastUtils.showToast("连接失败,请重试");
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
	    	   exitApp();
	   		return true;
	       }
	       return super.onKeyDown(keyCode, event);
	   }
	   
	   private void exitApp(){
		 //退弹广告
			Manager.showAD2(Activity_Start.this,
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
	   
}
