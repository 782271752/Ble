package com.li.ble220.Util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.li.ble220.DB.DBManager;
import com.li.ble220.Entity.SituaEntity;
import com.li.ble220.Entity.TimerEntity;
import com.li.ble220.Fragment.BalconyFragment;
import com.li.ble220.Fragment.BedOneFragment;
import com.li.ble220.Fragment.BedTwoFragment;
import com.li.ble220.Fragment.KitWCFragment;
import com.li.ble220.Fragment.LivingFragment;

public class SituaTask extends TimerTask{
	
	private Timer controllerTimer;
	// 定时设备启动的周期时间
	public static final long period = 24 * 60 * 60 * 1000; // 设置周期为24小时
	private SituaEntity equipmentTimer = null;
	private Context context = null;
//	private static HashMap<String, String> map = new HashMap<String, String>();
	private String location,state,eid;
	private LivingFragment livingFragment;
	private BedOneFragment bedOneFragment;
	private BedTwoFragment bedTwoFragment;
	private BalconyFragment balconyFragment;
	private KitWCFragment kitWCFragment;
	private Activity activity;
	private DBManager dbManager;
	private String ischeck;
	
	public SituaTask(Context context){
		this.context=context;
		activity=(Activity)context;
//		livingFragment=(LivingFragment)activity.getFragmentManager().findFragmentByTag("living");
//		bedOneFragment=(BedOneFragment)activity.getFragmentManager().findFragmentByTag("bedone");
//		bedTwoFragment=(BedTwoFragment)activity.getFragmentManager().findFragmentByTag("bedtwo");
//		balconyFragment=(BalconyFragment)activity.getFragmentManager().findFragmentByTag("bal");
//		kitWCFragment=(KitWCFragment)activity.getFragmentManager().findFragmentByTag("kw");
		dbManager=new DBManager(context);
	}
	
	public void start(long delay, SituaEntity equipmentTimer) {
		this.equipmentTimer = equipmentTimer;
		ischeck=equipmentTimer.getIscheck();
		controllerTimer = new Timer();
		controllerTimer.schedule(this, delay, period); // 调用timer.schedule方法（延时启动）
		

	}

	public void start(Date time, SituaEntity equipmentTimer) {
		this.equipmentTimer = equipmentTimer;
		Log.e("SituaTaskl----ischeck", equipmentTimer.getIscheck()+"");
		ischeck=equipmentTimer.getIscheck();
		controllerTimer = new Timer();
		controllerTimer.schedule(this, time, period); // 调用timer.schedule方法（定时启动）
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message message = new Message();
		message.what = 1;
		handler.sendMessage(message);
	}
	
	// 结束定时任务
	public void end() {
		Message message = new Message();
		message.what = 2;
		handler.sendMessage(message);
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String[] eids=equipmentTimer.getEids().split(",");
				String location="";
				String value="";
				for (int i = 0; i < eids.length; i++) {
					
					if (ischeck.equals("100")) {
						location=dbManager.getLocationId(eids[i]);
						Log.e("location----eid----ischeck",location+"---"+eids[i]+"----"+equipmentTimer.getIscheck());
						
						if (equipmentTimer.getState().equals("100")) {
							value=dbManager.getEquipValues(eids[i]).getTonValue();
						}else {
							value=dbManager.getEquipValues(eids[i]).getToffValue();
						}
						Log.e("--value---", value+"");
						startControl(location, value);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				}
				break;
			case 2:
				controllerTimer.cancel();
				break;
			}
		}
	};
	
	public void startControl(String location,String values){
		switch (Integer.parseInt(location)) {
		case 1:
			livingFragment=(LivingFragment)activity.getFragmentManager().findFragmentByTag("living");
			
			if (livingFragment!=null) {
				livingFragment.sendValue(Integer.parseInt(values));
			}else {
				Utility.showToast(context, "连接不到客厅控制设备", Toast.LENGTH_LONG);
			}
			
			break;
		case 2:
			bedOneFragment=(BedOneFragment)activity.getFragmentManager().findFragmentByTag("bedone");
			
			if (bedOneFragment!=null) {
				bedOneFragment.sendValues(Integer.parseInt(values));
			}else {
				Utility.showToast(context, "连接不到卧室1控制设备", Toast.LENGTH_LONG);
			}
			break;
		case 3:
			bedTwoFragment=(BedTwoFragment)activity.getFragmentManager().findFragmentByTag("bedtwo");
			
			if (bedTwoFragment!=null) {
				bedTwoFragment.sendValues(Integer.parseInt(values));
			}else {
				Utility.showToast(context, "连接不到卧室2控制设备", Toast.LENGTH_LONG);
			}
			break;
		case 4:
			balconyFragment=(BalconyFragment)activity.getFragmentManager().findFragmentByTag("bal");
			
			if (balconyFragment!=null) {
				balconyFragment.sendValues(Integer.parseInt(values));
			}else {
				Utility.showToast(context, "连接不到阳台控制设备", Toast.LENGTH_LONG);
			}
			
			break;
		case 5:
			kitWCFragment=(KitWCFragment)activity.getFragmentManager().findFragmentByTag("kw");
			if (kitWCFragment!=null) {
				kitWCFragment.sendValues(Integer.parseInt(values));
			}else {
				Utility.showToast(context, "连接不到厨卫控制设备", Toast.LENGTH_LONG);
			}
			break;
		
		}
	}

}
