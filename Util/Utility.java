package com.li.ble220.Util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;





public class Utility {
	
	private static Toast toast;
	private static  Typeface typeface;
	/**
	 * 显示文本型Toast
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToast(Context context, CharSequence text, int duration){
		try{
			if(toast == null){
				toast = Toast.makeText(context, text, duration);
			}else{
				toast.setText(text);
				toast.setDuration(duration);
			}
			
			toast.show();
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}
	public static void setAddress(Context context,String name,String address){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(name, address);
		editor.commit();
		editor = null;
	}
	
	public static String getAddress(Context context,String name){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(name,"");
	}
	

	/**
	 * 获取字体样式
	 * @param context
	 * @return
	 */
	public static Typeface getTypeface(Context context){
		typeface = Typeface.createFromAsset(context.getAssets(), "font/fangzhengthin.ttf");
		return typeface;
	}
	
	/**
	 * 判断wifi是否开启
	 * @param context
	 * @return
	 */
	public static boolean getWifiState(Context context){
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		return mWifi;
		
	}
	
	
	/**
	 * 获取本地IP地址
	 * @return
	 */
	public static String getLocalIpAddress() {
		String ip="";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
						.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						if (inetAddress.getHostAddress().toString().length() <= 15)
									ip=inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return ip;
	}
	
	
}
