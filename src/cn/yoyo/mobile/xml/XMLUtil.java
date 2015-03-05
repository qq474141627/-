package cn.yoyo.mobile.xml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class XMLUtil {
	public static  String readJsonFromUrl(String path) { 
		String result=null;
		try{
			URL url = new URL(path);
			SSLContext sslctxt = SSLContext.getInstance("TLS");
			sslctxt.init(null, new TrustManager[]{new MyX509TrustManager()}, new java.security.SecureRandom());
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setSSLSocketFactory(sslctxt.getSocketFactory());
			conn.setHostnameVerifier(new MyHostnameVerifier());
			conn.connect();
			int respCode = conn.getResponseCode();
			if(respCode==200){
				InputStream input = conn.getInputStream();
				result = toString(input);
				input.close();
			}
			conn.disconnect();
			}catch(Exception e){
				e.printStackTrace();
			}
		  return result;
	      } 
	
	private static String toString(InputStream input){
		
		String content = null;
		try{
		InputStreamReader ir = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(ir);
		
		StringBuilder sbuff = new StringBuilder();
		while(null != br){
			String temp = br.readLine();
			if(null == temp)break;
			sbuff.append(temp).append(System.getProperty("line.separator"));
		}
		
		content = sbuff.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return content;
	}	
	static class MyX509TrustManager implements X509TrustManager{

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		
	}
	
	static class MyHostnameVerifier implements HostnameVerifier{
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
		
	}
}
