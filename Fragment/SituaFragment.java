package com.li.ble220.Fragment;

import java.util.ArrayList;
import java.util.List;




import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.SituaActivity;
import com.li.ble220.Adapter.SituaAdapter;
import com.li.ble220.DB.DBManager;
import com.li.ble220.Entity.SituaEntity;
import com.li.ble220.Util.TimerUtilty;
import com.li.ble220.Util.Utility;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class SituaFragment extends Fragment implements OnClickListener{

	private View situaView;
	private Typeface typeface;
	private Context context;
	private TextView title;
	private Button add,refresh;
	private GridView situaGv;
	private SituaAdapter adapter;
	private List<SituaEntity> situaList;
	private DBManager dbManager;
	private Activity activity;
	public static TimerUtilty timer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=(MainActivity)getActivity();
		typeface=Utility.getTypeface(context);
		activity=(Activity)context;
		dbManager=new DBManager(context);
		situaList=new ArrayList<SituaEntity>();
		timer=new TimerUtilty(context, dbManager);
		
		
	}

	


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refresh();
		timer.startSitua();
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		situaView=inflater.inflate(R.layout.situation, container,false);
		title=(TextView)situaView.findViewById(R.id.situa_title);
		add=(Button)situaView.findViewById(R.id.addSitua);
		add.setTypeface(typeface);
		add.setOnClickListener(this);
		title.setTypeface(typeface);
		refresh=(Button)situaView.findViewById(R.id.refreshSitua);
		refresh.setTypeface(typeface);
		refresh.setOnClickListener(this);
		situaGv=(GridView)situaView.findViewById(R.id.situa_list);
		
		refresh();
		return situaView;
		
		
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addSitua:
			Intent intent=new Intent(context,SituaActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	public void refresh(){
		situaList.clear();
		situaList=dbManager.getSitua();
		Log.e("---------", situaList.size()+"");
		adapter=new SituaAdapter(context, situaList,dbManager);
		situaGv.setAdapter(adapter);
	}
}
