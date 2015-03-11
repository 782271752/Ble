package com.li.ble220.Adapter;

import com.li.ble220.R;
import com.li.ble220.Adapter.MenuAdapter.ViewHolder;
import com.li.ble220.Util.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HouseAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater layoutInflater;
	private String listName[]={"øÕÃ¸","Œ‘ “1","Œ‘ “2","—ÙÃ®","≥¯Œ¿",};
	
	public HouseAdapter(Context context){
		this.context=context;
		layoutInflater = LayoutInflater.from(context);
	};
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listName.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listName[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=new ViewHolder();
		if(convertView==null){
			convertView = layoutInflater.inflate(R.layout.control_fragment_item, null);
			holder.title=(TextView)convertView.findViewById(R.id.control_item);
			holder.title.setTypeface(Utility.getTypeface(context));
			holder.title.setText(listName[position]);
		}
		return convertView;
	}

	class ViewHolder 
	{ 
	    public TextView title; 
	} 
}
