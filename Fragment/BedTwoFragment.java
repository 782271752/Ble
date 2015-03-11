package com.li.ble220.Fragment;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.DB.DBManager;
import com.li.ble220.Util.BleUtility;
import com.li.ble220.Util.ConnectUtility;
import com.li.ble220.Util.Utility;

import android.app.Fragment;
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

public class BedTwoFragment extends Fragment implements OnClickListener{
	private View bedView;
	private String address="00:17:EA:8F:88:6D";
	private Context context;
	private BleUtility bleUtility;
	private ConnectUtility connectUtility;
	private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private Button one,two,three,four,one_off,two_off,connect_btn;
    private Typeface typeface;
    private TextView connect_state;
    private ProgressBar progressBar;
    private String state;
    private DBManager dbManager;
    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=(MainActivity)getActivity();
		context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		bleUtility=new BleUtility(context, "00:17:EA:8F:88:6D");
		connectUtility=new ConnectUtility(context,"00:17:EA:8F:88:6D",bleUtility);
//		connectUtility.connectBle();
		typeface=Utility.getTypeface(context);
		dbManager=new DBManager(context);
		state=dbManager.getBleState(address);
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
		bedView=inflater.inflate(R.layout.bedroom_two, container,false);
		one=(Button)bedView.findViewById(R.id.bed2_one);
		one.setTypeface(typeface);
		one.setOnClickListener(this);
		two=(Button)bedView.findViewById(R.id.bed2_two);
		two.setTypeface(typeface);
		two.setOnClickListener(this);
		three=(Button)bedView.findViewById(R.id.bed2_three);
		three.setTypeface(typeface);
		three.setOnClickListener(this);
		four=(Button)bedView.findViewById(R.id.bed2_four);
		four.setTypeface(typeface);
		four.setOnClickListener(this);
		
		one_off=(Button)bedView.findViewById(R.id.bed2_one_off);
		one_off.setTypeface(typeface);
		one_off.setOnClickListener(this);
		
		two_off=(Button)bedView.findViewById(R.id.bed2_two_off);
		two_off.setTypeface(typeface);
		two_off.setOnClickListener(this);
		
		connect_btn=(Button)bedView.findViewById(R.id.bedt_connect_btn);
		connect_btn.setTypeface(typeface);
		connect_btn.setOnClickListener(this);
		connect_btn.setVisibility(View.GONE);
		
		progressBar=(ProgressBar)bedView.findViewById(R.id.bedt_bar);
		connect_state=(TextView)bedView.findViewById(R.id.bedt_state);
		connect_state.setTypeface(typeface);
		connect_state.setOnClickListener(this);
		
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
		updateConstate();
		return bedView;
	}
	
	private  Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				connect_state.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
				connect_btn.setVisibility(View.VISIBLE);
				connect_state.setText("正在连接,请稍候");
				connect_btn.setClickable(true);
				break;
			case 1:
				connect_btn.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				connect_state.setVisibility(View.VISIBLE);
				connect_state.setText("正在连接,请稍候");
				connect_btn.setClickable(true);
				break;
			case 2:
				progressBar.setVisibility(View.GONE);
				connect_btn.setVisibility(View.GONE);
				connect_state.setVisibility(View.VISIBLE);
				connect_state.setText("连接成功");
				connect_btn.setClickable(false);
				break;
			default:
				break;
			}
		}
		
	};
	private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("BED2_GATT_CONNECTED");
        intentFilter.addAction("BED2_GATT_DISCONNECTED");
        intentFilter.addAction("BED2_GATT_SERVICES_DISCOVERED");
        intentFilter.addAction("BED2_DATA_AVAILABLE");
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
		case R.id.bed2_one:
			connectUtility.writeCharactic(context,1);
			break;
		case R.id.bed2_two:
			connectUtility.writeCharactic(context,3);
			break;
		case R.id.bed2_three:
			connectUtility.writeCharactic(context,0);
			break;
		case R.id.bed2_four:
			connectUtility.writeCharactic(context,8);
			break;
		case R.id.bed2_one_off:
			sendValues(2);
			break;
		case R.id.bed2_two_off:
			sendValues(4);
			break;
		case R.id.bedt_connect_btn:
			new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					connectUtility.connectBle();
				}
				
			}.start();
			Message msg=new Message();
			msg.what=1;
			mHandler.sendMessage(msg);
			updateConstate();
			
			
			break;
		default:
			break;
		}
		
	}
	
	private void updateConstate(){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!connect_state.getText().equals("连接成功")) {
					connect_state.setVisibility(View.GONE);
					progressBar.setVisibility(View.GONE);
					connect_btn.setVisibility(View.VISIBLE);
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
