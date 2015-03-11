package com.li.ble220.Fragment;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.Adapter.BleAdapter;
import com.li.ble220.Adapter.TimerAdapter;
import com.li.ble220.DB.DBManager;
import com.li.ble220.Entity.TimerEntity;
import com.li.ble220.Util.Utility;
import com.li.ble220.Widget.DiyDialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class TimerFragment extends Fragment implements OnClickListener{
	
	private View timeView;
	private TextView title;
	private Context context;
	private Button addBtn,refreshBtn;
	private ListView timeLv;
	private DBManager dbManager;
	private List<TimerEntity> timerList;
	private TimerAdapter timerAdapter;
	Dialog dialog;
	private int visibleCount;
	private int visibleLast;
//	private View view;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=(MainActivity)getActivity();
		dbManager=new DBManager(context);
//		view = View.inflate(context, R.layout.time_add, null);
		
		dialog = new DiyDialog(context,R.style.MyDialog,dbManager);
		timerList=new ArrayList<TimerEntity>();
		
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		timeView=inflater.inflate(R.layout.timer, container,false);
		title=(TextView)timeView.findViewById(R.id.timer_title);
		addBtn=(Button)timeView.findViewById(R.id.addTimer);
		refreshBtn=(Button)timeView.findViewById(R.id.refresh);
		title.setTypeface(Utility.getTypeface(context));
		addBtn.setTypeface(Utility.getTypeface(context));
		refreshBtn.setTypeface(Utility.getTypeface(context));
		addBtn.setOnClickListener(this);
		refreshBtn.setOnClickListener(this);
		timeLv=(ListView)timeView.findViewById(R.id.timer_list);
		timeLv.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				visibleCount = visibleItemCount;
				visibleLast = firstVisibleItem + visibleItemCount - 1;
				
			}
		});
		
		return timeView;
	}
	public int getSelection(){
		return visibleLast - visibleCount+1;
	}

	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		refreshTimer(0);
		
		super.onResume();
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addTimer:
//			AlertDialog.Builder builder = new Builder(context);
//			AlertDialog dialog = builder.setView(view).setPositiveButton("¹Ø±Õ", new DialogInterface.OnClickListener(){
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//					}).show();
			

            dialog.show();
			break;
		case R.id.refresh:
			refreshTimer(0);
			break;
		default:
			break;
		}
		
	}
	
	public void refreshTimer(int selection) {
		timerList.clear();
		
		timerList=dbManager.getEquipmentTimer();
		timerAdapter=new TimerAdapter(context, timerList, dbManager);
		timerAdapter.notifyDataSetChanged();
		timeLv.setAdapter(timerAdapter);
		timeLv.setSelection(selection);
		
	}
	
	
	
	
}
