package com.li.ble220.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.li.ble220.DB.DBManager;
import com.li.ble220.Entity.SituaEntity;
import com.li.ble220.Entity.TimerEntity;

import android.content.Context;
import android.util.Log;


public class TimerUtilty {

	private TimerEntity[] equipmentTimers = null;	//	定时设备
	private TimeTask[] controllerTimers = null;
	
	private SituaTask[] controllerSitua=null;
	private List<SituaEntity> situaList;
	private Context context = null;
	private DBManager dbManager;

	public TimerUtilty(Context context,DBManager dbManager) {
		this.context = context;
		this.dbManager=dbManager;
		situaList=new ArrayList<SituaEntity>();
	}

	// 启动所有的定时任务
	public void start() {
		//	关闭原来的定时任务、启动新的定时任务
		end();
		Calendar calendar = Calendar.getInstance();
		
		int hour, minute;
		
		equipmentTimers = dbManager.getTimer();
		controllerTimers = new TimeTask[equipmentTimers.length];
		for (int i = 0; i < equipmentTimers.length; i++) {
			hour = Integer.parseInt(equipmentTimers[i].getStartTime().split(":")[0]);
			minute = Integer.parseInt(equipmentTimers[i].getStartTime().split(":")[1]);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, 0);
			Date time = calendar.getTime();
			Log.e("--time--", time.getTime()+"");
			controllerTimers[i] = new TimeTask(context);
			if (time.getTime() < new Date().getTime()) {
				controllerTimers[i].start(
						time.getTime() + TimeTask.period - new Date().getTime(),
						equipmentTimers[i]);
			} else {
//				controllerTimers[i].start(time, equipmentTimers[i]);
				time.setTime(time.getTime()+100*i);
				controllerTimers[i].start(time, equipmentTimers[i]);
				
			}
		}
		
	}
	public void startSitua(){
		endSitua();
		Calendar scalendar=Calendar.getInstance();
		int shour,sminute;
		situaList=dbManager.getSitua();
		Log.e("------", situaList.size()+"");
		controllerSitua=new SituaTask[situaList.size()];
		for (int i = 0; i <situaList.size(); i++) {
			shour=Integer.parseInt(situaList.get(i).getStartTime().split(":")[0]);
			sminute=Integer.parseInt(situaList.get(i).getStartTime().split(":")[1]);
			
			scalendar.set(Calendar.HOUR_OF_DAY, shour);
			scalendar.set(Calendar.MINUTE, sminute);
			scalendar.set(Calendar.SECOND, 0);
			
			Date time = scalendar.getTime();
			controllerSitua[i]=new SituaTask(context);
			if (time.getTime()<new Date().getTime()) {
				controllerSitua[i].start(time.getTime()+SituaTask.period-new Date().getTime(),
						situaList.get(i)
						);
			}else {
				time.setTime(time.getTime()+100*i);
				controllerSitua[i].start(time,situaList.get(i));
				Log.e("TimerUtil----ischeck", situaList.get(i).getIscheck()+"");
			}
			
		}
	}

	// 取消所有的定时任务
	public void end() {
		if (controllerTimers != null) {
			for (TimeTask controllerTimer : controllerTimers) {
				if (controllerTimer != null)
					controllerTimer.end();
			}
		}
	}
	public void endSitua(){
		if (controllerSitua!=null) {
			for (SituaTask task:controllerSitua) {
				if (task!=null) {
					task.end();
				}
			}
		}
	}
}
