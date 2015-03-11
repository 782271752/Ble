package com.li.ble220.Fragment;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.Util.Utility;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingFragment extends Fragment implements OnClickListener{
	
	private View settingView;
	private Typeface typeface;
	private TextView title;
	private Button wifiBtn,bleBtn;
	private Context context;
	private boolean isOpen=false,isBleopen=false;
	private Activity activity;
	private BluetoothAdapter mBluetoothAdapter;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=(MainActivity)getActivity();
		typeface=Utility.getTypeface(context);
		activity=(Activity)context;
		mBluetoothAdapter = BluetoothAdapter 
                .getDefaultAdapter(); 
        if (mBluetoothAdapter == null) { 
            Toast.makeText(context, "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show(); 
            activity.finish(); 
        } 
	}
	
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.e("------wifi-----",Utility.getWifiState(context)+"");
		
		if (Utility.getWifiState(context)) {
			Message msg=new Message();
			msg.what=0;
			mHandler.sendMessage(msg);
			isOpen=true;
		}else {
			Message msg=new Message();
			msg.what=1;
			mHandler.sendMessage(msg);
			isOpen=false;
		}
		
		if (mBluetoothAdapter.isEnabled()) {
			Message msg=new Message();
			msg.what=2;
			mHandler.sendMessage(msg);
			isBleopen=true;
		}else {
			Message msg=new Message();
			msg.what=3;
			mHandler.sendMessage(msg);
			isBleopen=false;
		}
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		settingView=inflater.inflate(R.layout.setting, container,false);
		title=(TextView)settingView.findViewById(R.id.setting_title);
		title.setTypeface(typeface);
		wifiBtn=(Button)settingView.findViewById(R.id.wifi_switch);
		wifiBtn.setOnClickListener(this);
		bleBtn=(Button)settingView.findViewById(R.id.ble_switch);
		bleBtn.setOnClickListener(this);
		return settingView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.wifi_switch:
			if(isOpen){
				toggleWiFi(false);
				wifiBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_off));
				isOpen=false;
				Utility.showToast(context, "Wifi已经关闭", Toast.LENGTH_SHORT);
			}else{
				toggleWiFi(true);
				wifiBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_on));
				isOpen=true;
				Utility.showToast(context, "Wifi已经开启", Toast.LENGTH_SHORT);
			}
				
			break;
		case R.id.ble_switch:
			if (isBleopen) {
				toggleBle(false);
				bleBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_off));
				isBleopen=false;
				Utility.showToast(context, "蓝牙已经关闭", Toast.LENGTH_SHORT);
			}else{
				toggleBle(true);
				bleBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_on));
				isBleopen=true;
				Utility.showToast(context, "蓝牙已经开启", Toast.LENGTH_SHORT);
			}
			
			break;
		default:
			break;
		}
		
	}
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				wifiBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_on));
				break;
			case 1:
				wifiBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_off));
				break;
			case 2:
				bleBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_on));
				break;
			case 3:
				bleBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_off));
				break;
			default:
				break;
			}
		}
		
	};
	
	
	
	
	



	public void toggleWiFi(boolean status) {
	        WifiManager wifiManager = (WifiManager) context
	                .getSystemService(Context.WIFI_SERVICE);
	        if (status == true && !wifiManager.isWifiEnabled()) {
	            wifiManager.setWifiEnabled(true);
	        } else if (status == false && wifiManager.isWifiEnabled()) {
	            wifiManager.setWifiEnabled(false);
	        }
	        sendBrocasd();
	 }
	 
	 public void toggleBle(boolean status){
		 if (status==true&&!mBluetoothAdapter.isEnabled()) { 
			mBluetoothAdapter.enable();
		 }else if(status==false&&mBluetoothAdapter.isEnabled()){
			 mBluetoothAdapter.disable();
		 }
	 }
	 
	 public void sendBrocasd(){
			final String BROADCAST = "com.li.ble.address";
			Intent intent = new Intent(BROADCAST); // 对应setAction()
			context.sendBroadcast(intent);
	}
	 
	 
}
