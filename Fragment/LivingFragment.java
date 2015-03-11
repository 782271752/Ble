package com.li.ble220.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.li.ble220.DeviceScanActivity;
import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.DB.DBManager;
import com.li.ble220.Util.BleUtility;
import com.li.ble220.Util.ConnectUtility;
import com.li.ble220.Util.Utility;

import android.app.Fragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 客厅控制页面
 * @author li_zhuonan@163.com
 *
 */
public class LivingFragment extends Fragment implements OnClickListener{

	private View livingView;
	private Context context;
	private BleUtility bleUtility;
	public static ConnectUtility connectUtility;
	private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private Button one,two,three,one_off,two_off,three_off,four,connectBtn;
    private DBManager dbManager;
    private String state,address;
    private Typeface typeface;
    private ProgressBar progressbar;
    private TextView stateTv;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=(MainActivity)getActivity();
		address="00:17:EA:8F:86:A1";
		context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		dbManager=new DBManager(context);
		bleUtility=new BleUtility(context, address);
		connectUtility=new ConnectUtility(context,address,bleUtility);
		typeface=Utility.getTypeface(context);
		state=dbManager.getBleState(address);
		
	}
	
    

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		livingView=inflater.inflate(R.layout.livingroom, container,false);
		one=(Button)livingView.findViewById(R.id.one);
		one.setTypeface(typeface);
		one.setOnClickListener(this);
		two=(Button)livingView.findViewById(R.id.two);
		two.setTypeface(typeface);
		two.setOnClickListener(this);
		three=(Button)livingView.findViewById(R.id.three);
		three.setTypeface(typeface);
		three.setOnClickListener(this);
		four=(Button)livingView.findViewById(R.id.four);
		four.setTypeface(typeface);
		four.setOnClickListener(this);
		one_off=(Button)livingView.findViewById(R.id.one_off);
		one_off.setTypeface(typeface);
		one_off.setOnClickListener(this);
		two_off=(Button)livingView.findViewById(R.id.two_off);
		two_off.setTypeface(typeface);
		two_off.setOnClickListener(this);
		
		three_off=(Button)livingView.findViewById(R.id.three_off);
		three_off.setTypeface(typeface);
		three_off.setOnClickListener(this);
		
		connectBtn=(Button)livingView.findViewById(R.id.living_connect_btn);
		connectBtn.setTypeface(typeface);
		connectBtn.setOnClickListener(this);
		connectBtn.setVisibility(View.GONE);//将连接按钮设置为不可见
		progressbar=(ProgressBar)livingView.findViewById(R.id.living_bar);
		stateTv=(TextView)livingView.findViewById(R.id.living_state);
		stateTv.setTypeface(typeface);
		stateTv.setOnClickListener(this);
		Log.e("客厅", state);
		
		if(state.equals("100")){
			new Thread(){

				@Override
				public void run() {
					connectUtility.connectBle();
					
				}
			}.start();
			
			
		}else {
			Message msg=new Message();
			msg.what=0;
			mHandler.sendMessage(msg);
		}
		updateConState();
		return livingView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		context.unregisterReceiver(mGattUpdateReceiver);
		dbManager.updateState(address,"0");
		
		dbManager.updateContect(address, "0");
		super.onDestroy();
	}


	private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LIVING_GATT_CONNECTED");
        intentFilter.addAction("LIVING_GATT_DISCONNECTED");
        intentFilter.addAction("LIVING_GATT_SERVICES_DISCOVERED");
        intentFilter.addAction("LIVING_DATA_AVAILABLE");
        return intentFilter;
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.one:
//			connectUtility.writeCharactic(context,1);
			sendValue(1);
			
			break;
		case R.id.two:
			connectUtility.writeCharactic(context,3);
			break;
		case R.id.three:
			connectUtility.writeCharactic(context,7);
			break;
		case R.id.four:
			connectUtility.writeCharactic(context,0);
			break;
		case R.id.living_connect_btn:
			new Thread(){

				@Override
				public void run() {
					connectUtility.connectBle();
				}
			}.start();
			Message msg=new Message();
			msg.what=1;
			mHandler.sendMessage(msg);
			
			updateConState();
			
			break;

		case R.id.one_off:
			connectUtility.writeCharactic(context,2);
			break;
		case R.id.two_off:
			connectUtility.writeCharactic(context,4);
			break;
		case R.id.three_off:
			connectUtility.writeCharactic(context,6);
			break;
		default:
			break;
		}
		
	}
	
	public void sendValue(int value){
		connectUtility.writeCharactic(context, value);
	}

	/**
	 * 广播接收类，解析来自BluetoothLeService的广播
	 */
	private BroadcastReceiver  mGattUpdateReceiver=new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				
				final String action = intent.getAction();
				System.out.println("action = " + action);
				if (bleUtility.ACTION_GATT_CONNECTED.equals(action)) {
					
//					Utility.showToast(context, "连接成功", Toast.LENGTH_SHORT);
				} else if (bleUtility.ACTION_GATT_DISCONNECTED
						.equals(action)) {
					Message msg=new Message();
					msg.what=0;
					mHandler.sendMessage(msg);
					
					dbManager.updateContect(address, "0");
					
				} else if (bleUtility.ACTION_GATT_SERVICES_DISCOVERED
						.equals(action)) {
					connectUtility.displayGattServices(bleUtility.getSupportedGattServices());
					dbManager.updateContect(address, "100");
					Message msg=new Message();
					msg.what=2;
					mHandler.sendMessage(msg);
					Utility.showToast(context, "连接成功", Toast.LENGTH_SHORT);
				} 
				
			}
			
		};
		
		private Handler mHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					stateTv.setVisibility(View.GONE);
					progressbar.setVisibility(View.GONE);
					connectBtn.setVisibility(View.VISIBLE);
					stateTv.setText("正在连接,请稍候");
					connectBtn.setClickable(true);
					break;
				case 1:
					connectBtn.setVisibility(View.GONE);
					progressbar.setVisibility(View.VISIBLE);
					stateTv.setVisibility(View.VISIBLE);
					stateTv.setText("正在连接,请稍候");
					connectBtn.setClickable(true);
					break;
				case 2:
					progressbar.setVisibility(View.GONE);
					connectBtn.setVisibility(View.GONE);
					stateTv.setVisibility(View.VISIBLE);
					stateTv.setText("连接成功");
					connectBtn.setClickable(false);
					break;
				default:
					break;
				}
			}
			
		};
		
		private void updateConState(){
			new Handler().postDelayed(new Runnable() {
							
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!stateTv.getText().equals("连接成功")) {
						stateTv.setVisibility(View.GONE);
						progressbar.setVisibility(View.GONE);
						connectBtn.setVisibility(View.VISIBLE);
						bleUtility.disconnect();
						Utility.showToast(context, "连接失败", Toast.LENGTH_SHORT);
					}else {
						Message msg=new Message();
						msg.what=2;
						mHandler.sendMessage(msg);
						
					}
					
				}
			}, 10000);
		}
}
