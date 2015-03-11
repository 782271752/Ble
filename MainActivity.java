package com.li.ble220;

import com.li.ble220.DB.DBManager;
import com.li.ble220.Fragment.BalconyFragment;
import com.li.ble220.Fragment.BedOneFragment;
import com.li.ble220.Fragment.BedTwoFragment;
import com.li.ble220.Fragment.KitWCFragment;
import com.li.ble220.Fragment.LivingFragment;
import com.li.ble220.Fragment.MenuFragment;
import com.li.ble220.Service.ConnService;
import com.li.ble220.Util.TimerUtilty;
import com.li.ble220.Util.Utility;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static DBManager dbManager = null;
	public static TimerUtilty timer;
	private LivingFragment livingFragment;
	private BedOneFragment bedOneFragment;
	private BedTwoFragment bedTwoFragment;
	private BalconyFragment balconyFragment;
	private KitWCFragment kitWCFragment;
	IntentFilter filter;
	ServiceReciver br;
	String location,value;
	Intent intent;
	public String[] address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setContentView(R.layout.activity_main);
		dbManager = new DBManager(this);
		timer=new TimerUtilty(this, dbManager);
		
		
		
		filter = new IntentFilter("com.li.ble"); 
		br = new ServiceReciver();
		this.registerReceiver(br, filter);
		
		intent=new Intent(this,ConnService.class);
        startService(intent);
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				timer.start();
				timer.startSitua();
			}
			
		};
		showMenu();
		
		
	}
	
	/**
	 * 显示左侧的菜单碎片
	 */
	private void showMenu(){
		MenuFragment menuFragment = new MenuFragment();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();	

		fragmentTransaction.replace(R.id.menu, menuFragment);
		fragmentTransaction.commit();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 弹出确定退出对话框
			// 链式编程
			new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("确定退出程序吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(timer != null)
								timer.end();
								timer.endSitua();
								
								BluetoothAdapter  mBluetoothAdapter = BluetoothAdapter 
						                .getDefaultAdapter(); 
								address=dbManager.getAddress();
								
						        for (int i = 0; i < address.length; i++) {
									dbManager.updateState(address[i], "0");
									dbManager.updateContect(address[i], "0");
									Log.e("-----------",address[i]);
								}
								if (mBluetoothAdapter.isEnabled()) {
									mBluetoothAdapter.disable();
								}
								finish();
								System.exit(0);
						}
					}).setNeutralButton("最小化", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent home = new Intent(Intent.ACTION_MAIN);  
							home.addCategory(Intent.CATEGORY_HOME);   
							startActivity(home);  
							
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).show();
			return true;
		}
		// 继续执行父类的其他点击事件
		return super.onKeyDown(keyCode, event);
	}
	
	public void startControl(String location,String values){
		switch (Integer.parseInt(location)) {
		case 1:
			livingFragment=(LivingFragment)getFragmentManager().findFragmentByTag("living");
			if (livingFragment!=null) {
				livingFragment.sendValue(Integer.parseInt(values));
			}else{
				Utility.showToast(MainActivity.this, "连接不到客厅控制设备", Toast.LENGTH_LONG);
			}
			break;
		case 2:
			bedOneFragment=(BedOneFragment)getFragmentManager().findFragmentByTag("bedone");
			if (bedOneFragment!=null) {
				bedOneFragment.sendValues(Integer.parseInt(values));
			}else{
				Utility.showToast(MainActivity.this, "连接不到卧室1控制设备", Toast.LENGTH_LONG);
			}
			
			break;
		case 3:
			bedTwoFragment=(BedTwoFragment)getFragmentManager().findFragmentByTag("bedtwo");
			
			if (bedTwoFragment!=null) {
				bedTwoFragment.sendValues(Integer.parseInt(values));
			}else {
				Utility.showToast(MainActivity.this, "连接不到卧室2控制设备", Toast.LENGTH_LONG);
			}
			
			break;
		case 4:
			balconyFragment=(BalconyFragment)getFragmentManager().findFragmentByTag("bal");
			
			if (balconyFragment!=null) {
				balconyFragment.sendValues(Integer.parseInt(values));
			}else {
				Utility.showToast(MainActivity.this, "连接不到阳台控制设备", Toast.LENGTH_LONG);
			}
			
			break;
		case 5:
			kitWCFragment=(KitWCFragment)getFragmentManager().findFragmentByTag("kw");
			if (kitWCFragment!=null) {
				kitWCFragment.sendValues(Integer.parseInt(values));
			}else {
				Utility.showToast(MainActivity.this, "连接不到厨卫控制设备", Toast.LENGTH_LONG);
			}
			
			break;
		
		}
	}

	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(br);
		stopService(intent);
		
		
	}

	
	


	public class ServiceReciver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			location=intent.getExtras().getString("location");
			value=intent.getExtras().getString("value");
			startControl(location,value);
		}
	}
				
}
