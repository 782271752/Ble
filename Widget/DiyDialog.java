package com.li.ble220.Widget;

import java.util.Date;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.Adapter.BleAdapter;
import com.li.ble220.DB.DBManager;
import com.li.ble220.Entity.Equipment;
import com.li.ble220.Entity.TimerEntity;
import com.li.ble220.Fragment.TimerFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class DiyDialog extends Dialog{

	Context context;
	private ListView lv;
	private DBManager dbManager;
	private String location;
	public static String[] equipmentName = null;
//	public static String[] 
	public TimerEntity timerEntity;
	public static String[] equipmentId=null;
	public static String[] locations=null;
	public TimerFragment timerFragment;
	private Equipment equipment;
    public DiyDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public DiyDialog(Context context, int theme,DBManager dbManager){
        super(context, theme);
        this.context = context;
        this.dbManager=dbManager;
       
        
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.time_add);
        timerEntity=new TimerEntity();
        lv = (ListView)findViewById(R.id.time_add_list);
        lv.setAdapter(new BleAdapter(context, dbManager.getBle()));
        locations=dbManager.getEquipmentLocation();
        Activity activity=(Activity)context;
        equipment=new Equipment();
        timerFragment=(TimerFragment)activity.getFragmentManager().findFragmentByTag("time");
        
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				location=dbManager.getBle().get(position).getBid();
				Log.e("---location---", location);
				equipmentName=dbManager.getEquipmentNameByLocation(location);
				equipmentId=dbManager.getEquipmentEid(location);
				
				for (int i = 0; i < equipmentId.length; i++) {
					Log.e("---equipmentId----",equipmentId[i]+"");
				}
				
				
				timerEntity.setEid(equipmentId[0]);
				timerEntity.setLocation(location+"");
				equipment=dbManager.getEquipValues(equipmentId[0]);
				
				timerEntity.setValuse(equipment.getTonValue());
				Log.e("----tvalues---", equipment.getTonValue());
				timerEntity.setState("100");
				timerEntity.setName(equipmentName[0]);
				timerEntity.setIsClosed("100");
				DiyDialog.this.dismiss();
				new AlertDialog.Builder(context).setTitle("选择定时设备")
				.setSingleChoiceItems(equipmentName, 0, 
						new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						timerEntity.setLocation(which+"");
						timerEntity.setEid(equipmentId[which]);
						timerEntity.setName(equipmentName[which]);
						equipment=dbManager.getEquipValues(equipmentId[which]);
						timerEntity.setValuse(equipment.getTonValue());
						Log.e("----tvalues---", equipment.getTonValue());
					}
				}).setPositiveButton("设置", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new TimePickerDialog(context,
								mTimeSetListener, new Date().getHours(),
								new Date().getMinutes(), false).show();
						
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						DiyDialog.this.show();
					}
				}).show();
			}
		});
        
    }
    
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		boolean flag = false;

		@Override
		public void onTimeSet(final TimePicker view, int hourOfDay, int minute) {
			if (flag) {
				flag = false;
				return;
			}
			flag = true;
			timerEntity.setStartTime(hourOfDay + ":" + minute +"");
			Log.e("----------------", hourOfDay + ":" + minute + ":00");
			new AlertDialog.Builder(context)
					.setTitle("设备状态")
					.setSingleChoiceItems(new String[] { "开", "关" }, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									timerEntity.setState(which == 0 ? "100" : "0");
									
									timerEntity.setValuse(which==0?equipment.getTonValue():equipment.getToffValue());
									Log.e("--------tt------", timerEntity.getValuse());
									timerEntity.setIsClosed("100");
								}
							}).setPositiveButton("添加", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Boolean success = dbManager
									.addEquipmentTimer(timerEntity);
							if (success) {
								timerFragment.refreshTimer(0);
								Toast.makeText(context, "设备定时任务添加成功", Toast.LENGTH_SHORT).show();
//								refreshTimer();
								MainActivity.timer.start();
								
//								livingFragment.settex("kongzhi");
								
							} else if (!success) {
								Toast.makeText(context, "改定时任务已存在", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(context, "添加出错！请重启服务器后再试", Toast.LENGTH_SHORT)
										.show();
							}
							dialog.dismiss();
						}
					}).setNegativeButton("取消", null).show();
		}
	};
}
