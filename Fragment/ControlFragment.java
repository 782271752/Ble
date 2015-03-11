package com.li.ble220.Fragment;

import com.li.ble220.MainActivity;
import com.li.ble220.R;
import com.li.ble220.Adapter.HouseAdapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ControlFragment extends Fragment{
	
	private LivingFragment livingFragment;
	private BedOneFragment bedOneFragment;
	private BedTwoFragment bedTwoFragment;
	private BalconyFragment balconyFragment;
	private KitWCFragment kitWCFragment;
	
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	
	private MainActivity activity;
	private View control;
	private GridView controlGridView;
	
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
		control=inflater.inflate(R.layout.control_fragment, container,false);
		controlGridView=(GridView)control.findViewById(R.id.control_gridview);
		controlGridView.setAdapter(new HouseAdapter(activity));
		
		return control;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		controlGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setSelect(position);
			}
		});
		setSelect(0);
	}
	public void setSelect(int position){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		
		switch (position) {
		
		case 0:
			hideFragments(transaction);
			if (livingFragment == null) {
				livingFragment = new LivingFragment();
				
				transaction.add(R.id.control_content,livingFragment,"living");
			} else {
				transaction.show(livingFragment);
			}
            break;
		case 1:
			hideFragments(transaction);
			if (bedOneFragment == null) {
				bedOneFragment = new BedOneFragment();
				transaction.add(R.id.control_content, bedOneFragment,"bedone");
			} else {
				transaction.show(bedOneFragment);
			}
            break;
		case 2:
			hideFragments(transaction);
			if (bedTwoFragment == null) {
				bedTwoFragment = new BedTwoFragment();
				transaction.add(R.id.control_content, bedTwoFragment,"bedtwo");
			} else {
				transaction.show(bedTwoFragment);
			}
            break;
		case 3:
			hideFragments(transaction);
			if (balconyFragment == null) {
				balconyFragment = new BalconyFragment();
				transaction.add(R.id.control_content, balconyFragment,"bal");
			} else {
				transaction.show(balconyFragment);
			}
            break;
		case 4:
			hideFragments(transaction);
			if (kitWCFragment == null) {
				kitWCFragment = new KitWCFragment();
				transaction.add(R.id.control_content, kitWCFragment,"kw");
			} else {
				transaction.show(kitWCFragment);
			}
            break;
		default:
			break;
		
		}
		transaction.commit();
	}
	
	private void hideFragments(FragmentTransaction transaction) {
		
		if(livingFragment !=null){
			transaction.hide(livingFragment);
		}
		if (bedOneFragment != null) {
			transaction.hide(bedOneFragment);
		}
		if (bedTwoFragment != null) {
			transaction.hide(bedTwoFragment);
		}
		if (balconyFragment != null) {
			transaction.hide(balconyFragment);
		}
		if (kitWCFragment != null) {
			transaction.hide(kitWCFragment);
		}
//		if (aboutUsFragment!=null){
//			transaction.hide(aboutUsFragment);
//		}
	}
	
	
	
}
