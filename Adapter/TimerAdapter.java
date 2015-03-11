package com.li.ble220.Adapter;

import java.util.List;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.DB.DBManager;
import com.li.ble220.Entity.Equipment;
import com.li.ble220.Entity.TimerEntity;
import com.li.ble220.Fragment.TimerFragment;
import com.li.ble220.Util.Utility;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class TimerAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<TimerEntity> timeList;
	private Typeface typeface;
	private DBManager dbManager;
	private TimerEntity newTime,oldTime;
	private int hour, minute;
	private TimerFragment timerFragment;
	private Activity activity;
	private boolean isCheck;
	private Equipment equipment;

	public TimerAdapter(Context context,List<TimerEntity> bList,DBManager dbManager){
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.timeList=bList;
		typeface=Utility.getTypeface(context);
		this.dbManager=dbManager;
		newTime=new TimerEntity();
		oldTime=new TimerEntity();
		activity=(Activity)context;
		timerFragment=(TimerFragment)activity.getFragmentManager().findFragmentByTag("time");
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return timeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return timeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TimeHolder holder=new TimeHolder();
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.timer_item,null);
			holder.location=(TextView)convertView.findViewById(R.id.item_location);
			holder.title=(TextView)convertView.findViewById(R.id.item_title);
			holder.time=(TextView)convertView.findViewById(R.id.item_time);
			holder.switch_btn=(Button)convertView.findViewById(R.id.item_swBtn);
			holder.del_btn=(Button)convertView.findViewById(R.id.item_btn);
			holder.set_btn=(Button)convertView.findViewById(R.id.item_setting_btn);
			holder.close_btn=(Button)convertView.findViewById(R.id.item_select_btn);
			convertView.setTag(holder);//绑定ViewHolder对象
		}else {
			holder=(TimeHolder)convertView.getTag();
		}
		holder.title.setText(timeList.get(position).getName());
		holder.title.setTypeface(typeface);
		holder.time.setTypeface(typeface);
		holder.time.setText(timeList.get(position).getStartTime());
		switch (Integer.parseInt(timeList.get(position).getLocation())) {
		case 1:
			holder.location.setText("客厅");
			break;
		case 2:
			holder.location.setText("卧室1");
			break;
		case 3:
			holder.location.setText("卧室2");
			break;
		case 4:
			holder.location.setText("阳台");
			break;
		case 5:
			holder.location.setText("厨卫");
			break;
		default:
			break;
		}
//		holder.location.setText(timeList.get(position).getLocation());
		holder.location.setTypeface(typeface);
		equipment=dbManager.getEquipValues(timeList.get(position).getEid());
		if (timeList.get(position).getState().equals("100")) {
			holder.switch_btn.setBackground(context.getResources().getDrawable(R.drawable.sun));
			isCheck=true;
//			newTime.setValuse(equipment.getTonValue());
		}else {
			holder.switch_btn.setBackground(context.getResources().getDrawable(R.drawable.sun2));;
			isCheck=false;
//			newTime.setValuse(equipment.getToffValue());
		}
		
		if (timeList.get(position).getIsClosed().equals("100")) {
			holder.close_btn.setBackground(context.getResources().getDrawable(R.drawable.switch_on));
		}else {
			holder.close_btn.setBackground(context.getResources().getDrawable(R.drawable.switch_off));
		}
//		hour=;
//		minute=;
//		Log.e("-----position-----", hour+" "+minute+"");
		newTime.setLocation(timeList.get(position).getLocation());
		
		newTime.setState(timeList.get(position).getState());
		newTime.setName(timeList.get(position).getName());
		newTime.setStartTime(timeList.get(position).getStartTime());
		Log.e("|||||||||||||", timeList.get(position).getValuse()+"--");
		newTime.setValuse(timeList.get(position).getValuse());
		newTime.setIsClosed(timeList.get(position).getIsClosed());
		
		oldTime.setLocation(timeList.get(position).getLocation());
		oldTime.setStartTime(timeList.get(position).getStartTime());
		oldTime.setState(timeList.get(position).getState());
		oldTime.setName(timeList.get(position).getName());
		
		
		
		holder.switch_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (timeList.get(position).getState().equals("0")){
					newTime.setState("100");
					newTime.setValuse(equipment.getTonValue());
				}else {
					newTime.setState("0");
					newTime.setValuse(equipment.getToffValue());
				}
				newTime.setIsClosed(timeList.get(position).getIsClosed());
				newTime.setStartTime(timeList.get(position).getStartTime());
//			Boolean success = dbManager.updateEquipmentTimer(oldTime,
//					newTime);
//				Log.e("id---------------", timeList.get(position).getId());
				Boolean success = dbManager.updateTime(timeList.get(position).getId(), newTime);
				if (success == true) {
					timerFragment.refreshTimer(timerFragment.getSelection());
					Toast.makeText(context, "定时修改成功！", Toast.LENGTH_SHORT).show();
					MainActivity.timer.start();
				} else {
					Toast.makeText(context, "修改出错！请重启服务器后再试", Toast.LENGTH_SHORT).show();
				}
					
			}
		});

		holder.close_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (timeList.get(position).getIsClosed().equals("100")) {
					newTime.setIsClosed("0");
				}else {
					newTime.setIsClosed("100");
				}
				newTime.setState(timeList.get(position).getState());
				newTime.setStartTime(timeList.get(position).getStartTime());
				Boolean success = dbManager.updateTime(timeList.get(position).getId(), newTime);
				if (success == true) {
					timerFragment.refreshTimer(timerFragment.getSelection());
					Toast.makeText(context, "定时修改成功！", Toast.LENGTH_SHORT).show();
					MainActivity.timer.start();
				} else {
					Toast.makeText(context, "修改出错！请重启服务器后再试", Toast.LENGTH_SHORT).show();
				}
			}
		});
		holder.del_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("确定删除该定时？");
				builder.setPositiveButton("删除",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dbManager.deleteEquipmentTimer(timeList.get(position).getId());
								Toast.makeText(context, "该定时记录已删除",
										Toast.LENGTH_SHORT).show();
								timerFragment.refreshTimer(timerFragment.getSelection());
								MainActivity.timer.start();
							}
						});
				builder.setNegativeButton("取消", null);
				builder.show();
				
			}
		});
		
		holder.set_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				new UpdateEquipmentTimerLisenter();
				
				new AlertDialog.Builder(context)
				.setTitle(timeList.get(position).getName())
				.setMessage("是否修改定时时间?")
				.setPositiveButton("修改",  new UpdateEquipmentTimerLisenter(timeList.get(position).getId(),position))
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								
							}
						}).show();
			}
		});
		
		return convertView;
	}
	
	class TimeHolder{
		public TextView location;
		public TextView title;
		public TextView time;
		/**
		 * 灯泡亮灭状态
		 */
		public Button switch_btn;
		public Button del_btn;
		public Button set_btn;
		/**
		 * 定时是否启动
		 */
		public Button close_btn;
	}
	
	private String timeId;
	private int positionId;
	
	// 定时设备修改按钮的监听器
	class UpdateEquipmentTimerLisenter implements DialogInterface.OnClickListener {
		
		public  UpdateEquipmentTimerLisenter(String id,int position){
			timeId=id;
			positionId=position;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Log.e("-------------", hour+"  "+minute+"");
			new TimePickerDialog(context, TimerSettingListener, Integer.parseInt(timeList.get(positionId).getStartTime().split(":")[0]),
					Integer.parseInt(timeList.get(positionId).getStartTime().split(":")[1]), true)
					.show();
		}
	}
	
	private TimePickerDialog.OnTimeSetListener TimerSettingListener = new TimePickerDialog.OnTimeSetListener() {
		boolean flag = false;

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (flag) {
				flag = false;
				return;
			}
			flag = true;
			newTime.setStartTime(hourOfDay + ":" + minute + "");
			newTime.setIsClosed("100");
			newTime.setState(timeList.get(positionId).getState());
			
			newTime.setValuse(timeList.get(positionId).getValuse());
//			if (isCheck) {
//				newTime.setValuse(equipment.getTonValue());
//			}else {
//				newTime.setValuse(equipment.getToffValue());
//			}
			
			Boolean success=dbManager.updateTime(timeId, newTime);
			
			if (success == true) {
				timerFragment.refreshTimer(timerFragment.getSelection());
				Toast.makeText(context, "该定时任务修改成功！", Toast.LENGTH_SHORT).show();
				MainActivity.timer.start();
			} else {
				Toast.makeText(context, "修改出错！请重启服务器后再试", Toast.LENGTH_SHORT).show();
			}
			
		}
	};
}
