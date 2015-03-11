package com.li.ble220.Adapter;

import java.util.List;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.SituaActivity;
import com.li.ble220.Adapter.BleAdapter.BleHolder;
import com.li.ble220.DB.DBManager;
import com.li.ble220.Entity.SituaEntity;
import com.li.ble220.Fragment.SituaFragment;
import com.li.ble220.Util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SituaAdapter extends BaseAdapter {

	private List<SituaEntity> situaList;
	private Context context;
	private LayoutInflater inflater;
	private Typeface typeface;
	private DBManager dbManager;
	private SituaFragment fragment;
	private Activity activity;
	
	public SituaAdapter(Context context,List<SituaEntity> situaList,DBManager dbManager){
		this.context=context;
		this.situaList=situaList;
		inflater = LayoutInflater.from(context);
		typeface=Utility.getTypeface(context);
		dbManager=new DBManager(context);
		this.dbManager=dbManager;
		activity=(Activity)context;
		fragment=(SituaFragment)activity.getFragmentManager().findFragmentByTag("situation");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return situaList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return situaList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	class SituaHolder{
		TextView titleTv;
		Button checkBtn;
		Button setBtn;
		Button delBtn;
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SituaHolder holder=new SituaHolder();
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.situa_item,null);
			holder.titleTv=(TextView)convertView.findViewById(R.id.situa_title);
			holder.checkBtn=(Button)convertView.findViewById(R.id.situa_btn);
			holder.setBtn=(Button)convertView.findViewById(R.id.situa_set_btn);
			holder.delBtn=(Button)convertView.findViewById(R.id.situa_del_btn);
			convertView.setTag(holder);//绑定ViewHolder对象
		}else {
			holder=(SituaHolder)convertView.getTag();
		}
		holder.titleTv.setTypeface(typeface);
		holder.titleTv.setText(situaList.get(position).getName());
		String id=situaList.get(position).getId();
		
		
		
		if (situaList.get(position).getIscheck().equals("100")) {
			holder.checkBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_on));
		}else {
			holder.checkBtn.setBackground(context.getResources().getDrawable(R.drawable.switch_off));
		}
		
		holder.checkBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Log.e("-------ischeck------",situaList.get(position).getIscheck()+"");
				if (situaList.get(position).getIscheck().equals("100")) {
					if (dbManager.updateSitua(situaList.get(position).getId(),"0")) {
						fragment.refresh();
						MainActivity.timer.startSitua();
						
					}
				}else{
					if (dbManager.updateSitua(situaList.get(position).getId(),"100")) {
						fragment.refresh();
						MainActivity.timer.startSitua();
					}
				}
				
			}
		});
		
		
		holder.setBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent(context,SituaActivity.class);
//				Bundle bundle=new Bundle();
//				bundle.putSerializable("situa", situaList.get(position));
//				intent.putExtras(bundle);
//				context.startActivity(intent);
				
//				String equipString=dbManager.getEquipName("21");
//				Log.e("-----------", equipString);
				
				String[] equipNames=dbManager.getEquipNames(situaList.get(position).getEids());
				String[] eids=situaList.get(position).getEids().split(",");
				String[] locations=new String[eids.length];
				for (int i = 0; i < eids.length; i++) {
					locations[i]=dbManager.getLocation(eids[i]);
					Log.e("--------", locations[i]);
				}
				String equip="";
				for (int i = 0; i < equipNames.length; i++) {
					equip+=locations[i]+": "+equipNames[i]+"\n";
				}
				if (situaList.get(position).getState().equals("100")) {
					equip+="状态: 开启\n";
					
				}else{
					equip+="状态: 关闭\n";
				}
				equip+="开启时间："+situaList.get(position).getStartTime()+"\n";
				new AlertDialog.Builder(context).setTitle(situaList.get(position).getName())
				.setMessage(equip)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
					}
				})
				.show();
				
			}
		});
		
		holder.delBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context)
				.setTitle("温馨提示")
				.setMessage("是否要删除该情景模式?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dbManager.delSitua(situaList.get(position).getId());
						fragment.refresh();
						MainActivity.timer.startSitua();
//						}
						
					}
				})
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

}
