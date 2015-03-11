package com.li.ble220.Fragment;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.MainActivity.ServiceReciver;
import com.li.ble220.Util.Utility;

import android.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

public class ConnectFragment extends Fragment implements OnClickListener{
	
	private View connectView;
	private TextView titleTv,addressTv,introduTv;
	private Typeface typeface;
	private Context mcontext;
	ServiceReciver br;
	IntentFilter filter;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mcontext=(MainActivity)getActivity();
		typeface=Utility.getTypeface(mcontext);
		
		filter = new IntentFilter("com.li.ble.address"); 
		br = new ServiceReciver();
		mcontext.registerReceiver(br, filter);
		
		
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mcontext.unregisterReceiver(br);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}





	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		connectView=inflater.inflate(R.layout.connection, container,false);
		titleTv=(TextView)connectView.findViewById(R.id.conn_title);
		titleTv.setTypeface(typeface);
		introduTv=(TextView)connectView.findViewById(R.id.textView);
		introduTv.setTypeface(typeface);
		addressTv=(TextView)connectView.findViewById(R.id.con_address);
		addressTv.setTypeface(typeface);
		addressTv.setOnClickListener(this);
		
		String address=Utility.getLocalIpAddress();
		if (address.equals("")) {
//			Utility.showToast(mcontext, "连接错误,请检查wifi", Toast.LENGTH_LONG);
//			addressTv.setText("");
			Message msg=new Message();
			msg.what=0;
			mHandler.sendMessage(msg);
		}else {
			Message msg=new Message();
			msg.what=1;
			mHandler.sendMessage(msg);
		}
		
		return connectView;
	}
	
	public class ServiceReciver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String address=Utility.getLocalIpAddress();
					Log.e("---------------", address);
					if (address.equals("")) {
//						Utility.showToast(mcontext, "连接错误,请检查wifi", Toast.LENGTH_LONG);
//						addressTv.setText("");
						Message msg=new Message();
						msg.what=0;
						mHandler.sendMessage(msg);
					}else {
//						addressTv.setText(address);
						Message msg=new Message();
						msg.what=1;
						mHandler.sendMessage(msg);
					}
					
				}
			}, 1000);
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.con_address:
			Message msg=new Message();
			msg.what=2;
			mHandler.sendMessage(msg);
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (Utility.getLocalIpAddress().equals("")) {
						Message msg=new Message();
						msg.what=0;
						mHandler.sendMessage(msg);
					}else {
						Message msg=new Message();
						msg.what=1;
						mHandler.sendMessage(msg);
					}
					
				}
			}, 3000);
			
			break;

		default:
			break;
		}
		
	}
	
	Handler mHandler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				addressTv.setText("获取不到IP，请重试");
				break;
			case 1:
				addressTv.setText(Utility.getLocalIpAddress());
				break;
			case 2:
				addressTv.setText("IP获取中，请稍候....");
				break;
			default:
				break;
			}
		}
		
	};
}
