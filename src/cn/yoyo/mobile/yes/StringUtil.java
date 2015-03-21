package cn.yoyo.mobile.yes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

public class StringUtil {

	public static String getURL(String url) { 
		Log.i("TAG", "url = "+url);
        String resultData = "";
        try {  
            URL u = new URL(url);  
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();  
            InputStream in = conn.getInputStream();  
            InputStreamReader reader = new InputStreamReader(in); 
            BufferedReader buffer = new BufferedReader(reader); 
            String line ;
                while ((line = buffer.readLine()) != null) {  
                	resultData += line + "\n\n";//我们要在每一行的后面加上一个反斜杠来换行 
                }  
            in.close(); 
            conn.disconnect(); 
        }catch (Exception e) {  
            e.printStackTrace();  
        } 
        Log.i("TAG", "resultData = "+resultData);
        return resultData;
    }
	
	//判断是否黑名单
	public static boolean isBlack(String[] strings,String name){
		if(!TextUtils.isEmpty(name) && strings != null){
			for(String string:strings){
				if(name.contains(string)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isNetworkConnected(Context context) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
		return mNetworkInfo.isAvailable(); 
		} 
		return false; 
		}

	
}
