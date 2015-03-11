package com.li.ble220.Fragment;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.DB.DBManager;
import com.li.ble220.Util.BleUtility;
import com.li.ble220.Util.ConnectUtility;
import com.li.ble220.Util.Utility;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

public class BalconyFragment extends Fragment implements OnClickListener{
	private View balView;
	//00:17:EA:93:BF:24
	private Context context;
	private BleUtility bleUtility;
	private ConnectUtility connectUtility;
	private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private Button one,two,connectBtn;
    private String state,address="00:17:EA:93:BF:24";
    private DBManager dbManager;
    private Typeface typeface;
    private ProgressBar progressbar;
    private TextView stateTv;
    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=(MainActivity)getActivity();
		context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		
		dbManager=new DBManager(context);
		typeface=Utility.getTypeface(context);
		bleUtility=new BleUtility(context, "00:17:EA:93:BF:24");
		connectUtility=new ConnectUtility(context,"00:17:EA:93:BF:24",bleUtility);
//		connectUtility.connectBle();
		state=dbManager.getBleState(address);
		Log.e("阳台", state);
	}
    
    @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		context.unregisterReceiver(mGattUpdateReceiver);
		dbManager.updateState(address,"0");
		dbManager.updateContect(address, "0");
		super.onDestroy();
	}
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		balView=inflater.inflate(R.layout.balcony, container,false);
		one=(Button)balView.findViewById(R.id.balcony_on);
		one.setTypeface(typeface);
		one.setOnClickListener(this);
		two=(Button)balView.findViewById(R.id.balcony_off);
		two.setTypeface(typeface);
		two.setOnClickListener(this);
		progressbar=(ProgressBar)balView.findViewById(R.id.bal_bar);
		stateTv=(TextView)balView.findViewById(R.id.bal_state);
		stateTv.setTypeface(typeface);
		connectBtn=(Button)balView.findViewById(R.id.bal_connect_btn);
		connectBtn.setTypeface(typeface);
		connectBtn.setOnClickListener(this);
		connectBtn.setVisibility(View.GONE);
		
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
		return balView;
	}
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
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
	
	private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("BAL_GATT_CONNECTED");
        intentFilter.addAction("BAL_GATT_DISCONNECTED");
        intentFilter.addAction("BAL_GATT_SERVICES_DISCOVERED");
        intentFilter.addAction("BAL_DATA_AVAILABLE");
        return intentFilter;
    }
	
	private BroadcastReceiver  mGattUpdateReceiver=new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			
			final String action = intent.getAction();
			System.out.println("action = " + action);
			if (bleUtility.ACTION_GATT_CONNECTED.equals(action)) {

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

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.balcony_on:
			connectUtility.writeCharactic(context,1);
			break;
		case R.id.balcony_off:
			connectUtility.writeCharactic(context,0);
			break;
		case R.id.bal_connect_btn:
			
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
		default:
			break;
		}
		
	}
	
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
	
	public void sendValues(int values){
		connectUtility.writeCharactic(context, values);
	}
}
