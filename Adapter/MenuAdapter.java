package com.li.ble220.Adapter;

import com.li.ble220.R;
import com.li.ble220.Util.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater layoutInflater;
	private String listName[]={"控制台","定时","情景模式","手机连接","设置"};
	private int icons[]={R.drawable.control,R.drawable.bell,R.drawable.settings,R.drawable.connection,R.drawable.cog};
	
	public MenuAdapter(Context context){
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
		// TODO Auto-generated method stub
		ViewHolder holder=new ViewHolder();
		if(convertView==null){
			convertView = layoutInflater.inflate(R.layout.menu_fragment_item, null);
			holder.title=(TextView)convertView.findViewById(R.id.title);
			holder.image=(ImageView)convertView.findViewById(R.id.image);
			
			holder.image.setImageDrawable(context.getResources().getDrawable(icons[position]));
			holder.title.setText(listName[position]);
			holder.title.setTypeface(Utility.getTypeface(context));
			
		}
		return convertView;
	}
	
	class ViewHolder 
	{ 
	    public TextView title; 
	    public ImageView image; 
	} 
}
