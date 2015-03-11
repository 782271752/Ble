package com.li.ble220.Fragment;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.Adapter.MenuAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;

public class MenuFragment extends Fragment{

	private ControlFragment controlFragment;
	private TimerFragment timerFragment;
	private SituaFragment situaFragment;
	private ConnectFragment connectFragment;
	private SettingFragment settingFragment;
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	
	private MainActivity activity;
	private View menuView;
	private GridView menuGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (MainActivity)getActivity();
//		fragmentManager = activity.getFragmentManager();
		fragmentManager = activity.getFragmentManager();
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		menuView=inflater.inflate(R.layout.menu_fragment, container,false);
		menuGridView=(GridView)menuView.findViewById(R.id.menu_gridview);
		menuGridView.setAdapter(new MenuAdapter(activity));
		
		
		return menuView;
	}
	
	
	
	
	public void setSelect(int position){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		
		switch (position) {
		
		case 0:
			hideFragments(transaction);
			if (controlFragment == null) {
				controlFragment = new ControlFragment();
				transaction.add(R.id.content, controlFragment,"control");
			} else {
				transaction.show(controlFragment);
			}
            break;
		case 1:
			hideFragments(transaction);
			if (timerFragment == null) {
				timerFragment = new TimerFragment();
				transaction.add(R.id.content, timerFragment,"time");
				
			} else {
				transaction.show(timerFragment);
			}
            break;
		case 2:
			hideFragments(transaction);
			if (situaFragment == null) {
				situaFragment = new SituaFragment();
				transaction.add(R.id.content, situaFragment,"situation");
			} else {
				transaction.show(situaFragment);
			}
            break;
		case 3:
			hideFragments(transaction);
			if (connectFragment == null) {
				connectFragment = new ConnectFragment();
				transaction.add(R.id.content, connectFragment,"connect");
			} else {
				transaction.show(connectFragment);
			}
			break;
		case 4:
			hideFragments(transaction);
			if (settingFragment == null) {
				settingFragment = new SettingFragment();
				transaction.add(R.id.content, settingFragment);
			} else {
				transaction.show(settingFragment);
			}
			break;
		default:
			break;
		
		}
		transaction.commit();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		menuGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setSelect(position);
				
			}
		});
		setSelect(0);
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		
		if(controlFragment !=null){
			transaction.hide(controlFragment);
		}
		if (timerFragment != null) {
			transaction.hide(timerFragment);
		}
		if (situaFragment != null) {
			transaction.hide(situaFragment);
		}
		if (connectFragment != null) {
			transaction.hide(connectFragment);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}
	}

}
