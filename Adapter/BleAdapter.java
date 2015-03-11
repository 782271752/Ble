package com.li.ble220.Adapter;

import java.util.List;

import com.li.ble220.R;
import com.li.ble220.Entity.BleEntity;
import com.li.ble220.Util.Utility;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BleAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<BleEntity> bleList;
	private Typeface typeface;
	public BleAdapter(Context context,List<BleEntity> bList){
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.bleList=bList;
		typeface=Utility.getTypeface(context);
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bleList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BleHolder holder=new BleHolder();
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.time_add_item,null);
			holder.name=(TextView)convertView.findViewById(R.id.location);
			holder.address=(TextView)convertView.findViewById(R.id.macaddress);
			holder.state=(TextView)convertView.findViewById(R.id.state);
			convertView.setTag(holder);//绑定ViewHolder对象
		}else {
			holder=(BleHolder)convertView.getTag();
		}
		holder.address.setText(bleList.get(position).getAddress());
		holder.address.setTypeface(typeface);
		if (bleList.get(position).getIsconnect().equals("0")) {
			holder.state.setText("未连接");
			holder.state.setTextColor(context.getResources().getColor(R.color.red));
		}else {
			holder.state.setText("已连接");
			holder.state.setTextColor(context.getResources().getColor(R.color.green));
		}
		holder.state.setTypeface(typeface);
		holder.name.setTypeface(typeface);
		holder.name.setText(bleList.get(position).getLocation());
		
		return convertView;
	}
	
	class BleHolder{
		public TextView name;
		public TextView address;
		public TextView state;
	}

}
