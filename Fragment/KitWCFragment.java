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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class KitWCFragment extends Fragment implements OnClickListener{
	private View KWView;
	private String address="00:17:EA:93:89:DC";
	private Context context;
	private BleUtility bleUtility;
	private ConnectUtility connectUtility;
	private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private Button one,two,three,four,one_off,two_off,three_off,connectBtn;
    private DBManager dbManager;
    private String state;
    private Typeface typeface;
    private ProgressBar progressbar;
    /**
     * 连接状态
     */
    private TextView stateTv;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=(MainActivity)getActivity();
		context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		dbManager=new DBManager(context);
		bleUtility=new BleUtility(context, address);
		connectUtility=new ConnectUtility(context,address,bleUtility);
		typeface=Utility.getTypeface(context);
		state=dbManager.getBleState(address);
//		connectUtility.connectBle();
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
        intentFilter.addAction("KW_GATT_CONNECTED");
        intentFilter.addAction("KW_GATT_DISCONNECTED");
        intentFilter.addAction("KW_GATT_SERVICES_DISCOVERED");
        intentFilter.addAction("KW_DATA_AVAILABLE");
        return intentFilter;
    }
    
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
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		KWView=inflater.inflate(R.layout.kitchen_toilet, container,false);
		one=(Button)KWView.findViewById(R.id.kw_one);
		one.setTypeface(typeface);
		one.setOnClickListener(this);
		two=(Button)KWView.findViewById(R.id.kw_two);
		two.setTypeface(typeface);
		two.setOnClickListener(this);
		three=(Button)KWView.findViewById(R.id.kw_three);
		three.setTypeface(typeface);
		three.setOnClickListener(this);
		four=(Button)KWView.findViewById(R.id.kw_four);
		four.setTypeface(typeface);
		four.setOnClickListener(this);
		
		one_off=(Button)KWView.findViewById(R.id.kw_one_off);
		one_off.setTypeface(typeface);
		one_off.setOnClickListener(this);
		two_off=(Button)KWView.findViewById(R.id.kw_two_off);
		two_off.setTypeface(typeface);
		two_off.setOnClickListener(this);
		
		three_off=(Button)KWView.findViewById(R.id.kw_three_off);
		three_off.setTypeface(typeface);
		three_off.setOnClickListener(this);
		
		connectBtn=(Button)KWView.findViewById(R.id.kw_connect_btn);
		connectBtn.setTypeface(typeface);
		connectBtn.setOnClickListener(this);
		connectBtn.setVisibility(View.GONE);//将连接按钮设置为不可见
		progressbar=(ProgressBar)KWView.findViewById(R.id.kw_bar);
		stateTv=(TextView)KWView.findViewById(R.id.kw_state);
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
		return KWView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.kw_one:
			connectUtility.writeCharactic(context,1);
			break;
		case R.id.kw_two:
			connectUtility.writeCharactic(context,3);
			break;
		case R.id.kw_three:
			connectUtility.writeCharactic(context,5);
			break;
		case R.id.kw_one_off:
			connectUtility.writeCharactic(context,8);
			break;
		case R.id.kw_two_off:
			connectUtility.writeCharactic(context,8);
			break;
		case R.id.kw_three_off:
			connectUtility.writeCharactic(context,8);
			break;
		case R.id.kw_four:
			connectUtility.writeCharactic(context,8);
			break;
		
		case R.id.kw_connect_btn:
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
	
	/**
	 * 广播接收类，解析来自BluetoothLeService的广播
	 */
	private BroadcastReceiver  mGattUpdateReceiver=new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				
				final String action = intent.getAction();
				System.out.println("action = " + action);
				if (bleUtility.ACTION_GATT_CONNECTED.equals(action)) {
//					mConnected = true;
//					updateConnectionState(R.string.connected);
//					invalidateOptionsMenu();
//					Utility.showToast(context, "连接成功", Toast.LENGTH_SHORT);
				} else if (bleUtility.ACTION_GATT_DISCONNECTED
						.equals(action)) {
//					mConnected = false;
//					updateConnectionState(R.string.disconnected);
//					invalidateOptionsMenu();
//					clearUI();
//					Utility.showToast(context, "连接失败", Toast.LENGTH_SHORT);
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
		
		public void sendValues(int values){
			connectUtility.writeCharactic(context, values);
		}
}
